package com.surrattfamily.acsdash.action;

import com.surrattfamily.acsdash.PageFetcher;
import com.surrattfamily.acsdash.model.DashboardItem;
import com.surrattfamily.acsdash.model.Relay;
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
    private final Predicate<Relay> m_predicate;

    public DashboardAction()
    {
        m_predicate = relay -> true;
    }

    public DashboardAction(Predicate<Relay> predicate)
    {
        m_predicate = predicate;
    }

    @Override
    public Renderer apply(ActionContext actionContext)
    {
        String template = "dashboard";
        VelocityRenderer renderer = new VelocityRenderer(template, "All relays");

        List<DashboardItem> items =
            actionContext.getRelays().parallelStream()
                         .filter(m_predicate)
//                         .limit(4)
                         .map(PageFetcher::parsePage)
                         .sorted(comparing(DashboardItem::getDate))
                         .collect(toList());

        renderer.put("items", items);
        return renderer;
    }
}
