package com.ficture7.aasexplorer.model.examination

import com.ficture7.aasexplorer.Loader
import com.ficture7.aasexplorer.Saver
import com.ficture7.aasexplorer.model.SubjectRepository

/**
 * Represents an examination.
 *
 * @author FICTURE7
 */
abstract class Examination {

    /**
     * Returns the [SubjectRepository] of the [Examination].
     *
     * @return [SubjectRepository] of the [Examination].
     */
    val subjects: SubjectRepository

    /**
     * Returns the getName of the [Examination].
     *
     * @return Name of the [Examination].
     */
    abstract val name: String

    /**
     * Constructs a new instance of the [Examination] class with the specified [Loader]
     * and [Saver] which will be passed to [Examination.subjects].
     *
     * @param loader [Loader] instance.
     * @param saver  [Saver] instance.
     * @throws NullPointerException `getLoader` is null.
     * @throws NullPointerException `getSaver` is null.
     */
    protected constructor(loader: Loader, saver: Saver) {
        subjects = SubjectRepository(this, loader, saver)
    }
}
