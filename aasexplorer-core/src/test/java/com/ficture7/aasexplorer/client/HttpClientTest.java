package com.ficture7.aasexplorer.client;

import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;

import org.jsoup.nodes.Document;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertNotNull;

public class HttpClientTest {

    @Test(expected = NullPointerException.class)
    public void get_nullUri_exception() throws DownloadException {
        MockHttpClient client = new MockHttpClient();
        client.get(null);
    }

    @Test(expected = DownloadException.class)
    public void get__failed_to_download__throwsException() throws DownloadException {
        MockHttpClient client = new MockHttpClient();
        client.get(URI.create("http://somedomainwhichshouldnotexistsfortestingpurposes.com/"));
    }

    @Test
    public void get__returns_document_instance() throws DownloadException {
        MockHttpClient client = new MockHttpClient();
        Document document = client.get(URI.create("http://google.com"));

        assertNotNull(document);
    }

    @Test
    public void Processor__for_100_coverage_sake() {
        new HttpClient.Processor() {
            @Override
            public void process(String name, String url) {

            }
        };
    }

    private static class MockHttpClient extends HttpClient {
        @Override
        public <T extends Examination> Iterable<SubjectSource> getSubjects(Class<T> examinationClass) throws ParseException, DownloadException {
            return null;
        }

        @Override
        public Iterable<ResourceSource> getResources(SubjectSource subjectSource) {
            return null;
        }

        @Override
        protected void parse(Document document, Processor processor) throws ParseException {

        }
    }
}