package org.sammancoaching.service;

import org.sammancoaching.dependencies.DeploymentEnvironment;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

import static org.sammancoaching.Pipeline.*;


public class DeploymentService {
    private final Logger log;

    public static final String DeploymentFailureToStagingMessage = "Deployment to staging failed";
    public static final String DeploymentFailureMessage = "Deployment failed";
    public static final String DeploymentSuccessToStagingMessage = "Deployment to staging successful";
    public static final String DeploymentSuccessMessage = "Deployment successful";

    public DeploymentService(Logger log) {
        this.log = log;
    }

    public boolean deployToStaging(Project project) {
        boolean success = "success".equals(project.deploy(DeploymentEnvironment.STAGING));

        if (success) {
            log.info(DeploymentSuccessToStagingMessage);
        } else {
            log.error(DeploymentFailureToStagingMessage);
        }

        return success;
    }

    public boolean deployToProduction(Project project) {
        boolean success = "success".equals(project.deploy(DeploymentEnvironment.PRODUCTION));

        if (success) {
            log.info(DeploymentSuccessMessage);
        } else {
            log.error(DeploymentFailureMessage);
        }

        return success;
    }
}