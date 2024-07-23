package com.stepanew.senlaproject.exceptions;

import lombok.Getter;

@Getter
public class StoreException extends RuntimeException {

    @Getter
    public enum CODE {

        NO_SUCH_STORE("Нет магазина с таким id");

        final String codeDescription;

        CODE(String codeDescription) {
            this.codeDescription = codeDescription;
        }

        public StoreException get() {
            return new StoreException(this, this.codeDescription);
        }

        public StoreException get(String msg) {
            return new StoreException(this, this.codeDescription + " : " + msg);
        }

        public StoreException get(Throwable e) {
            return new StoreException(this, this.codeDescription + " : " + e.getMessage());
        }

        public StoreException get(Throwable e, String msg) {
            return new StoreException(this, e, this.codeDescription + " : " + msg);
        }
    }

    private final StoreException.CODE code;

    private StoreException(StoreException.CODE code, String msg) {
        this(code, null, msg);
    }

    private StoreException(StoreException.CODE code, Throwable e, String msg) {
        super(msg, e);
        this.code = code;
    }

}
