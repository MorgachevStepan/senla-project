package com.stepanew.senlaproject.exceptions;

import lombok.Getter;

@Getter
public class CategoryException extends RuntimeException {

    @Getter
    public enum CODE {

        NO_SUCH_CATEGORY("Нет категории с таким id"),

        NAME_IN_USE("Категория с таким именем уже существует");

        final String codeDescription;

        CODE(String codeDescription) {
            this.codeDescription = codeDescription;
        }

        public CategoryException get() {
            return new CategoryException(this, this.codeDescription);
        }

        public CategoryException get(String msg) {
            return new CategoryException(this, this.codeDescription + " : " + msg);
        }

        public CategoryException get(Throwable e) {
            return new CategoryException(this, this.codeDescription + " : " + e.getMessage());
        }

        public CategoryException get(Throwable e, String msg) {
            return new CategoryException(this, e, this.codeDescription + " : " + msg);
        }
    }

    private final CategoryException.CODE code;

    private CategoryException(CategoryException.CODE code, String msg) {
        this(code, null, msg);
    }

    private CategoryException(CategoryException.CODE code, Throwable e, String msg) {
        super(msg, e);
        this.code = code;
    }

}
