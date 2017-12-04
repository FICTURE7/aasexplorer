package com.ficture7.aasexplorer.client;

import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.ALevelExamination;
import com.ficture7.aasexplorer.model.examination.MockExamination;

import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.Date;

import static org.junit.Assert.*;

public class XtremePapersClientTest {

    private XtremePapersClient client;

    @Before
    public void setUp() {
        client = new XtremePapersClient();
    }

    @Test(expected = NullPointerException.class)
    public void getSubjects__null_examinationClass__throwsException() throws ParseException, DownloadException {
        client.getSubjects(null);
    }

    @Test
    public void getSubjects__ALevelExamination_examinationClass__returns_subjects() throws ParseException, DownloadException {
        Iterable<SubjectSource> sources = client.getSubjects(ALevelExamination.class);

        assertNotNull(sources);
    }

    @Test
    public void getSubjects__unknown_examinationClass__returns_null() throws ParseException, DownloadException {
        Iterable<SubjectSource> sources = client.getSubjects(MockExamination.class);

        assertNull(sources);
    }


    @Test(expected = NullPointerException.class)
    public void getResources__null_subjectSource__throwsException() throws ParseException, DownloadException {
        client.getResources(null);
    }

    @Test
    public void getResources__returns_resources() throws ParseException, DownloadException {
        SubjectSource source = new SubjectSource(client,
                                                 9702,
                                                 "Physics",
                                                 new Date(),
                                                 URI.create("http://papers.xtremepapers.com/CIE/Cambridge%20International%20A%20and%20AS%20Level/Physics%20%289702%29/"));
        Iterable<ResourceSource> sources = client.getResources(source);

        assertNotNull(sources);
    }

    @Test(expected = ParseException.class)
    public void parseTable__cannot_parse__throwsException() throws DownloadException, ParseException {
        client.parse(client.get(URI.create("http://google.com/")), null);
    }

    @Test
    public void parseTable__passes_parsed_values_to_Processor() throws DownloadException, ParseException {
        client.parse(client.get(XtremePapersClient.ALEVEL_ROOT_URI), new HttpClient.Processor() {
            @Override
            public void process(String name, String url) {
                assertNotNull(name);
                assertNotNull(url);
                try {
                    new URI(url);
                } catch (Exception e) {
                    fail();
                }
            }
        });
    }
}