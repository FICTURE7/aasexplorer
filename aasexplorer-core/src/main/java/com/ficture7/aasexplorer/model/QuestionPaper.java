package com.ficture7.aasexplorer.model;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Represents a question paper resource.
 *
 * @author FICTURE7
 */
public class QuestionPaper extends Resource {

    private final Session session;
    private final int number;

    /**
     * Constructs a new instance of the {@link QuestionPaper} class with the specified resource name
     * and session.
     *
     * @param name    Resource name.
     * @param session Session.
     * @param number  Paper number.
     * @throws NullPointerException {@code name} is null.
     * @throws NullPointerException {@code session} is null.
     */
    public QuestionPaper(String name, Session session, int number) {
        super(name);

        this.session = checkNotNull(session, "session");
        this.number = number;
    }

    /**
     * Gets the {@link Session} of the {@link QuestionPaper}.
     *
     * @return {@link Session} of the {@link QuestionPaper}.
     */
    public Session session() {
        return session;
    }

    /**
     * Gets the paper number of the {@link QuestionPaper}.
     *
     * @return Paper number of the {@link QuestionPaper}.
     */
    public int number() {
        return number;
    }
}
