package com.ficture7.aasexplorer.model

import com.ficture7.aasexplorer.Loader
import com.ficture7.aasexplorer.Saver

import java.util.ArrayList
import java.util.Collections
import java.util.HashMap

/**
 * Represents a repository of [Resource].
 *
 * @author FICTURE7
 */
class ResourceRepository : Repository<String, Resource>, Iterable<Resource> {

    //TODO: Create a custom collection for resources to filter them by getDate and stuff.

    private var _isLoaded: Boolean = false

    private val subject: Subject
    private val loader: Loader
    private val saver: Saver

    // Maps resource getName to resource instances.
    private val resources: MutableMap<String, Resource>

    // Pre-sorted lists of resources.
    private val internalMarkingSchemes: MutableList<MarkingScheme>
    private val internalQuestionPapers: MutableList<QuestionPaper>
    private val internalOthers: MutableList<Resource>

    // Read-only collections to prevent leaks.
    private val readOnlyMarkingSchemes: Collection<MarkingScheme>
    private val readOnlyQuestionPapers: Collection<QuestionPaper>
    private val readOnlyOthers: Collection<Resource>

    /**
     * Constructs a new instance of th e[ResourceRepository] with the specified [Subject]
     * instance which owns this [ResourceRepository], [Loader] and [Saver].
     *
     * @param subject [Subject] instance which owns this [ResourceRepository].
     * @param loader  [Loader] instance.
     * @param saver   [Saver] instance.
     */
    internal constructor(subject: Subject, loader: Loader, saver: Saver) {
        this.subject = subject
        this.loader = loader
        this.saver = saver

        resources = HashMap()

        internalMarkingSchemes = ArrayList()
        internalQuestionPapers = ArrayList()
        internalOthers = ArrayList()

        readOnlyMarkingSchemes = Collections.unmodifiableCollection(internalMarkingSchemes)
        readOnlyQuestionPapers = Collections.unmodifiableCollection(internalQuestionPapers)
        readOnlyOthers = Collections.unmodifiableCollection(internalOthers)
    }

    /**
     * Returns a boolean value indicating weather the [ResourceRepository] is loaded.
     *
     * @return A boolean value indicating weather the [ResourceRepository] is loaded.
     */
    override val isLoaded: Boolean
        get() = _isLoaded

    /**
     * Returns an iterable which contains only [MarkingScheme]s.
     *
     * @return An iterable which contains only [MarkingScheme]s.
     */
    val markingSchemes: Iterable<MarkingScheme>
        get() = readOnlyMarkingSchemes

    /**
     * Returns an iterable which contains only [QuestionPaper]s.
     *
     * @return An iterable which contains only [QuestionPaper]s.
     */
    val questionPapers: Iterable<QuestionPaper>
        get() = readOnlyQuestionPapers

    /**
     * Returns an iterable which contains other unidentified [Resource]s.
     *
     * @return An iterable which contains other unidentified [Resource]s.
     */
    val others: Iterable<Resource>
        get() = readOnlyOthers

    /**
     * Returns the number of [Resource] which is in the repository.
     *
     * @return Number of [Resource] which is in the repository.
     */
    fun size(): Int {
        return resources.size
    }

    /**
     * Returns the [Resource] with the specified getName.
     * Returns null if no [Resource] with the specified getName is in the repository.
     *
     * @param resourceName Resource getName.
     * @return [Resource] with the specified getName.
     */
    override fun get(resourceName: String): Resource? {
        return resources[resourceName]
    }

    /**
     * Puts the specified [Resource] to the repository.
     *
     * @param resource [Resource] to put in the repository.
     */
    override fun put(resource: Resource) {
        // Add the Resource instance to the Map
        // mapping resource names to resource instances.
        resources.put(resource.name, resource)

        // Add the Resource instance to the corresponding list.
        when (resource) {
            is MarkingScheme -> internalMarkingSchemes.add(resource)
            is QuestionPaper -> internalQuestionPapers.add(resource)
            else -> internalOthers.add(resource)
        }
    }

