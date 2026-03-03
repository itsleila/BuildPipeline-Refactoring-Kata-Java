package org.sammancoaching;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;
 
public class Pipeline {
    private final Config config;
    private final Emailer emailer;
    private final Logger log;

    public Pipeline(Config config, Emailer emailer, Logger log) {
        this.config = config;
        this.emailer = emailer;
        this.log = log;
    }

    public void run(Project project) {
        if (!testPassed(project)) {
            notifyFailure("Tests failed");
            return;
        }

        if (!deploySuccessful(project)) {
            notifyFailure("Deployment failed");
            return;
        }

        notifySuccess();
    }

    private boolean hasTests(Project project) {
        return project.hasTests();
    }

    private boolean runTests(Project project) {
        return "success".equals(project.runTests());
    }

    private boolean testPassed(Project project) {
        boolean passed = runTests(project);

        if (!hasTests(project)) {
            log.info("No tests");
            return true;
        }

        if (passed) {
            log.info("Tests passed");
        } else {
            log.error("Tests failed");
        }
        return passed;
    }

    private boolean runDeployment(Project project) {
        return "success".equals(project.deploy());
    }

    private boolean deploySuccessful(Project project) {
        boolean success = runDeployment(project);
        if (success) {
            log.info("Deployment successful");
        } else {
            log.error("Deployment failed");
        }
        return success;
    }

    private boolean isEmailEnabled() {
        boolean enabled = config.sendEmailSummary();
        if (!enabled) {
            log.info("Email summary disabled");
        }
        return enabled;
    }

    private void notifyFailure(String reason) {
        log.error(reason);
        if (isEmailEnabled()) {
            emailer.send(reason);
        }
    }

    private void notifySuccess() {
        if (isEmailEnabled()) {
            emailer.send("Deployment completed successfully");
        }
    }

    
}
