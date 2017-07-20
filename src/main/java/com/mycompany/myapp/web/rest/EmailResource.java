package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Task;
import com.mycompany.myapp.repository.TaskRepository;
import com.mycompany.myapp.service.MailService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchProviderException;
import java.util.Optional;

/**
 * Created by william on 7/18/17.
 */

@RestController
@RequestMapping("/api")
public class EmailResource {

    private final Logger log = LoggerFactory.getLogger(EmailResource.class);

    private final MailService mailService;

    private final TaskRepository taskRepository;

    @Autowired
    @Lazy
    public EmailResource(MailService mailService, TaskRepository taskRepository) {
        this.mailService = mailService;
        this.taskRepository = taskRepository;
    }

    @PostMapping("/email")
    @Timed
    public ResponseEntity<Void> sendEmail(@RequestBody final Task task) {
        if (task.isSendEmail()) {
            log.debug("REST request to send email to User: {}", task.getUser().getLogin());
            final String assunto = String.format("Tarefa: %s", task.getTitle());
            final String conteudo = String.format("Descrição: %s", task.getDescription());
            mailService
                .sendEmail(task.getUser().getEmail(), assunto, conteudo, false, false);
            return ResponseEntity
                .ok()
                .headers(
                    HeaderUtil
                        .createAlert("tasksApp.task.emailSent.success", task.getUser().getLogin())).build();
        }
        return ResponseEntity
            .notFound()
            .headers(HeaderUtil.createAlert("tasksApp.task.emailSent.fail", task.getTitle())).build();

    }

}
