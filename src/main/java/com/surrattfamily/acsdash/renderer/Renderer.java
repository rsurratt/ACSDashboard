package com.surrattfamily.acsdash.renderer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public interface Renderer
{
    public void render(HttpServletResponse response) throws IOException;
}
