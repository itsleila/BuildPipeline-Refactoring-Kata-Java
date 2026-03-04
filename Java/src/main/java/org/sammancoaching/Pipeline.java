package org.sammancoaching;

import org.sammancoaching.dependencies.*;
import org.sammancoaching.service.DeploymentService;
import org.sammancoaching.service.NotificationService;
import org.sammancoaching.service.TestService;

public class Pipeline {
    private final DeploymentService deploymentService;
    private final TestService testService;
    private final NotificationService notification;

    public static final String DeploymentCompletedMessage = "Deployment completed successfully";
    

    public Pipeline(Config config, Emailer emailer, Logger log) {
        this.testService = new TestService(log);
        this.deploymentService = new DeploymentService(log);
        this.notification = new NotificationService(config, emailer, log);
    }

    public void run(Project project) {

        if (!testService.runUnitTests(project)) {
            notification.notify(TestService.TestFailureMessage);
            return;
        }

        if (!deploymentService.deployToStaging(project)) {
            notification.notify(DeploymentService.DeploymentFailureToStagingMessage);
            return;
        }

        TestStatus smokeStatus = testService.runSmokeTests(project);

        if (smokeStatus == TestStatus.NO_TESTS) {
            notification.notify(TestService.NoSmokeTestsMessage);
            return;
        }

        if (smokeStatus == TestStatus.FAILING_TESTS) {
            notification.notify(TestService.SmokeTestFailureMessage);
            return;
        }

        if (!deploymentService.deployToProduction(project)) {
            notification.notify(DeploymentService.DeploymentFailureMessage);
            return;
        }
        
        notification.notify(DeploymentCompletedMessage);
    }
    
}
