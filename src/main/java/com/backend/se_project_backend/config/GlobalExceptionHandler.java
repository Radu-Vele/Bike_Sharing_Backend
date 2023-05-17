package com.backend.se_project_backend.config;

import com.backend.se_project_backend.utils.exceptions.DocumentNotFoundException;
import com.backend.se_project_backend.utils.exceptions.IllegalOperationException;
import com.backend.se_project_backend.utils.exceptions.UniqueDBFieldException;
import com.backend.se_project_backend.utils.exceptions.UserAlreadyRegisteredException;
import com.mongodb.MongoWriteException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.mail.MessagingException;
import javax.xml.bind.ValidationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static com.backend.se_project_backend.utils.StringConstants.EX_MAIL_NOT_SENT;

/**
 * Handles all exceptions in the program
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Catch exception raised by the validation mechanisms
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException (
            UsernameNotFoundException ex, WebRequest request) {
        String bodyOfResponse = "There is no user associated with the given token";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<Object> handleUserAlreadyRegisteredException (
                                                                   UserAlreadyRegisteredException ex, WebRequest request) {
        String bodyOfResponse = "Username or email address already in use.";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(MongoWriteException.class)
    public ResponseEntity<Object> handleMongoWriteException (
            MongoWriteException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    /**
     * caught when one tries to insert data that needs to be unique and is already present
     */
    @ExceptionHandler(UniqueDBFieldException.class)
    public ResponseEntity<Object> handleUniqueDBFieldException (
            UniqueDBFieldException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<Object> handleDocumentNotFoundException (
            DocumentNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
    @ExceptionHandler(IllegalOperationException.class)
    public ResponseEntity<Object> handleDocumentNotFoundException (
            IllegalOperationException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(io.jsonwebtoken.SignatureException.class)
    public ResponseEntity<Object> handleDocumentNotFoundException (
            SignatureException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<Object> handleMessagingException (MessagingException   ex, WebRequest request) {
        String bodyOfText = EX_MAIL_NOT_SENT + ex.getMessage();
        return handleExceptionInternal(ex, bodyOfText, new HttpHeaders(), HttpStatus.CONTINUE, request);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Object> handleFileNotFoundException (FileNotFoundException ex, WebRequest request) {
        String bodyOfText = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfText, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Object> handleNumberFormatException (NumberFormatException ex, WebRequest request) {
        String bodyOfText = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfText, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
