package com.stepanew.senlaproject.exceptions;

import lombok.Getter;

@Getter
public class ChartException extends RuntimeException {

    @Getter
    public enum CODE {

        SOMETHING_WRONG("Что-то сломалось");

        final String codeDescription;

        CODE(String codeDescription) {
            this.codeDescription = codeDescription;
        }

        public ChartException get() {
            return new ChartException(this, this.codeDescription);
        }

        public ChartException get(String msg) {
            return new ChartException(this, this.codeDescription + " : " + msg);
        }

        public ChartException get(Throwable e) {
            return new ChartException(this, this.codeDescription + " : " + e.getMessage());
        }

        public ChartException get(Throwable e, String msg) {
            return new ChartException(this, e, this.codeDescription + " : " + msg);
        }
    }

    private final ChartException.CODE code;

    private ChartException(ChartException.CODE code, String msg) {
        this(code, null, msg);
    }

    private ChartException(ChartException.CODE code, Throwable e, String msg) {
        super(msg, e);
        this.code = code;
    }

}
