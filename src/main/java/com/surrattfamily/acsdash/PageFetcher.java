package com.surrattfamily.acsdash;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteStreams;
import com.surrattfamily.acsdash.model.DashboardItem;
import com.surrattfamily.acsdash.model.Relay;
import com.surrattfamily.acsdash.model.Stats;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class PageFetcher
{
    private static int CONNECT_TIMEOUT = (int)TimeUnit.SECONDS.toMillis(1);
    private static int READ_TIMEOUT = (int)TimeUnit.SECONDS.toMillis(15);

    private static String fetchPage(String homePage) throws IOException
    {
        URL url = new URL(homePage);

        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        HttpURLConnection.setFollowRedirects(true);
        huc.setReadTimeout(READ_TIMEOUT);
        huc.setConnectTimeout(CONNECT_TIMEOUT);
        huc.setRequestMethod("GET");
        huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
        huc.connect();

        InputStream in = huc.getInputStream();
        byte[] bytes = ByteStreams.toByteArray(in);

        return new String(bytes, Charsets.UTF_8);
    }


    private static Pattern DATE_PATTERN   = Pattern.compile("<p id=\"tr-greeting-eventInfo-date\">([^<]*)");
    private static Pattern STATUS_PATTERN = Pattern.compile(".*>([0-9]+) teams?.*>([0-9]+) participants?.*>\\$([0123456789,]+)");

    public static DashboardItem parsePage(Relay relay)
    {
        String page = null;
        try
        {
            page = fetchPage(relay.getHomePage());
        }
        catch (IOException e)
        {
            // ignore
        }

        Stats actual = Stats.ZERO;
        String date = "Not Found";

        if (page != null)
        {
            Matcher matcher = STATUS_PATTERN.matcher(page);
            if (matcher.find())
            {
                int teams = Integer.valueOf(matcher.group(1));
                int participants = Integer.valueOf(matcher.group(2));
                int dollarsRaised = Integer.valueOf(matcher.group(3).replace(",", ""));

                actual = new Stats(dollarsRaised, participants, teams);
            }

            matcher = DATE_PATTERN.matcher(page);
            if (matcher.find())
            {
                date = parseDate(matcher.group(1));
            }
        }

        return new DashboardItem(relay, actual, date);
    }


    private static String date(Matcher matcher, int yearGroup, int monthGroup, int dayGroup, String id)
    {
        String year = (yearGroup == 0) ? "2015" : matcher.group(yearGroup).trim();
        String month = matcher.group(monthGroup).trim();
        String day = matcher.group(dayGroup).trim();

        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        for (int i = 0; i < months.length; i++)
        {
            if (month.equals(months[i]))
            {
                month = String.format("%02d", i + 1);
                break;
            }
        }

        if (day.length() == 1)
        {
            day = "0" + day;
        }

        return String.format("%s-%s-%s", year, month, day);
    }

    private static ImmutableMap<Pattern, Function<Matcher, String>> DATE_PATTERNS
        = ImmutableMap.<Pattern, Function<Matcher, String>>builder()
                      .put(Pattern.compile("(\\w+) (\\d+)\\w*.*"), m -> date(m, 0, 1, 2, "a"))
                      .put(Pattern.compile("(\\w+), (\\w+) (\\d+)-(\\d+), (\\d+)"), m -> date(m, 5, 2, 3, "j"))
                      .put(Pattern.compile("(.*)-(.*),(.*)"), m -> date(m, 0, 1, 3, "b"))
                      .put(Pattern.compile("(.*), (\\w*) (\\d*)\\w*,(.*)"), m -> date(m, 4, 2, 3, "c"))
                      .put(Pattern.compile("(.*),(.*),(.*)"), m -> date(m, 0, 2, 3, "d"))
                      .put(Pattern.compile("(.*),(.*) ([0-9:]+PM)"), m -> date(m, 0, 1, 2, "e"))
                      .put(Pattern.compile("(.*), (201[34567])"), m -> date(m, 0, 1, 2, "f"))
                      .put(Pattern.compile("(.*),([^, ]+)(201[34567])"), m -> date(m, 3, 1, 2, "g"))
                      .put(Pattern.compile("(\\w*) (\\w*) (\\d*)\\w* (\\d*).*"), m -> date(m, 4, 2, 3, "h"))
                      .put(Pattern.compile("(\\w*), (\\w*) (\\d*) (\\d*)"), m -> date(m, 4, 2, 3, "i"))
                      .build();


    private static String parseDate(String s)
    {
        for (Map.Entry<Pattern, Function<Matcher, String>> entry : DATE_PATTERNS.entrySet())
        {
            Matcher matcher = entry.getKey().matcher(s);
            if (matcher.matches())
            {
                return entry.getValue().apply(matcher);
            }
        }

        System.out.println("Couldn't parse: [" + s + "]");
        return "Not Found";
    }


    public static void main(String[] args)
    {
        System.out.println(parseDate("March 28, 2014"));
        System.out.println(parseDate("Friday, June 27 2014"));
        System.out.println(parseDate("June 13- June 14, 2014"));
        System.out.println(parseDate("Saturday, June 14, 2014"));
        System.out.println(parseDate("Saturday, June 14,2014"));
        System.out.println(parseDate("September 6,2013 5:30PM"));
        System.out.println(parseDate("Saturday May 31st 2014 "));
        System.out.println(parseDate("April 11th-April 12th 6pm-8am"));
        System.out.println(parseDate("May 30th 10am to 11pm"));
        System.out.println(parseDate("Saturday, May 16th, 2015"));
        System.out.println(parseDate("Friday, June 12-13, 2015"));
        System.out.println(parseDate("Friday, September 11-12, 2015"));
    }
}
