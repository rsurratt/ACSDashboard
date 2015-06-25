package com.surrattfamily.acsdash.action;

import com.google.common.collect.ImmutableList;
import com.surrattfamily.acsdash.model.Relays;
import com.surrattfamily.acsdash.renderer.Renderer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class ActionContext
{
    private final String                m_pathInfo;
    private final ImmutableList<String> m_params;
    private final Relays                m_relays;

    public ActionContext(Matcher matcher, Relays relays)
    {
        m_relays = relays;
        m_pathInfo = matcher.group(0);

        ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (int i = 1; i <= matcher.groupCount(); i++)
        {
            builder.add(matcher.group(i));
        }
        m_params = builder.build();
    }

    public Relays getRelays()
    {
        return m_relays;
    }

    public String getPathInfo()
    {
        return m_pathInfo;
    }

    public ImmutableList<String> getParams()
    {
        return m_params;
    }
}
