package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;
 
public class Pipeline {
    private final Config config;
    private final Emailer emailer;
    private final Logger log;

    private static final String TestFailureMessage = "Tests failed";
    private static final String NoTestsMessage = "No tests";
    private static final String TestsPassedMessage = "Tests passed";
    private static final String DeploymentFailureMessage = "Deployment failed";
    private static final String DeploymentSuccessMessage = "Deployment successful";
    private static final String DeploymentCompletedMessage = "Deployment completed successfully";

    public Pipeline(Config config, Emailer emailer, Logger log) {
        this.config = config;
        this.emailer = emailer;
        this.log = log;
    }

    public void run(Project project) {
        if (!testPassed(project)) {
            sendEmail(TestFailureMessage);
            return;
        }

        if (!deploySuccessful(project)) {
            sendEmail(DeploymentFailureMessage);
            return;
        }

        sendEmail(DeploymentCompletedMessage);
    }

    private boolean hasTests(Project project) {
        return project.hasTests();
    }

    private boolean isSucessful(String result) {
        return "success".equals(result);
    }

    private boolean runTests(Project project) {
        return isSucessful(project.runTests());
    }

    private boolean runDeployment(Project project) {
        return isSucessful(project.deploy());
    }


    private boolean testPassed(Project project) {
        if (!hasTests(project)) {
            log.info(NoTestsMessage);
            return true;
        }

        boolean passed = runTests(project);
        if (passed) {
            log.info(TestsPassedMessage);
        } else {
            log.error(TestFailureMessage);
        }
        return passed;
    }


    private boolean deploySuccessful(Project project) {
        boolean success = runDeployment(project);
        if (success) {
            log.info(DeploymentSuccessMessage);
        } else {
            log.error(DeploymentFailureMessage);
        }
        return success;
    }

    private boolean isEmailEnabled() {
        boolean emailEnabled = config.sendEmailSummary();
        if (!emailEnabled) {
            log.info("Email summary disabled");
        }
        return emailEnabled;
    }

    private void sendEmail(String message) {
        if (isEmailEnabled()) {
            emailer.sendEmail(message);
        }
    }
    
}
