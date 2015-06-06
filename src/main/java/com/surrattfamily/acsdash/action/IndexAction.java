package com.surrattfamily.acsdash.action;

import com.surrattfamily.acsdash.model.Relay;
import com.surrattfamily.acsdash.renderer.Renderer;
import com.surrattfamily.acsdash.renderer.VelocityRenderer;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class IndexAction implements Function<ActionContext, Renderer>
{
    @Override
    public Renderer apply(ActionContext actionContext)
    {
        List<String> staffPartners
            = actionContext.getRelays().stream()
                                       .map(Relay::getStaffPartner)
                                       .distinct()
                                       .sorted()
                                       .collect(toList());

        VelocityRenderer renderer = new VelocityRenderer("index", "Index");
        renderer.put("staffPartners", staffPartners);
        return renderer;
    }
}
