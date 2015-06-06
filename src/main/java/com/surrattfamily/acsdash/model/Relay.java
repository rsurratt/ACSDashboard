package com.surrattfamily.acsdash.model;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class Relay
{
    private String m_name;
    private String m_staffPartner;
    private String m_homePage;
    private Goal m_goal;

    public Relay(String name,
                 String staffPartner,
                 String homePage,
                 int dollarsRaisedGoal,
                 int participantsGoal,
                 int teamsGoal)
    {
        m_name = name;
        m_staffPartner = staffPartner;
        m_homePage = homePage;
        m_goal = new Goal(dollarsRaisedGoal, participantsGoal, teamsGoal);
    }

    public String getName()
    {
        return m_name;
    }

    public String getStaffPartner()
    {
        return m_staffPartner;
    }

    public String getHomePage()
    {
        return m_homePage;
    }

    public Goal getGoal()
    {
        return m_goal;
    }
}
