package org.sammancoaching.service;

import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Logger;

public class NotificationService {

    private final Config config;
    private final Emailer emailer;
    private final Logger log;

    public NotificationService(Config config, Emailer emailer, Logger log) {
        this.config = config;
        this.emailer = emailer;
        this.log = log;
    }

    public void notify(String message) {
        if (config.sendEmailSummary()) {
            emailer.sendEmail(message);
        } else {
            log.info("Email summary disabled");
        }
    }
}