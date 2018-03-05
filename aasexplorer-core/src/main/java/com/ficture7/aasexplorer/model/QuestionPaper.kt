package com.ficture7.aasexplorer.model

/**
 * Represents a question paper resource.
 *
 * @author FICTURE7
 */
class QuestionPaper : Resource {

    /**
     * Constructs a new instance of the [QuestionPaper] class with the specified resource name,
     * session and paper number.
     *
     * @param name    Resource name.
     * @param session Session.
     * @param number  Paper number.
     */
    constructor(name: String, session: Session, number: Int) : super(name) {
        this.session = session
        this.number = number
    }

    /**
     * Returns the [Session] of the [QuestionPaper].
     *
     * @return [Session] of the [QuestionPaper].
     */
    val session: Session

    /**
     * Returns the paper number of the [QuestionPaper].
     *
     * @return Paper getNumber of the {@link QuestionPaper}.
     */
    val number: Int
}