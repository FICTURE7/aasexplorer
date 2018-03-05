package com.ficture7.aasexplorer.client

import com.ficture7.aasexplorer.model.ResourceSource
import com.ficture7.aasexplorer.model.SubjectSource
import com.ficture7.aasexplorer.model.examination.Examination

/**
 * Represents a client which retrieves [SubjectSource] and [ResourceSource] from the internet (or somewhere).
 *
 * @author FICTURE7.
 */
interface Client {

    /**
     * Returns the name of the [Client].
     *
     * @return Name of the [Client].
     */
    val name: String

    /**
     * Retrieves the [SubjectSource]s of the specified [Examination] type.
     *
     * @param examinationClass [Examination] class.
     * @param <T>              Type of [Examination].
     * @return [SubjectSource]s of the specified [Examination] type; returns null if the specified examination is not supported.
     * @throws ParseException    Exception when parsing the data.
     * @throws DownloadException Exception when downloading the data.
     */
    @Throws(ParseException::class, DownloadException::class)
    fun <T : Examination> getSubjects(examinationClass: Class<T>): Iterable<SubjectSource>?

    /**
     * Retrieves the [ResourceSource]s of the specified [SubjectSource].
     *
     * @param subjectSource [SubjectSource] instance.
     * @return [ResourceSource]s of the specified [SubjectSource].
     * @throws ParseException    Exception when parsing the data.
     * @throws DownloadException Exception when downloading the data.
     */
    @Throws(ParseException::class, DownloadException::class)
    fun getResources(subjectSource: SubjectSource): Iterable<ResourceSource>?
}
