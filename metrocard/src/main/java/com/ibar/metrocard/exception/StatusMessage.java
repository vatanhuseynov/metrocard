package com.ibar.metrocard.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusMessage {
    USERNAME_OR_PASSWORD_WRONG("Istifadəçi adı və ya parolu yanlışdır"),
    NOT_FOUND("Tapılmadı"),
    BALANCE_EXCEEDED("Balans nəzərdə tutulandan çox ola bilməz"),
    NOT_ENOUGH("Balans kifayət qədər olmadığı üçün artıra bilməzsiniz"),
    ALREADY_EXISTS("Artıq mövcuddur"),
    FORBIDDEN("İcazə yoxdur"),
    OK("Uğurla icra edildi"),
    CREATED("Uğurla yaradıldı"),;
    private final String message;
}
