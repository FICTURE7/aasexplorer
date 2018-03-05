package com.ficture7.aasexplorer.model.examination

import com.ficture7.aasexplorer.Loader
import com.ficture7.aasexplorer.Saver

/**
 * Represents the A & AS level examination.
 *
 * @author FICTURE7
 */
class ALevelExamination
/**
 * Constructs a new instance of the [ALevelExamination] class with the specified [Loader]
 * and [Saver] which will be passed to [Examination.subjects].
 *
 * @param loader [Loader] instance.
 * @param saver  [Saver] instance.
 * @throws NullPointerException `getLoader` is null.
 * @throws NullPointerException `getSaver` is null.
 */
(loader: Loader, saver: Saver) : Examination(loader, saver) {

    /**
     * Returns the getName of the [Examination].
     *
     * @return Name of the [Examination].
     */
    override val name: String
        get() = "A & AS Level"
}
