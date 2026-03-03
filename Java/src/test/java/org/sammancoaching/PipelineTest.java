package org.sammancoaching;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sammancoaching.dependencies.Config;
import org.sammancoaching.dependencies.Emailer;
import org.sammancoaching.dependencies.Logger;
import org.sammancoaching.dependencies.Project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.sammancoaching.dependencies.TestStatus.*;

class PipelineTest {

    @Test
    @DisplayName("Deve enviar email de sucesso quando testes e deploy passarem")
    void EmailSucessoTest(){
        Config config = mock(Config.class);
        when(config.sendEmailSummary()).thenReturn(true);
        Emailer emailer = mock(Emailer.class);
        Logger log = mock(Logger.class);

        Pipeline pipeline = new Pipeline(config, emailer, log);
        Project project = Project.builder()
            .setTestStatus(PASSING_TESTS)
            .setDeploysSuccessfully(true)
            .build();
        pipeline.run(project);

        verify(emailer).sendEmail("Deployment completed successfully");
    }

    @Test
    @DisplayName("Deve enviar email de falha quando os testes falharem")
    void EmailTestesFalhaTest(){
        Config config = mock(Config.class);
        when(config.sendEmailSummary()).thenReturn(true);
        Emailer emailer = mock(Emailer.class);
        Logger log = mock(Logger.class);

        Pipeline pipeline = new Pipeline(config, emailer, log);
        Project project = Project.builder()
            .setTestStatus(FAILING_TESTS)
            .setDeploysSuccessfully(false)
            .build();
        pipeline.run(project);

        verify(emailer).sendEmail("Tests failed");
    }

    @Test
    @DisplayName("Deve enviar email de falha quando o deploy falhar")
    void EmailDeployFalhaTest(){
        Config config = mock(Config.class);
        when(config.sendEmailSummary()).thenReturn(true);
        Emailer emailer = mock(Emailer.class);
        Logger log = mock(Logger.class);

        Pipeline pipeline = new Pipeline(config, emailer, log);
        Project project = Project.builder()
            .setTestStatus(PASSING_TESTS)
            .setDeploysSuccessfully(false)
            .build();
        pipeline.run(project);

        verify(emailer).sendEmail("Deployment failed");
    }

    @Test
    @DisplayName("Deve fazer o deploy mesmo sem testes")
    void DeploySemTestesTest(){
        Config config = mock(Config.class);
        when(config.sendEmailSummary()).thenReturn(true);
        Emailer emailer = mock(Emailer.class);
        Logger log = mock(Logger.class);

        Pipeline pipeline = new Pipeline(config, emailer, log);
        Project project = Project.builder()
            .setTestStatus(NO_TESTS)
            .setDeploysSuccessfully(true)
            .build();
        pipeline.run(project);
        verify(log).info("Deployment successful");
    }

    @Test
    @DisplayName("Não deve enviar email quando o email estiver desabilitada")
    void NaoEnviaEmailQuandoDesabilitadaTest(){
        Config config = mock(Config.class);
        when(config.sendEmailSummary()).thenReturn(false);
        Emailer emailer = mock(Emailer.class);
        Logger log = mock(Logger.class);

        Pipeline pipeline = new Pipeline(config, emailer, log);
        Project project = Project.builder()
            .setTestStatus(PASSING_TESTS)
            .setDeploysSuccessfully(true)
            .build();
        pipeline.run(project);

        verify(emailer, never()).sendEmail(any(String.class));
    }
    }