package com.ficture7.aasexplorer.client;

import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.ALevelExamination;
import com.ficture7.aasexplorer.model.examination.Examination;
import com.ficture7.aasexplorer.util.SubjectUtil;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * A <a href="http://www.xtremepapers.com">www.xtremepapers.com</a> implementation of {@link Client}.
 *
 * @author FICTURE7
 */
public class XtremePapersClient extends HttpClient {

    static final URI ALEVEL_ROOT_URI = URI.create("http://papers.xtremepapers.com/CIE/Cambridge%20International%20A%20and%20AS%20Level/");

    /**
     * Retrieves the {@link SubjectSource}s of the specified {@link Examination} type.
     *
     * @param examinationClass {@link Examination} class.
     * @param <T>              Type of {@link Examination}.
     * @return {@link SubjectSource}s of the specified {@link Examination} type; returns null if the specified examination is not supported.
     * @throws NullPointerException {@code examinationClass} is null.
     * @throws ParseException       Exception when parsing the data.
     * @throws DownloadException    Exception when downloading the data.
     */
    @Override
    public <T extends Examination> Iterable<SubjectSource> getSubjects(Class<T> examinationClass) throws ParseException, DownloadException {
        checkNotNull(examinationClass, "examinationClass");

        // Figure out which document to download and
        // download it using HTTP GET
        URI root;
        if (examinationClass == ALevelExamination.class) {
            root = ALEVEL_ROOT_URI;
        } else {
            return null;
        }

        Date date = new Date();
        List<SubjectSource> sources = new ArrayList<>(16);
        // Download the HTML document.
        Document document = get(root);

        // Parse the HTML document.
        parse(document,  new SubjectsDocumentProcessor(root, sources, date));
        return sources;
    }

    /**
     * Retrieves the {@link ResourceSource}s of the specified {@link SubjectSource}.
     *
     * @param subjectSource {@link SubjectSource} instance.
     * @return {@link ResourceSource}s of the specified {@link SubjectSource}.
     * @throws NullPointerException {@code subjectSource} is null.
     * @throws ParseException       Exception when parsing the data.
     * @throws DownloadException    Exception when downloading the data.
     */
    @Override
    public Iterable<ResourceSource> getResources(SubjectSource subjectSource) throws ParseException, DownloadException {
        checkNotNull(subjectSource, "subjectSource");

        Date date = new Date();
        List<ResourceSource> sources = new ArrayList<>(16);
        // Download the HTML document using HTTP GET request.
        Document document = get(subjectSource.uri());

        // Parse the HTML document to retrieve the resources.
        parse(document, new ResourcesDocumentProcessor(subjectSource, sources, date));
        return sources;
    }

    /**
     * Parses the specified {@link Document} instance and uses the specified {@link Processor}
     * to process parsed data from the {@link Document} instance.
     *
     * @param document  {@link Document} instance.
     * @param processor {@link Processor} instance.
     * @throws ParseException Exception caught when parsing the {@link Document}.
     */
    @Override
    protected void parse(Document document, Processor processor) throws ParseException {
        try {
            Element table = document.getElementsByClass("autoindex_table").first(); // <table/>
            Element tableBody = table.child(0); // <tbody/>

            // i = 2, to skip the first two element which is the header and parent directory.
            // size - 1 to skip last element.
            for (int i = 2; i < tableBody.children().size() - 1; i++) {
                Element row = tableBody.children().get(i); // <tr/>
                Element data = row.child(0); // <td/>
                Element link = data.child(0); // <a/>

                String name = link.text();
                String url = link.attr("href");

                processor.process(name, url);
            }
        } catch (Exception e) {
            throw new ParseException(e);
        }
    }

    //TODO: Make SubjectsDocumentProcessor and ResourcesDocumentProcessor generic.
    /**
     * Processes the parsed data for {@link SubjectSource} instances.
     */
    private class SubjectsDocumentProcessor extends Processor {

        public SubjectsDocumentProcessor(URI root, List<SubjectSource> sources, Date date) {
            this.root = root;
            this.sources = sources;
            this.date = date;
        }

        private URI root;
        private List<SubjectSource> sources;
        private Date date;

        @Override
        public void process(String fullName, String url) {
            URI uri;
            String name;
            int id;

            try {
                uri = root.resolve(url + "/");
                name = SubjectUtil.parseName(fullName);
                id = SubjectUtil.parseId(fullName);
            } catch (Exception e) {
                return;
            }

            SubjectSource s = new SubjectSource(XtremePapersClient.this, id, name, date, uri);
            sources.add(s);
        }
    }

    /**
     * Processes the parsed data for {@link ResourceSource} instances.
     */
    private class ResourcesDocumentProcessor extends Processor {

        public ResourcesDocumentProcessor(SubjectSource subjectSource, List<ResourceSource> sources, Date date) {
            this.subjectSource = subjectSource;
            this.sources = sources;
            this.date = date;
        }

        private SubjectSource subjectSource;
        private List<ResourceSource> sources;
        private Date date;

        @Override
        public void process(String name, String url) {
            URI uri = subjectSource.uri().resolve(url.replace(" ", "%20"));

            ResourceSource s = new ResourceSource(XtremePapersClient.this, name, date, uri);
            sources.add(s);
        }
    }
}
