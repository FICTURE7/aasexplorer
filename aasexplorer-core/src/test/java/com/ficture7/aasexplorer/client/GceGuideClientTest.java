package com.ficture7.aasexplorer.client;

import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;
import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.ALevelExamination;
import com.ficture7.aasexplorer.model.examination.Examination;
import com.ficture7.aasexplorer.model.examination.OLevelExamination;

import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class GceGuideClientTest {

    private GceGuideClient client;

    @Before
    public void setUp() {
        client = new GceGuideClient();
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
    public void getSubjects__OLevelExamination_examinationClass__returns_subjects() throws ParseException, DownloadException {
        Iterable<SubjectSource> sources = client.getSubjects(OLevelExamination.class);

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
        SubjectSource source = new SubjectSource(mock(Client.class),
                                                 9702,
                                                 "Physics",
                                                 new Date(),
                                                 URI.create("http://papers.gceguide.xyz/A%20Levels/Physics%20%289702%29/"));
        Iterable<ResourceSource> sources = client.getResources(source);

        assertNotNull(sources);
    }

    @Test(expected = ParseException.class)
    public void parseTable__cannot_parse__throwsException() throws DownloadException, ParseException {
        client.parse(client.get(URI.create("http://google.com/")), null);
    }

    @Test
    public void parseTable__passes_parsed_values_to_TableProcessor() throws DownloadException, ParseException {
        client.parse(client.get(GceGuideClient.ALEVEL_ROOT_URI), new HttpClient.Processor() {
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

    private static class MockExamination extends Examination {
        protected MockExamination(Loader loader, Saver saver) {
            super(loader, saver);
        }

        @Override
        public String name() {
            return "mock";
        }
    }
}