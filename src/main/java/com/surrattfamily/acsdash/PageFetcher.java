package com.surrattfamily.acsdash;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;
import com.surrattfamily.acsdash.model.DashboardItem;
import com.surrattfamily.acsdash.model.Relay;
import com.surrattfamily.acsdash.model.Stats;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class PageFetcher
{
    public static final String UNKNOWN_DATE = "???";
    private static final int CONNECT_TIMEOUT = (int)TimeUnit.SECONDS.toMillis(1);
    private static final int READ_TIMEOUT = (int)TimeUnit.SECONDS.toMillis(15);

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
        String date = UNKNOWN_DATE;
        String datePattern = "N/A";
        String rawDate = "NO FOUND";

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
                rawDate = matcher.group(1);
                String[] result = parseDate(rawDate);
                date = result[0];
                datePattern = result[1];
            }
        }

        return new DashboardItem(relay, actual, date, datePattern, rawDate);
    }


    private static String date(Matcher matcher, int yearGroup, int monthGroup, int dayGroup)
    {
        String year = (yearGroup == 0) ? "2016" : matcher.group(yearGroup).trim();
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

    private static ImmutableList<Pattern> STRIP_PATTERNS
        = ImmutableList.of(Pattern.compile("Friday,? *"),
                           Pattern.compile("Saturday,? *"),
                           Pattern.compile("Sunday,? *"));

    private enum DatePattern
    {
        MDY(Pattern.compile("(\\w+) (\\d+), (\\d+)")),
        MD_DY(Pattern.compile("(\\w+) (\\d+)-\\d+, (\\d+)")),
        MD_MDY(Pattern.compile("(\\w+) (\\d+)-\\w+ \\d+, (\\d+)")),
        MDY_MDY(Pattern.compile("(\\w+) (\\d+), (\\d+) - \\w+ \\d+, \\d+"));

        private Pattern m_pattern;
        private int m_yearGroup  = 3;
        private int m_monthGroup = 1;
        private int m_dayGroup   = 2;

        DatePattern(Pattern pattern)
        {
            m_pattern = pattern;
        }

        public Matcher matcher(String s)
        {
            return m_pattern.matcher(s);
        }

        public String parseDate(Matcher matcher)
        {
            return date(matcher, m_yearGroup, m_monthGroup, m_dayGroup);
        }
    }

    private static String[] parseDate(String s)
    {
        for (Pattern strip : STRIP_PATTERNS)
        {
            Matcher matcher = strip.matcher(s);
            s = matcher.replaceAll("");
        }

        for (DatePattern pattern : DatePattern.values())
        {
            Matcher matcher = pattern.matcher(s);
            if (matcher.matches())
            {
                return new String[] { pattern.parseDate(matcher), pattern.name() };
            }
        }

        return new String[] { UNKNOWN_DATE, "Couldn't parse: [" + s + "]" };
    }


    public static void main(String[] args)
    {
        checkDate("Friday, May 6, 2016", "2016-05-06");
        checkDate("Friday, September 11-12, 2015", "2015-09-11");
        checkDate("June 11-12, 2016", "2016-06-11");
        checkDate("Friday, April 08-Saturday, April 09, 2016", "2016-04-08");
        checkDate("Saturday, May 21, 2016 - May 22, 2016", "2016-05-21");
        checkDate("TBD", "???");
    }

    public static void checkDate(String input, String expected)
    {
        String[] actual = parseDate(input);
        System.out.println(actual[0] + (actual[0].equals(expected) ? " GOOD" : " BAD from " + actual[1]));
    }
}
