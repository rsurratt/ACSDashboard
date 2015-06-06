package com.surrattfamily.acsdash.renderer;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class StaticFileRenderer implements Renderer
{
    private String m_filename;

    public StaticFileRenderer(String filename)
    {
        m_filename = filename;
    }

    @Override
    public void render(HttpServletResponse response) throws IOException
    {
        URL url = Resources.getResource("static" + m_filename);
        ByteSource source = Resources.asByteSource(url);
        source.copyTo(response.getOutputStream());
    }
}
