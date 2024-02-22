package uz.project.controller;

import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uz.project.common.exception.AccessDeniedException;
import uz.project.common.exception.ApiException;
import uz.project.common.response.ErrorResponse;
import uz.project.common.response.ResponseMessage;
import uz.project.common.response.status.SystemStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> internalServerError(ApiException e) {
        var responseStatus = e.getResponseStatus();
        log.error("Api exception status {} = {} ",
                e.getResponseStatus().getClass().getSimpleName(),
                responseStatus.getStatus()
        );
        var tag = responseStatus.name();

        var build = ErrorResponse.builder()
                .status(responseStatus.getStatus())
                .message(new ResponseMessage(responseStatus.getMessageType(), tag))
                .build();
        return ResponseEntity
                .status(responseStatus.getHttpStatus())
                .body(build);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("Validation error {}  ", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> constraintViolationException(
            javax.validation.ConstraintViolationException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((error) -> {
            var stream = StreamSupport.stream(error.getPropertyPath().spliterator(), false);
            var field = Objects.requireNonNull(stream.reduce((first, second) -> second).orElse(null)).
                    toString();
            var errorMessage = error.getMessage();
            errors.put(field, errorMessage);
        });
        log.error("Validation error {}  ", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        var mostSpecificCause = ex.getMostSpecificCause();
        Map<String, String> errors = new HashMap<>();
        var exceptionName = mostSpecificCause.getClass().getName();
        var message = mostSpecificCause.getMessage();
        errors.put("exceptionName", exceptionName);
        errors.put("message", message);
        log.error("HttpMessageNotReadableException  error {}  ", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        var unsupported = "Unsupported content type: " + ex.getContentType();
        var supported = "Supported content types: " + MediaType.toString(ex.getSupportedMediaTypes());
        Map<String, String> errors = new HashMap<>();
        errors.put("unsupported", unsupported);
        errors.put("supported", supported);
        log.error("HttpMessageNotReadableException  error {}  ", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler({
            AccessDeniedException.class,
            org.springframework.security.access.AccessDeniedException.class,
            JwtException.class,
            AuthenticationException.class
    })
    public ResponseEntity<Map<String, String>> springAccessDeniedException(
            RuntimeException e
    ) {
        var message = e.getMessage();
        Map<String, String> errors = new HashMap<>();
        errors.put("AUTH", message);
        log.error("Spring access denied error {}  ", errors);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errors);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleExceptionInternal(Throwable e) {
        log.error("Unknown error  {}", e.getLocalizedMessage());
        var internalServerError = SystemStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(internalServerError.getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(internalServerError.getStatus())
                        .message(new ResponseMessage(internalServerError.getMessageType(), e.getMessage()))
                        .build()
                );
    }
}
