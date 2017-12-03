package com.ficture7.aasexplorer.model;

import com.ficture7.aasexplorer.util.ObjectUtil;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Represents a marking scheme resource.
 *
 * @author FICTURE7
 */
public class MarkingScheme extends Resource {

    private final Session session;

    /**
     * Constructs a new instance of the {@link MarkingScheme} class with the specified resource name
     * and session.
     *
     * @param name    Resource name.
     * @param session Session.
     * @throws NullPointerException {@code name} is null.
     * @throws NullPointerException {@code session} is null.
     */
    public MarkingScheme(String name, Session session) {
        super(name);

        this.session = checkNotNull(session, "session");
    }

    /**
     * Gets the {@link Session} of the {@link MarkingScheme}.
     *
     * @return {@link Session} of the {@link MarkingScheme}.
     */
    public Session session() {
        return session;
    }
}
