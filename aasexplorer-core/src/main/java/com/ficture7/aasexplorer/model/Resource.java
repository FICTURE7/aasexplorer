package com.ficture7.aasexplorer.model;

import com.ficture7.aasexplorer.util.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Represents a resource of a {@link Subject}.
 *
 * @author FICTURE7
 */
public class Resource {

    private final String name;
    private final Source.List<ResourceSource> sources;

    /**
     * Constructs a new instance of the {@link Resource} class with the specified resource name.
     *
     * @param name Resource name.
     * @throws NullPointerException {@code name} is null.
     */
    public Resource(String name) {
        this.name = checkNotNull(name, "name");
        this.sources = new Source.List<>();
    }

    /**
     * Gets the name of the {@link Resource}.
     *
     * @return Name of the {@link Resource}.
     */
    public String name() {
        return name;
    }

    /**
     * Gets the sources of the {@link Resource}.
     *
     * @return Sources of the {@link Resource}.
     */
    public Source.List<ResourceSource> sources() {
        return sources;
    }
}

