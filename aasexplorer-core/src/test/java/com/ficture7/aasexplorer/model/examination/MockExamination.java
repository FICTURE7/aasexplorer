package com.ficture7.aasexplorer.model.examination;

import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;

import org.jetbrains.annotations.NotNull;

public class MockExamination extends Examination {

    public MockExamination(Loader loader, Saver saver) {
        super(loader, saver);
    }

    @NotNull
    @Override
    public String getName() {
        return "mock";
    }
}
