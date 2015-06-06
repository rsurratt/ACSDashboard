package com.surrattfamily.acsdash.action;

import com.surrattfamily.acsdash.model.Stats;
import com.surrattfamily.acsdash.model.Relay;
import com.surrattfamily.acsdash.renderer.Renderer;
import com.surrattfamily.acsdash.renderer.VelocityRenderer;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class StaffPartnersAction implements Function<ActionContext, Renderer>
{
    @Override
    public Renderer apply(ActionContext actionContext)
    {
        VelocityRenderer renderer = new VelocityRenderer("staffPartners", "Community Managers");

        List<String> staffPartners =
            actionContext.getRelays().stream()
                         .map(Relay::getStaffPartner)
                         .distinct()
                         .sorted()
                         .collect(toList());

        Map<String,Stats> goals =
            actionContext.getRelays().stream()
                .collect(groupingBy(Relay::getStaffPartner,
                                    mapping(Relay::getGoal,
                                            reducing(Stats.ZERO, Stats::sum))));

        renderer.put("staffPartners", staffPartners);
        renderer.put("goals", goals);
        return renderer;
    }
}
