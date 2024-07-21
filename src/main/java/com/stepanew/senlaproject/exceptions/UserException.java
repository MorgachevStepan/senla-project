package com.stepanew.senlaproject.exceptions;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    @Getter
    public enum CODE {

        NO_SUCH_USER("Нет пользователя с таким id");

        final String codeDescription;

        CODE(String codeDescription) {
            this.codeDescription = codeDescription;
        }

        public UserException get() {
            return new UserException(this, this.codeDescription);
        }

        public UserException get(String msg) {
            return new UserException(this, this.codeDescription + " : " + msg);
        }

        public UserException get(Throwable e) {
            return new UserException(this, this.codeDescription + " : " + e.getMessage());
        }

        public UserException get(Throwable e, String msg) {
            return new UserException(this, e, this.codeDescription + " : " + msg);
        }
    }

    protected final UserException.CODE code;

    protected UserException(UserException.CODE code, String msg) {
        this(code, null, msg);
    }

    protected UserException(UserException.CODE code, Throwable e, String msg) {
        super(msg, e);
        this.code = code;
    }

}