package com.surrattfamily.acsdash.action;

/**
 * @author rsurratt
 * @since 6/25/15
 */
public class MoneyDashboardAction extends DashboardAction
{
    public MoneyDashboardAction(Format format)
    {
        super(format);
    }

    @Override
    protected String getTemplate()
    {
        return "moneyDashboard";
    }

    @Override
    protected String getPageTitle()
    {
        return "Money Dashboard";
    }
}
