package com.surrattfamily.acsdash.model;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class Goal
{
    private final int m_dollarsRaisedGoal;
    private final int m_participantsGoal;
    private final int m_teamsGoal;

    public static Goal ZERO = new Goal(0, 0, 0);

    public Goal(int dollarsRaisedGoal, int participantsGoal, int teamsGoal)
    {
        m_dollarsRaisedGoal = dollarsRaisedGoal;
        m_participantsGoal = participantsGoal;
        m_teamsGoal = teamsGoal;
    }

    public int getDollarsRaisedGoal()
    {
        return m_dollarsRaisedGoal;
    }

    public int getParticipantsGoal()
    {
        return m_participantsGoal;
    }

    public int getTeamsGoal()
    {
        return m_teamsGoal;
    }

    public static Goal sum(Goal a, Goal b)
    {
        return new Goal(a.m_dollarsRaisedGoal + b.m_dollarsRaisedGoal,
                        a.m_participantsGoal + b.m_participantsGoal,
                        a.m_teamsGoal + b.m_teamsGoal);
    }
}
