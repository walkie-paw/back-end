package com.WalkiePaw.presentation.domain.mail.dto.request;

import jakarta.validation.constraints.Email;

public record EmailSendRequest(@Email String email) {
}
