package com.surrattfamily.acsdash.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class Relay
{
    private String m_name;
    private String m_specialist;
    private String m_homePage;
    private int m_dollarsRaisedGoal;
    private int m_participantsGoal;
    private int m_teamsGoal;

    @JsonCreator
    public Relay(@JsonProperty("name") String name,
                 @JsonProperty("specialist") String specialist,
                 @JsonProperty("homePage") String homePage,
                 @JsonProperty("dollarsRaisedGoal") int dollarsRaisedGoal,
                 @JsonProperty("participantsGoal") int participantsGoal,
                 @JsonProperty("teamsGoal") int teamsGoal)
    {
        m_name = name;
        m_specialist = specialist;
        m_homePage = homePage;
        m_dollarsRaisedGoal = dollarsRaisedGoal;
        m_participantsGoal = participantsGoal;
        m_teamsGoal = teamsGoal;
    }

    public String getName()
    {
        return m_name;
    }

    public String getSpecialist()
    {
        return m_specialist;
    }

    public String getHomePage()
    {
        return m_homePage;
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
}
