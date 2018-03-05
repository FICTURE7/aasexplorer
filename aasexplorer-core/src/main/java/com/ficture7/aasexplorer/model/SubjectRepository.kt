package com.ficture7.aasexplorer.model

import com.ficture7.aasexplorer.Loader
import com.ficture7.aasexplorer.Saver
import com.ficture7.aasexplorer.model.examination.Examination

import java.util.ArrayList
import java.util.HashMap

/**
 * Represents a [repository][Repository] of [subjects][Subject].
 *
 * @author FICTURE7
 */
class SubjectRepository : Repository<Int, Subject> {

    //TODO: Create some form factory to create Subject & Resource instances.

    private var _isLoaded: Boolean = false

    private val factory: Subject.Factory
    private val examination: Examination
    private val examinationClass: Class<Examination>

    private val loader: Loader
    private val saver: Saver

    // Maps subject ID/codes to subject instances.
    private val subjects: MutableMap<Int, Subject>

    /**
     * Constructs a new instance of the [SubjectRepository] class with the
     * specified [Examination], [Loader] and [Saver].
     *
     * @param examination [Examination] instance.
     * @param loader      [Loader] instance.
     * @param saver       [Saver] instance.
     * @throws NullPointerException `examination` is null.
     * @throws NullPointerException `getLoader` is null.
     * @throws NullPointerException `getSaver` is null.
     */
    internal constructor(examination: Examination, loader: Loader, saver: Saver) {
        this.examination = examination
        this.loader = loader
        this.saver = saver

        subjects = HashMap()
        factory = Subject.Factory(examination, loader, saver)
        examinationClass = examination.javaClass
    }

    /**
     * Returns a boolean value indicating weather the [SubjectRepository] is loaded.
     *
     * @return A boolean value indicating weather the [SubjectRepository] is loaded.
     */
    override val isLoaded: Boolean
        get() = _isLoaded

    /**
     * Returns the [Subject] with the specified subject ID/code.
     *
     * @param subjectId Subject ID/code.
     * @return [Subject] with the specified subject ID/code.
     */
    override fun get(subjectId: Int): Subject? {
        return subjects[subjectId]
    }

    /**
     * Puts the specified [Subject] instance to the [SubjectRepository].
     *
     * @param subject [Subject] instance.
     */
    override fun put(subject: Subject) {
        subjects.put(subject.id, subject)
    }

    /**
     * Loads the [SubjectRepository].
     *
     * @throws Exception When an exception is thrown.
     */
    @Throws(Exception::class)
    override fun load() {
        // Unloads the repository to prevent duplication.
        unload()

        // Get the subject getSources from the getLoader which can come from a
        // a getClient or a getStore.
        val sources = loader.loadSubjects<Examination>(examinationClass)

        for (source in sources) {
            var subject: Subject? = get(source.id)

            if (subject != null) {
                subject.sources.add(source)
            } else {
                subject = factory.create(source.id, source.name)
                subject.sources.add(source)

                put(subject)
            }
        }

        _isLoaded = true
    }

    /**
     * Loads the [SubjectRepository] asynchronously.
     *
     * @param callback Callback initializer.
     */
    override fun loadAsync(callback: Repository.LoadCallback.() -> Unit) {
        val loadCallback = Repository.LoadCallback()
        loadCallback.callback()

        // Unloads the repository to prevent duplication.
        unload()

        loader.executor.execute {
            try {
                // Get the subject getSources from the getLoader which can come from a
                // a getClient or a getStore.
                val sources = loader.loadSubjects<Examination>(examinationClass)

                for (source in sources) {
                    var subject: Subject? = get(source.id)

                    if (subject != null) {
                        subject.sources.add(source)
                    } else {
                        subject = factory.create(source.id, source.name)
                        subject.sources.add(source)

                        put(subject)
                    }
                }
                _isLoaded = true

                // Call the callback on the callback getExecutor.
                loader.executor.callbackExecutor.execute {
                    loadCallback.onLoadCallback?.invoke()
                }
            } catch (e: Exception) {
                // Call the callback on the callback getExecutor.
                loader.executor.callbackExecutor.execute {
                    loadCallback.onErrorCallback?.invoke(e)
                }
            }
        }
    }

    /**
     * Unloads the [SubjectRepository].
     */
    override fun unload() {
        subjects.clear()
        _isLoaded = false
    }

    /**
     * Saves the [SubjectRepository].
     *
     * @throws Exception When an exception is thrown.
     */
    @Throws(Exception::class)
    override fun save() {
        // Merge all the subject getSources into a single linear list.
        val sources = ArrayList<SubjectSource>()
        for (subject in this) {
            for (source in subject.sources) {
                sources.add(source)
            }
        }

        // Pass the subjects to save to the getSaver instance.
        saver.saveSubjects<Examination>(examinationClass, sources)
    }

    /**
     * Saves the [SubjectRepository].
     *
     * @param callback Callback initializer.
     */
    override fun saveAsync(callback: Repository.SaveCallback.() -> Unit) {
        val saveCallback = Repository.SaveCallback()
        saveCallback.callback()

        val sources = ArrayList<SubjectSource>()
        for (subject in this) {
            for (source in subject.sources) {
                sources.add(source)
            }
        }

        saver.executor.execute {
            try {

                saver.saveSubjects<Examination>(examinationClass, sources)

                saver.executor.callbackExecutor.execute {
                    saveCallback.onSaveCallback?.invoke()
                }
            } catch (e: Exception) {
                saver.executor.callbackExecutor.execute {
                    saveCallback.onErrorCallback?.invoke(e)
                }
            }
        }
    }

    /**
     * Returns an iterator through the [Subject] in the [SubjectRepository].
     *
     * @return An iterator through the [Subject] in the [SubjectRepository].
     */
    override fun iterator(): MutableIterator<Subject> {
        return subjects.values.iterator()
    }
}
