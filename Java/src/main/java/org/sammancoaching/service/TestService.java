package org.sammancoaching.service;

import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;
import org.sammancoaching.dependencies.TestStatus;

public class TestService {
    private final Logger log;

    public static final String TestFailureMessage = "Tests failed";
    public static final String NoTestsMessage = "No tests";
    public static final String TestsPassedMessage = "Tests passed";
    public static final String SmokeTestsPassedMessage = "Smoke tests passed";
    public static final String SmokeTestFailureMessage = "Smoke tests failed";
    public static final String NoSmokeTestsMessage = "Pipeline failed - no smoke tests";

    public TestService(Logger log) {
        this.log = log;
    }

    public boolean runUnitTests(Project project) {
        if (!project.hasTests()) {
            log.info(NoTestsMessage);
            return true;
        }

        boolean success = "success".equals(project.runTests());

        if (success) {
            log.info(TestsPassedMessage);
        } else {
            log.error(TestFailureMessage);
        }

        return success;
    }

    public TestStatus runSmokeTests(Project project) {
        TestStatus status = project.runSmokeTests();

        if (status == TestStatus.NO_TESTS) {
            log.error(NoSmokeTestsMessage);
        }
        if (status == TestStatus.FAILING_TESTS) {
            log.error(SmokeTestFailureMessage);
        }
        if (status == TestStatus.PASSING_TESTS) {
            log.info(SmokeTestsPassedMessage);
        }

        return status;
    }
}