package com.surrattfamily.acsdash.action;

import com.surrattfamily.acsdash.PageFetcher;
import com.surrattfamily.acsdash.model.DashboardItem;
import com.surrattfamily.acsdash.model.Relay;
import com.surrattfamily.acsdash.model.Stats;
import com.surrattfamily.acsdash.renderer.Renderer;
import com.surrattfamily.acsdash.renderer.VelocityRenderer;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class DashboardAction implements Function<ActionContext, Renderer>
{
    @Override
    public Renderer apply(ActionContext actionContext)
    {
        Predicate<Relay> predicate = r -> true;
        String pageTitle = "Overview Dashboard";
        boolean isOverview = true;

        if (!actionContext.getParams().isEmpty())
        {
            String staffPartner = actionContext.getParams().get(0);
            pageTitle = staffPartner + "'s Dashboard";
            predicate = r -> r.getStaffPartner().equals(staffPartner);
            isOverview = false;
        }

        String template = "dashboard";
        VelocityRenderer renderer = new VelocityRenderer(template, pageTitle);

        List<DashboardItem> items =
            actionContext.getRelays().parallelStream()
                         .filter(predicate)
                         .map(PageFetcher::parsePage)
                         .sorted(comparing(DashboardItem::getDate))
                         .collect(toList());

        Stats totalGoal = items.stream()
                               .map(item -> item.getRelay().getGoal())
                               .reduce(Stats.ZERO, Stats::sum);

        Stats totalActual = items.stream()
                                 .map(DashboardItem::getActual)
                                 .reduce(Stats.ZERO, Stats::sum);

        renderer.put("items", items);
        renderer.put("total", new DashboardItem(new Relay(totalGoal), totalActual, null));
        renderer.put("isOverview", isOverview);
        return renderer;
    }
}
