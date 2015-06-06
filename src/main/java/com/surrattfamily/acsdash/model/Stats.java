package com.surrattfamily.acsdash.model;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class Stats
{
    private final int m_dollarsRaised;
    private final int m_participants;
    private final int m_teams;

    public static Stats ZERO = new Stats(0, 0, 0);

    public Stats(int dollarsRaised, int participants, int teams)
    {
        m_dollarsRaised = dollarsRaised;
        m_participants = participants;
        m_teams = teams;
    }

    public int getDollarsRaised()
    {
        return m_dollarsRaised;
    }

    public String getDollarsRaisedFormatted()
    {
        return String.format("$%,d", m_dollarsRaised);
    }

    public int getParticipants()
    {
        return m_participants;
    }

    public int getTeams()
    {
        return m_teams;
    }

    public static Stats sum(Stats a, Stats b)
    {
        return new Stats(a.m_dollarsRaised + b.m_dollarsRaised,
                         a.m_participants + b.m_participants,
                         a.m_teams + b.m_teams);
    }

    @Override
    public String toString()
    {
        return "Stats{" +
                   "m_dollarsRaised=" + m_dollarsRaised +
                   ", m_participants=" + m_participants +
                   ", m_teams=" + m_teams +
                   '}';
    }
}
