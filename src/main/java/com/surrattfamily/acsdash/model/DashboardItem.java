package com.surrattfamily.acsdash.model;

import com.surrattfamily.acsdash.PageFetcher;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class DashboardItem
{
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Relay  m_relay;
    private Stats  m_actual;
    private String m_date;
    private String m_datePattern;
    private String m_rawDate;

    public DashboardItem(Relay relay, Stats actual, String date, String datePattern, String rawDate)
    {
        m_relay = relay;
        m_actual = actual;
        m_date = date;
        m_datePattern = datePattern;
        m_rawDate = rawDate;
    }

    public Relay getRelay()
    {
        return m_relay;
    }

    public Stats getActual()
    {
        return m_actual;
    }

    public String getDate()
    {
        return m_date;
    }

    public String getDatePattern()
    {
        return m_datePattern;
    }

    public String getRawDate()
    {
        return m_rawDate;
    }

    public long getDaysUntil()
    {
        try
        {
            if (!PageFetcher.UNKNOWN_DATE.equals(m_date))
            {
                LocalDate eventDate = LocalDate.parse(m_date, DATE_FORMAT);
                return ChronoUnit.DAYS.between(LocalDate.now(ZoneId.of("America/New_York")), eventDate);
            }
        }
        catch (Exception e)
        {
            // ignore
        }
        return 0;
    }

    public String getYearAsString()
    {
        if (PageFetcher.UNKNOWN_DATE.equals(m_date))
        {
            return "??";
        }
        else
        {
            LocalDate eventDate = LocalDate.parse(m_date, DATE_FORMAT);
            return Integer.toString(eventDate.getYear());
        }
    }

    public int getDollarsRaisedPercentage()
    {
        return (int) (m_actual.getDollarsRaised() * 100.0 / m_relay.getGoal().getDollarsRaised());
    }

    public int getParticipantsPercentage()
    {
        return (int) (m_actual.getParticipants() * 100.0 / m_relay.getGoal().getParticipants());
    }

    public int getTeamsPercentage()
    {
        return (int) (m_actual.getTeams() * 100.0 / m_relay.getGoal().getTeams());
    }
}

