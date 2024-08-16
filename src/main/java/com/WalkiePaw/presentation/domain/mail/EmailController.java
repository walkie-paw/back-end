package com.WalkiePaw.presentation.domain.mail;

import com.WalkiePaw.domain.mail.service.MailService;
import com.WalkiePaw.presentation.domain.mail.dto.request.EmailAuthRequest;
import com.WalkiePaw.presentation.domain.mail.dto.request.EmailSendRequest;
import com.WalkiePaw.presentation.domain.mail.dto.response.EmailAuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class EmailController {

    private final MailService mailService;

    @PostMapping("/send")
    @ResponseStatus(OK)
    public void mailSend(final @RequestBody @Validated  EmailSendRequest request) {
        mailService.joinEmail(request.email());
    }

    @PostMapping("/authCheck")
    @ResponseStatus(OK)
    public EmailAuthResponse AuthCheck(final @RequestBody @Validated EmailAuthRequest request) {
        return mailService.CheckAuthNum(request.email(), request.authNum());
    }
}
