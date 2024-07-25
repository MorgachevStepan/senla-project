package com.stepanew.senlaproject.exceptions;

import lombok.Getter;

@Getter
public class ParserException extends RuntimeException {

    @Getter
    public enum CODE {

        WRONG_FORMAT("Неверный формат файла"),

        WRONG_DATA_FORMAT("Неверный формат данных"),

        SOMETHING_WRONG("Что-то сломалось");

        final String codeDescription;

        CODE(String codeDescription) {
            this.codeDescription = codeDescription;
        }

        public ParserException get() {
            return new ParserException(this, this.codeDescription);
        }

        public ParserException get(String msg) {
            return new ParserException(this, this.codeDescription + " : " + msg);
        }

        public ParserException get(Throwable e) {
            return new ParserException(this, this.codeDescription + " : " + e.getMessage());
        }

        public ParserException get(Throwable e, String msg) {
            return new ParserException(this, e, this.codeDescription + " : " + msg);
        }
    }

    private final ParserException.CODE code;

    private ParserException(ParserException.CODE code, String msg) {
        this(code, null, msg);
    }

    private ParserException(ParserException.CODE code, Throwable e, String msg) {
        super(msg, e);
        this.code = code;
    }

}
