package com.commerce.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidOperationException extends Exception {


    public InvalidOperationException() {
        super("Operação invalida, operações conhecidas: 'adicionar'  e 'remover'");
    }

}
