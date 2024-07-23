package com.stepanew.senlaproject.exceptions.message;

import java.util.Map;

public record ValidationErrorMessage(

        String code,

        String message,

        Map<String, String> errors

) {
}
