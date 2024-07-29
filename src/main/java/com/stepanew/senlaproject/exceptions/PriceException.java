package com.stepanew.senlaproject.exceptions;

import lombok.Getter;

@Getter
public class PriceException extends RuntimeException {

    @Getter
    public enum CODE {

        EMPTY_DATA("Id товаров не должны быть пустыми"),

        NO_SUCH_PRICE("На этот товар еще не добавили цену в данном магазине");

        final String codeDescription;

        CODE(String codeDescription) {
            this.codeDescription = codeDescription;
        }

        public PriceException get() {
            return new PriceException(this, this.codeDescription);
        }

        public PriceException get(String msg) {
            return new PriceException(this, this.codeDescription + " : " + msg);
        }

        public PriceException get(Throwable e) {
            return new PriceException(this, this.codeDescription + " : " + e.getMessage());
        }

        public PriceException get(Throwable e, String msg) {
            return new PriceException(this, e, this.codeDescription + " : " + msg);
        }
    }

    private final PriceException.CODE code;

    private PriceException(PriceException.CODE code, String msg) {
        this(code, null, msg);
    }

    private PriceException(PriceException.CODE code, Throwable e, String msg) {
        super(msg, e);
        this.code = code;
    }

}
