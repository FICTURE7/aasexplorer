package com.ficture7.aasexplorer.model

import com.ficture7.aasexplorer.Loader
import com.ficture7.aasexplorer.Saver
import com.ficture7.aasexplorer.model.examination.Examination

import java.util.Locale

import com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull

/**
 * Represents a subject.
 *
 * @author FICTURE7
 */
open class Subject {

    /**
     * Returns the subject ID/code of the [Subject].
     *
     * @return Subject ID/code of the [Subject].
     */
    val id: Int

    /**
     * Returns the getName of the [Subject].
     *
     * @return Name of the [Subject].
     */
    val name: String

    /**
     * Returns the resources of the [Subject].
     *
     * @return Resources of the [Subject].
     */
    val resources: ResourceRepository

    /**
     * Returns the getSources of the [Subject].
     *
     * @return Sources of the [Subject].
     */
    val sources: Source.List<SubjectSource>

    /**
     * Returns the [Examination] of the [Subject].
     *
     * @return [Examination] of the [Subject].
     */
    val examination: Examination

    /**
     * Constructs a new instance of the [Subject] class with the specified [Examination],
     * [Loader], [Saver], subject ID/code and getName.
     *
     * @param examination [Examination].
     * @param loader      [Loader].
     * @param saver       [Saver].
     * @param id          Subject ID/code.
     * @param name        Subject getName.
     * @throws NullPointerException `getName` is null.
     * @throws NullPointerException `examination` is null.
     */
    internal constructor(examination: Examination, loader: Loader, saver: Saver, id: Int, name: String) {
        this.examination = examination
        this.resources = ResourceRepository(this, loader, saver)
        this.sources = Source.List()
        this.id = id
        this.name = name
    }

    /**
     * Returns the hash code of the [Subject] instance.
     *
     * @return Hash code of the [Subject] instance.
     */
    override fun hashCode(): Int {
        return id
    }

    /**
     * Returns the string representation of the [Subject] instance.
     *
     * @return String representation of the [Subject].
     */
    override fun toString(): String {
        return String.format(Locale.US, "%s (%d)", name, id)
    }

    /**
     * Determines if the [Subject] instance is equal to the specified [Object].
     *
     * @return true if equal; otherwise false.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (javaClass != other?.javaClass)
            return false

        other as Subject

        if (id != other.id) return false
        if (name != other.name) return false
        if (resources != other.resources) return false
        if (sources != other.sources) return false
        if (examination != other.examination) return false

        return true
    }

    /**
     * Represents a [Subject] factory.
     *
     * @author FICTURE7.
     */
    class Factory
    /**
     * Constructs a new instance of the [Factory] class with the specified
     * [Examination], [Loader] and [Saver].
     *
     * @param examination [Examination] instance.
     * @param loader      [Loader] instance.
     * @param saver       [Saver] instance.
     * @throws NullPointerException `examination` is null.
     * @throws NullPointerException `getLoader` is null.
     * @throws NullPointerException `getSaver` is null.
     */
    (examination: Examination, loader: Loader, saver: Saver) {

        private val examination: Examination
        private val loader: Loader
        private val saver: Saver

        init {
            this.examination = checkNotNull(examination, "examination")
            this.loader = checkNotNull(loader, "getLoader")
            this.saver = checkNotNull(saver, "getSaver")
        }

        /**
         * Creates a new instance of a [Subject] with the specified subject ID/code
         * and getName.
         *
         * @param id Subject ID/code.
         * @param name Subject getName.
         * @return [Subject] instance.
         * @throws NullPointerException `getName` is null.
         */
        fun create(id: Int, name: String): Subject {
            checkNotNull(name, "getName")
            return Subject(examination, loader, saver, id, name)
        }
    }
}
