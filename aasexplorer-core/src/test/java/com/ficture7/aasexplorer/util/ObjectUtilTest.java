package com.ficture7.aasexplorer.util;

import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ObjectUtilTest {

    @Test
    public void ctor_forTheSake_of_100_Coverage() {
        new ObjectUtil();
    }

    @Test
    public void checkNotNull_nullValue_exception() {
        try {
            ObjectUtil.checkNotNull(null, "testParam");
        } catch (NullPointerException e) {
            assertTrue(e.getMessage().contains("testParam") && e.getMessage().contains("cannot be null"));
            return;
        }

        fail("Expected IllegalArgumentException but caught none.");
    }

    @Test
    public void checkNotNull_nonNullValue_returnsValue() {
        String test = "testValue";
        String ret = ObjectUtil.checkNotNull(test, "testValue");

        assertSame(test, ret);
    }

    @Test
    public void checkNotAbstract__abstractValue__throwsException() {
        try {
            ObjectUtil.checkNotAbstract(MockAbstract.class, "testParam");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("testParam") && e.getMessage().contains("cannot be an abstract"));
            return;
        }

        fail("Expected IllegalArgumentException but caught none.");
    }

    @Test
    public void checkNotAbstract__nonAbstractValue__returnsValue() {
        Class test = MockNonAbstract.class;
        Class ret = ObjectUtil.checkNotAbstract(test, "testValue");

        assertSame(test, ret);
    }

    @Test
    public void checkNotInterface__interfaceValue__throwsException() {
        try {
            ObjectUtil.checkNotInterface(MockInterface.class, "testParam");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("testParam") && e.getMessage().contains("cannot be an interface"));
            return;
        }

        fail("Expected IllegalArgumentException but caught none.");
    }

    @Test
    public void checkNotInterface__nonInterfaceValue__returnsValue() {
        Class test = MockNonAbstract.class;
        Class ret = ObjectUtil.checkNotInterface(test, "testValue");

        assertSame(test, ret);
    }

    private interface MockInterface {

    }

    private class MockNonAbstract {

    }

    private static abstract class MockAbstract {

    }
}