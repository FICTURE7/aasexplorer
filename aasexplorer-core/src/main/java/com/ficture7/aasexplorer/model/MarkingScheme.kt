package com.ficture7.aasexplorer.model

/**
 * Represents a marking scheme resource.
 *
 * @author FICTURE7
 */
class MarkingScheme : Resource {

    /**
     * Constructs a new instance of the [MarkingScheme] class with the specified resource name
     * and session.
     *
     * @param name    Resource name.
     * @param session Session.
     */
    constructor(name: String, session: Session) : super(name) {
        this.session = session
    }

    /**
     * Returns the [Session] of the [MarkingScheme].
     *
     * @return [Session] of the [MarkingScheme].
     */
    val session: Session
}