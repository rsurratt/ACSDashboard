package com.surrattfamily.acsdash.renderer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class VelocityRenderer implements Renderer
{
    private VelocityEngine  m_ve;
    private VelocityContext m_vc;
    private String          m_templateName;

    public VelocityRenderer(String templateName)
    {
        m_ve = new VelocityEngine(getVelocityProperties());
        m_vc = new VelocityContext();
        m_templateName = templateName;
    }

    private Properties getVelocityProperties()
    {
        Properties props = new Properties();
        props.setProperty("resource.loader", "class");
        props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return props;
    }

    public void put(String key, Object value)
    {
        m_vc.put(key, value);
    }

    public void render(HttpServletResponse response) throws IOException
    {
        Template template = m_ve.getTemplate("templates/" + m_templateName + ".vm");
        template.merge(m_vc, response.getWriter());
    }

}
