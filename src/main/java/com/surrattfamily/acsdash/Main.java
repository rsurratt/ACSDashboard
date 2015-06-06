package com.surrattfamily.acsdash;

import com.google.common.collect.ImmutableMap;
import com.surrattfamily.acsdash.action.ActionContext;
import com.surrattfamily.acsdash.action.DashboardAction;
import com.surrattfamily.acsdash.action.IndexAction;
import com.surrattfamily.acsdash.action.RelaysAction;
import com.surrattfamily.acsdash.action.StaffPartnersAction;
import com.surrattfamily.acsdash.action.StaticFileAction;
import com.surrattfamily.acsdash.renderer.Renderer;
import com.surrattfamily.acsdash.model.Relays;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rsurratt
 * @since 6/5/15
 */
public class Main extends HttpServlet
{
    private final ImmutableMap<Pattern, Function<ActionContext, Renderer>> m_actions;
    private final Relays                                                   m_relays;

    public Main() throws IOException
    {
        m_actions = initActions();
        m_relays = Relays.loadFromCSV("data/relays.csv");
    }

    private ImmutableMap<Pattern, Function<ActionContext, Renderer>> initActions()
    {
        ImmutableMap.Builder<Pattern, Function<ActionContext, Renderer>> builder = ImmutableMap.builder();

        builder.put(Pattern.compile("/managers"), new StaffPartnersAction());
        builder.put(Pattern.compile("/relays(.*)"), new RelaysAction());
        builder.put(Pattern.compile("/dashboard"), new DashboardAction());
        builder.put(Pattern.compile("/dashboard/(.+)"), new DashboardAction());
        builder.put(Pattern.compile("/"), new IndexAction());
        builder.put(Pattern.compile("/.*"), new StaticFileAction());

        return builder.build();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String pathInfo = req.getPathInfo();
        System.out.println("[" + pathInfo + "]");

        for (Map.Entry<Pattern, Function<ActionContext, Renderer>> entry : m_actions.entrySet())
        {
            Matcher matcher = entry.getKey().matcher(pathInfo);
            if (matcher.matches())
            {
                ActionContext actionContext = new ActionContext(matcher, m_relays);
                Renderer renderer = entry.getValue().apply(actionContext);
                renderer.render(resp);
                break;
            }
        }
    }

    public static void main(String[] args)
    {
        try
        {
            Server server = new Server(Integer.valueOf(System.getenv("PORT")));
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            context.addServlet(new ServletHolder(new Main()), "/*");
            server.start();
            server.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