    /**
     * Loads the [Repository].
     *
     * @throws Exception When an exception is thrown.
     */
    @Throws(Exception::class)
    override fun load() {
        unload()

        val sources = loader.loadResources(subject)

        for (source in sources) {
            var resource = get(source.name)

            if (resource == null) {
                // Turn the resource source to the resource type.
                // E.G: MarkingScheme, QuestionPaper or Resource it self if
                // unable to identified.
                resource = resourceSourceToResource(source)
                resource.sources.add(source)

                put(resource)
            } else {
                resource.sources.add(source)
            }
        }

        _isLoaded = true
    }

    /**
     * Loads the [ResourceRepository] asynchronously.
     *
     * @param callback Callback initializer.
     */
    override fun loadAsync(callback: Repository.LoadCallback.() -> Unit) {
        val loadCallback = Repository.LoadCallback()
        loadCallback.callback()

        unload()

        loader.executor.execute {
            try {
                val sources = loader.loadResources(subject)

                for (source in sources) {
                    var resource = get(source.name)

                    if (resource == null) {
                        // Turn the resource source to the resource type.
                        // E.G: MarkingScheme, QuestionPaper or Resource it self if
                        // unable to identified.
                        resource = resourceSourceToResource(source)
                        resource.sources.add(source)

                        put(resource)
                    } else {
                        resource.sources.add(source)
                    }
                }

                _isLoaded = true

                loader.executor.callbackExecutor.execute {
                    loadCallback.onLoadCallback?.invoke()
                }
            } catch (e: Exception) {
                loader.executor.callbackExecutor.execute {
                    loadCallback.onErrorCallback?.invoke(e)
                }
            }
        }
    }

    /**
     * Unloads the [Repository].
     */
    override fun unload() {
        resources.clear()
        _isLoaded = false
    }

    /**
     * Saves the [Repository].
     *
     * @throws Exception When an exception is thrown.
     */
    @Throws(Exception::class)
    override fun save() {
        val resourceSources = ArrayList<ResourceSource>()
        for (resource in this) {
            for (source in resource.sources) {
                resourceSources.add(source)
            }
        }

        saver.saveResources(subject, resourceSources)
    }

    /**
     * Loads the [ResourceRepository] asynchronously.
     *
     * @param callback Callback initializer.
     */
    override fun saveAsync(callback: Repository.SaveCallback.() -> Unit) {
        val saveCallback = Repository.SaveCallback()
        saveCallback.callback()

        val resourceSources = ArrayList<ResourceSource>()
        for (resource in this) {
            for (source in resource.sources) {
                resourceSources.add(source)
            }
        }

        saver.executor.execute {
            try {
                saver.saveResources(subject, resourceSources)

                saver.executor.execute {
                    saveCallback.onSaveCallback?.invoke()
                }
            }catch (e: Exception) {
                saver.executor.callbackExecutor.execute {
                    saveCallback.onErrorCallback?.invoke(e)
                }
            }
        }
    }

    /**
     * Returns an iterator over the [Resource] of the repository.
     *
     * @return An iterator over the [Resource] of the repository.
     */
    override fun iterator(): Iterator<Resource> {
        return resources.values.iterator()
    }

    private fun resourceSourceToResource(source: ResourceSource): Resource {
        //TODO: Improve the logic n stuff. Defensive stuff.
        val name = source.name
        try {
            val index1: Int
            val index2: Int
            val index3: Int
            val index4: Int

            index1 = name.indexOf('_')
            index2 = name.indexOf('_', index1 + 1)
            index3 = name.indexOf('_', index2 + 1)
            index4 = name.indexOf('.', index3 + 1)

            if (index1 == -1 || index2 == -1 || index3 == -1 || index4 == -1) {
                return Resource(name)
            }

            val id = name.substring(0, index1)
            val session = name.substring(index1 + 1, index2)
            val type = name.substring(index2 + 1, index3)
            val number = name.substring(index3 + 1, index4)

            return when (type) {
                "qp" -> QuestionPaper(name, Session.parse(session), Integer.parseInt(number))
                "ms" -> MarkingScheme(name, Session.parse(session))
                else -> Resource(name)
            }
        } catch (e: Exception) {
            // Fallback to a Resource instance if we fail to parse the getName.
            return Resource(name)
        }

    }
}

