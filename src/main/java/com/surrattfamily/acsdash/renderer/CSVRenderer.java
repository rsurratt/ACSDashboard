package com.surrattfamily.acsdash.renderer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class CSVRenderer<T> implements Renderer
{
    private Map<String, Function<T, String>> m_columns = new LinkedHashMap<>();
    private List<T>                          m_rows    = new ArrayList<>();

    public CSVRenderer(List<T> rows)
    {
        m_rows = rows;
    }

    public void addColumn(String header, Function<T, String> columnSupplier)
    {
        m_columns.put(header, columnSupplier);
    }

    public void render(HttpServletResponse response) throws IOException
    {
        response.setContentType("text/csv");

        Writer out = response.getWriter();

        out.write(m_columns.keySet().stream().collect(joining(",")));
        out.write("\n");

        out.write(m_rows.stream()
                        .map(row ->
                            m_columns.values().stream()
                                              .map(func -> func.apply(row))
                                              .collect(joining(",")))
                        .collect(joining("\n")));
        out.write("\n");
        out.close();
    }
}
