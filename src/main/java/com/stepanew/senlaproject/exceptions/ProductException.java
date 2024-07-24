package com.stepanew.senlaproject.exceptions;

import lombok.Getter;

@Getter
public class ProductException extends RuntimeException {

    @Getter
    public enum CODE {

        NO_SUCH_PRODUCT("Нет продукта с таким id"),

        NAME_IN_USE("Продукт с таким именем уже существует"),

        TEA_POT("Не трогай меня, я чайник!");

        final String codeDescription;

        CODE(String codeDescription) {
            this.codeDescription = codeDescription;
        }

        public ProductException get() {
            return new ProductException(this, this.codeDescription);
        }

        public ProductException get(String msg) {
            return new ProductException(this, this.codeDescription + " : " + msg);
        }

        public ProductException get(Throwable e) {
            return new ProductException(this, this.codeDescription + " : " + e.getMessage());
        }

        public ProductException get(Throwable e, String msg) {
            return new ProductException(this, e, this.codeDescription + " : " + msg);
        }
    }

    private final ProductException.CODE code;

    private ProductException(ProductException.CODE code, String msg) {
        this(code, null, msg);
    }

    private ProductException(ProductException.CODE code, Throwable e, String msg) {
        super(msg, e);
        this.code = code;
    }

}