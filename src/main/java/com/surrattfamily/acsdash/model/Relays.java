package com.surrattfamily.acsdash.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author rsurratt
 * @since 6/6/15
 */
public class Relays implements Iterable<Relay>
{
    private final ImmutableList<Relay> m_relays;

    @JsonCreator
    public Relays(@JsonProperty("relays") List<Relay> relays)
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
}
