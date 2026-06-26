package com.digitalMarket.iamService.infraestructure.exception;

import com.digitalMarket.iamService.infraestructure.exceptions.IdNotFoundException;
import com.digitalMarket.iamService.infraestructure.exceptions.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestController {

    @ExceptionHandler({UsernameNotFoundException.class, IllegalArgumentException.class})
    public BaseErrorResponse handleIlegalArgument(RuntimeException exception) {
        return ErrorsResponse.builder()
                .message(Collections.singletonList(exception.getMessage()))
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }


    @ExceptionHandler(IdNotFoundException.class)
    public BaseErrorResponse handleIdNotFound(RuntimeException exception) {
        return ErrorsResponse.builder()
                .message(Collections.singletonList(exception.getMessage()))
                .status(HttpStatus.NOT_FOUND.name())
                .code(HttpStatus.NOT_FOUND.value())
                .build();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseErrorResponse handleIdNotFound(MethodArgumentNotValidException exception) {
        var errors = new ArrayList<String>();
        exception.getAllErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));
        return ErrorsResponse.builder()
                .message(errors)
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public BaseErrorResponse handleDuplicateEntry(DataIntegrityViolationException exception) {
            return ErrorsResponse.builder()
                .message(Collections.singletonList(extractConstraintMessage("handleDuplicateEntry: "+ exception.getMessage())))
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler(TransactionSystemException.class)
    public BaseErrorResponse handleTransactionException(TransactionSystemException exception) {
        return ErrorsResponse.builder()
                .message(Collections.singletonList("handleTransactionException: "+ exception.getMessage()))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }

    @ExceptionHandler(JpaSystemException.class)
    public BaseErrorResponse handleJpaException(JpaSystemException exception) {
        return ErrorsResponse.builder()
                .message(Collections.singletonList("handleJpaException: "+ exception.getMessage()))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
    }

    private String extractConstraintMessage(String originalMessage) {
        Pattern pattern = Pattern.compile("Detail: Key (.*?\\.)");
        Matcher matcher = pattern.matcher(originalMessage);
        if (matcher.find()) {
            String msg = matcher.group(1).replace(")=(", " ").trim();
            return msg.replace("already exists", "ya existe")
                    .replace("(","")
                    .replace(")","");
        }
        return matcher.group(1);
    }
}