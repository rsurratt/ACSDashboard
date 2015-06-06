package com.surrattfamily.acsdash.model;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class Relays implements Iterable<Relay>
{
    private final ImmutableList<Relay> m_relays;

    public Relays(List<Relay> relays)
    {
        m_relays = ImmutableList.copyOf(relays);
    }

    public Stream<Relay> stream()
    {
        return m_relays.stream();
    }

    @Override
    public Iterator<Relay> iterator()
    {
        return m_relays.iterator();
    }

    public static Relays loadFromCSV(String filename) throws IOException
    {
        URL url = Resources.getResource("data/relays.csv");
        List<String> lines = Resources.readLines(url, Charsets.UTF_8);

        List<Relay> relays = lines.stream()
            .skip(1)
            .map(line -> line.split(","))
            .map(parts -> new Relay(parts[0],  // name
                                    parts[1],  // staffPartner
                                    parts[5],  // homePage
                                    Integer.parseInt(parts[2]), // dollars
                                    Integer.parseInt(parts[4]), // participants
                                    Integer.parseInt(parts[3]))) // teams
            .collect(toList());

        return new Relays(relays);
    }
}
