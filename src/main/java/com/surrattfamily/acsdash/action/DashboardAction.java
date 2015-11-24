package com.surrattfamily.acsdash.action;

import com.surrattfamily.acsdash.PageFetcher;
import com.surrattfamily.acsdash.model.DashboardItem;
import com.surrattfamily.acsdash.model.Relay;
import com.surrattfamily.acsdash.model.Stats;
import com.surrattfamily.acsdash.renderer.CSVRenderer;
import com.surrattfamily.acsdash.renderer.Renderer;
import com.surrattfamily.acsdash.renderer.VelocityRenderer;

import java.time.LocalDate;
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
    private Format m_format;

    public DashboardAction(Format format)
    {
        m_format = format;
    }

    protected String getTemplate()
    {
        return "dashboard";
    }

    protected String getPageTitle()
    {
        return "Overview Dashboard";
    }

    @Override
    public Renderer apply(ActionContext actionContext)
    {
        Predicate<Relay> predicate = r -> true;
        String pageTitle = getPageTitle();
        boolean isOverview = true;

        if (!actionContext.getParams().isEmpty())
        {
            String staffPartner = actionContext.getParams().get(0);
            pageTitle = staffPartner + "'s Dashboard";
            predicate = r -> r.getStaffPartner().equals(staffPartner);
            isOverview = false;
        }

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

        switch (m_format)
        {
            case HTML:
            {
                VelocityRenderer renderer = new VelocityRenderer(getTemplate(), pageTitle);
                renderer.put("items", items);
                renderer.put("total", new DashboardItem(new Relay(totalGoal), totalActual, null, null, null));
                renderer.put("isOverview", isOverview);
                if (isOverview)
                {
                    renderer.put("downloadLink", "/dashboard.csv");
                }
                return renderer;
            }

            case CSV:
            {
                CSVRenderer<DashboardItem> renderer = new CSVRenderer<>(items);
                renderer.addColumn("Date", DashboardItem::getDate);
                if (isOverview)
                {
                    renderer.addColumn("Manager", item -> item.getRelay().getStaffPartner());
                }
                renderer.addColumn("Relay", item -> item.getRelay().getName());
                renderer.addColumn("Dollars", item -> Integer.toString(item.getActual().getDollarsRaised()));
                renderer.addColumn("Dollars%", item -> item.getDollarsRaisedPercentage() + "%");
                renderer.addColumn("Teams", item -> Integer.toString(item.getActual().getTeams()));
                renderer.addColumn("Teams%", item -> item.getTeamsPercentage() + "%");
                renderer.addColumn("Participants", item -> Integer.toString(item.getActual().getParticipants()));
                renderer.addColumn("Participants%", item -> item.getParticipantsPercentage() + "%");
                return renderer;
            }

            case DELTA_CSV:
            {
                LocalDate now = LocalDate.now();
                String nowString = DashboardItem.DATE_FORMAT.format(now);

                CSVRenderer<DashboardItem> renderer = new CSVRenderer<>(items);
                renderer.addColumn("Date", item -> nowString);
                renderer.addColumn("Year", DashboardItem::getYearAsString);
                renderer.addColumn("Relay", item -> item.getRelay().getCode());
                renderer.addColumn("DeltaDate", item -> Long.toString(-item.getDaysUntil()));
                renderer.addColumn("Dollars", item -> Integer.toString(item.getActual().getDollarsRaised()));
                renderer.addColumn("Teams", item -> Integer.toString(item.getActual().getTeams()));
                renderer.addColumn("Participants", item -> Integer.toString(item.getActual().getParticipants()));
                return renderer;
            }
        }
        return null;  // can't get here
    }
}
