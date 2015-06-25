package com.surrattfamily.acsdash.action;

import com.surrattfamily.acsdash.model.Relay;
import com.surrattfamily.acsdash.renderer.CSVRenderer;
import com.surrattfamily.acsdash.renderer.Renderer;
import com.surrattfamily.acsdash.renderer.VelocityRenderer;

import java.util.List;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class RelaysAction implements Function<ActionContext, Renderer>
{
    private Format m_format;

    public RelaysAction(Format format)
    {
        m_format = format;
    }

    @Override
    public Renderer apply(ActionContext actionContext)
    {
        List<Relay> relays =
            actionContext.getRelays().stream()
                         .sorted(comparing(Relay::getName))
                         .collect(toList());

        if (m_format == Format.CSV)
        {
            CSVRenderer<Relay> renderer = new CSVRenderer<>(relays);
            renderer.addColumn("Code", Relay::getCode);
            renderer.addColumn("Name", Relay::getName);
            renderer.addColumn("Manager", Relay::getStaffPartner);
            renderer.addColumn("Dollar Goal", relay -> Integer.toString(relay.getGoal().getDollarsRaised()));
            renderer.addColumn("Teams Goal", relay -> Integer.toString(relay.getGoal().getTeams()));
            renderer.addColumn("Participants Goal", relay -> Integer.toString(relay.getGoal().getParticipants()));
            renderer.addColumn("Home Page", Relay::getHomePage);
            return renderer;
        }

        VelocityRenderer renderer = new VelocityRenderer("relays", "All relays");
        renderer.put("relays", relays);
        renderer.put("downloadLink", "/relays.csv");
        return renderer;
    }
}
