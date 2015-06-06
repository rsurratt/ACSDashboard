package com.surrattfamily.acsdash.model;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class DashboardItem
{
    private Relay m_relay;
    private Stats m_actual;
    private String m_date;

    public DashboardItem(Relay relay, Stats actual, String date)
    {
        m_relay = relay;
        m_actual = actual;
        m_date = date;
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

