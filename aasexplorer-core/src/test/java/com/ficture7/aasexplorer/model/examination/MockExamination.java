package com.ficture7.aasexplorer.model.examination;

import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;

public class MockExamination extends Examination {

    public MockExamination(Loader loader, Saver saver) {
        super(loader, saver);
    }

    @Override
    public String name() {
        return "mock";
    }
}
