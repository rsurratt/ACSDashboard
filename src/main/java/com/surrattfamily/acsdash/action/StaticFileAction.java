package com.surrattfamily.acsdash.action;

import com.surrattfamily.acsdash.renderer.Renderer;
import com.surrattfamily.acsdash.renderer.StaticFileRenderer;

import java.util.function.Function;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class StaticFileAction implements Function<ActionContext,Renderer>
{
    @Override
    public Renderer apply(ActionContext actionContext)
    {
        return new StaticFileRenderer(actionContext.getPathInfo());
    }
}
