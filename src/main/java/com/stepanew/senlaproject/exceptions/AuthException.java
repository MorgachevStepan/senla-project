package com.stepanew.senlaproject.exceptions;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException{
    @Getter
    public enum CODE {
        NO_SUCH_EMAIL_OR_PASSWORD("Неправильный email или пароль"),
        JWT_VALIDATION_ERROR("Ошибка валидации JWT"),
        INVALID_REPEAT_PASSWORD("Пароли не совпадают"),
        EMAIL_IN_USE("Этот email уже используется");

        final String codeDescription;

        CODE(String codeDescription) {
            this.codeDescription = codeDescription;
        }

        public AuthException get() {
            return new AuthException(this, this.codeDescription);
        }

        public AuthException get(String msg) {
            return new AuthException(this, this.codeDescription + " : " + msg);
        }

        public AuthException get(Throwable e) {
            return new AuthException(this, this.codeDescription + " : " + e.getMessage());
        }

        public AuthException get(Throwable e, String msg) {
            return new AuthException(this, e, this.codeDescription + " : " + msg);
        }
    }

    private final AuthException.CODE code;

    private AuthException(AuthException.CODE code, String msg) {
        this(code, null, msg);
    }

    private AuthException(AuthException.CODE code, Throwable e, String msg) {
        super(msg, e);
        this.code = code;
    }

}
