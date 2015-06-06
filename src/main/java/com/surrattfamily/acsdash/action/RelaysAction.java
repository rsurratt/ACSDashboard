package com.surrattfamily.acsdash.action;

import com.surrattfamily.acsdash.model.Goal;
import com.surrattfamily.acsdash.model.Relay;
import com.surrattfamily.acsdash.renderer.Renderer;
import com.surrattfamily.acsdash.renderer.VelocityRenderer;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class RelaysAction implements Function<ActionContext, Renderer>
{
    @Override
    public Renderer apply(ActionContext actionContext)
    {
        String template = "relays" + actionContext.getParams().get(0);
        VelocityRenderer renderer = new VelocityRenderer(template, "All relays");

        List<Relay> relays =
            actionContext.getRelays().stream()
                         .sorted(comparing(Relay::getName))
                         .collect(toList());

        renderer.put("relays", relays);
        return renderer;
    }
}
