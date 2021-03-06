package com.ficture7.aasexplorer.client

import com.ficture7.aasexplorer.model.ResourceSource
import com.ficture7.aasexplorer.model.SubjectSource
import com.ficture7.aasexplorer.model.examination.ALevelExamination
import com.ficture7.aasexplorer.model.examination.Examination
import com.ficture7.aasexplorer.model.examination.OLevelExamination
import com.ficture7.aasexplorer.util.SubjectUtil

import org.jsoup.nodes.Document

import java.net.URI
import java.util.ArrayList
import java.util.Date

/**
 * A [www.gceguides.xyz](http://www.gceguides.xyz) implementation of [Client].
 *
 * @author FICTURE7
 */
class GceGuideClient : HttpClient() {

    //TODO: Merge table parsing logic into a single method or something.

    /**
     * Returns the name of the [Client].
     *
     * @return Name of the [Client].
     */
    override val name: String
        get() = "gceguide.xyz"

    /**
     * Retrieves the [SubjectSource]s of the specified [Examination] type.
     *
     * @param examinationClass [Examination] class.
     * @param <T>              Type of [Examination].
     * @return [SubjectSource]s of the specified [Examination] type; returns null if the specified examination is not supported.
     * @throws NullPointerException `examinationClass` is null.
     * @throws ParseException       Exception when parsing the data.
     * @throws DownloadException    Exception when downloading the data.
    </T> */
    @Throws(ParseException::class, DownloadException::class)
    override fun <T : Examination> getSubjects(examinationClass: Class<T>): Iterable<SubjectSource>? {
        // Figure out which document to download and
        // download it using HTTP GET
        val root: URI = when (examinationClass) {
            ALevelExamination::class.java -> ALEVEL_ROOT_URI
            OLevelExamination::class.java -> OLEVEL_ROOT_URI
            else -> // Return null to let them know we don't support the examination type.
                return null
        }

        val date = Date()
        val sources = ArrayList<SubjectSource>(16)
        // Download the HTML document.
        val document = get(root)

        // Parse the HTML document.
        parse(document, SubjectsDocumentProcessor(root, sources, date))
        return sources
    }

    /**
     * Retrieves the [ResourceSource]s of the specified [SubjectSource].
     *
     * @param subjectSource [SubjectSource] instance.
     * @return [ResourceSource]s of the specified [SubjectSource].
     * @throws NullPointerException `subjectSource` is null.
     * @throws ParseException       Exception when parsing the data.
     * @throws DownloadException    Exception when downloading the data.
     */
    @Throws(ParseException::class, DownloadException::class)
    override fun getResources(subjectSource: SubjectSource): Iterable<ResourceSource>? {
        val date = Date()
        val sources = ArrayList<ResourceSource>(16)
        // Download the HTML document using HTTP GET request.
        val document = get(subjectSource.URI)

        // Parse the HTML document to retrieve the resources.
        parse(document, ResourcesDocumentProcessor(subjectSource, sources, date))
        return sources
    }

    /**
     * Parses the specified [Document] instance and uses the specified [HttpClient.Processor]
     * to process parsed data from the [Document] instance.
     *
     * @param document [Document] instance.
     * @param processor [HttpClient.Processor] instance.
     * @throws ParseException Exception caught when parsing the [Document].
     */
    @Throws(ParseException::class)
    override fun parse(document: Document, processor: HttpClient.Processor) {
        try {
            // Get the main table.
            val table = document.body().getElementsByClass("sortable").first() // <table/>
            val tableBody = table.child(1) // <tbody/>

            // Iterable through the table's rows.
            for (row in tableBody.children()) { // <tr/>
                val data = row.child(0) // <td/>
                val link = data.child(0) // <a/>

                val name = link.text()
                val url = link.attr("href")

                // Pass control the Processor instance.
                processor.process(name, url)
            }
        } catch (e: Exception) {
            throw ParseException(e)
        }
    }

    /**
     * Processes the parsed data for [SubjectSource] instances.
     */
    private inner class SubjectsDocumentProcessor(private val root: URI, private val sources: MutableList<SubjectSource>, private val date: Date) : HttpClient.Processor() {

        override fun process(fullName: String, url: String) {
            val uri: URI
            val name: String
            val id: Int

            try {
                uri = root.resolve(url + "/")
                name = SubjectUtil.parseName(fullName)
                id = SubjectUtil.parseId(fullName)
            } catch (e: Exception) {
                return
            }

            val s = SubjectSource(this@GceGuideClient, id, name, date, uri)
            sources.add(s)
        }
    }

    /**
     * Processes the parsed data for [ResourceSource] instances.
     */
    private inner class ResourcesDocumentProcessor(private val subjectSource: SubjectSource, private val sources: MutableList<ResourceSource>, private val date: Date) : HttpClient.Processor() {

        override fun process(name: String, url: String) {
            val uri = subjectSource.URI.resolve(url)

            val s = ResourceSource(this@GceGuideClient, name, date, uri)
            sources.add(s)
        }
    }

    companion object {
        /**
         * A-Level root URL.
         */
        @JvmField
        val ALEVEL_ROOT_URI = URI.create("http://papers.gceguide.xyz/A%20Levels/")!!

        /**
         * O-Level root URL.
         */
        @JvmField
        val OLEVEL_ROOT_URI = URI.create("https://papers.gceguide.xyz/O%20Levels/")!!
    }
}
