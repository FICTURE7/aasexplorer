package com.ficture7.aasexplorer.model;

/**
 * Represents a question paper resource.
 *
 * @author FICTURE7
 */
public class QuestionPaper extends Resource {

    private final Session session;

    /**
     * Constructs a new instance of the {@link QuestionPaper} class with the specified resource name
     * and session.
     *
     * @param name    Resource name.
     * @param session Session.
     * @throws NullPointerException {@code name} is null.
     * @throws NullPointerException {@code session} is null.
     */
    public QuestionPaper(String name, Session session) {
        super(name);

        this.session = session;
    }

    /**
     * Gets the {@link Session} of the {@link QuestionPaper}.
     *
     * @return {@link Session} of the {@link QuestionPaper}.
     */
    public Session session() {
        return session;
    }
}
