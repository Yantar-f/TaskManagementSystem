package ru.effectivemobile.taskmanagementsystem.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.effectivemobile.taskmanagementsystem.exception.AccountNotFoundException;
import ru.effectivemobile.taskmanagementsystem.exception.EmailOccupiedException;
import ru.effectivemobile.taskmanagementsystem.exception.InvalidAccountCredentialsException;
import ru.effectivemobile.taskmanagementsystem.exception.InvalidTokenException;
import ru.effectivemobile.taskmanagementsystem.exception.MissingTokenException;
import ru.effectivemobile.taskmanagementsystem.exception.NotAvailableTaskException;
import ru.effectivemobile.taskmanagementsystem.exception.TaskNotFoundException;

import java.net.URI;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            @NonNull HttpRequestMethodNotSupportedException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return generateErrorResponse(
                METHOD_NOT_ALLOWED,
                extractURI(request),
                "Method not supported",
                ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            @NonNull HttpMediaTypeNotSupportedException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return generateErrorResponse(
                UNSUPPORTED_MEDIA_TYPE,
                extractURI(request),
                "Media type not supported",
                ex.getMessage()
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            @NonNull HttpMediaTypeNotAcceptableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return generateErrorResponse(
                NOT_ACCEPTABLE,
                extractURI(request),
                "Not acceptable media type",
                ex.getMessage()
        );
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            @NonNull MissingServletRequestParameterException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return generateErrorResponse(
                BAD_REQUEST,
                extractURI(request),
                "Missing request param",
                ex.getMessage()
        );
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            @NonNull MissingServletRequestPartException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return generateErrorResponse(
                BAD_REQUEST,
                extractURI(request),
                "Missing request part",
                ex.getMessage()
        );
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            @NonNull ServletRequestBindingException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return generateErrorResponse(
                BAD_REQUEST,
                extractURI(request),
                "Binding exception",
                ex.getMessage()
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "Undefined constraint violation";

        return generateErrorResponse(
                BAD_REQUEST,
                extractURI(request),
                "Argument not valid",
                errorMessage
        );
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            @NonNull NoHandlerFoundException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return generateErrorResponse(
                NOT_FOUND,
                extractURI(request),
                "No handler found",
                ex.getMessage()
        );
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
            @NonNull AsyncRequestTimeoutException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return generateErrorResponse(
                SERVICE_UNAVAILABLE,
                extractURI(request),
                "Request timeout",
                ex.getMessage()
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            @NonNull HttpMessageNotWritableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return generateErrorResponse(
                INTERNAL_SERVER_ERROR,
                extractURI(request),
                "Not writable http message",
                ex.getMessage()
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        return generateErrorResponse(
                BAD_REQUEST,
                extractURI(request),
                "Not readable http message",
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      HttpServletRequest request) {

        return generateErrorResponse(
                BAD_REQUEST,
                request.getRequestURI(),
                "Argument type mismatch",
                "Can`t parse parameter " + ex.getPropertyName()//ex.getMessage()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleEmailOccupied(HttpServletRequest request,
                                                      ConstraintViolationException ex) {
        return generateErrorResponse(
                BAD_REQUEST,
                request.getRequestURI(),
                "Argument not valid",
                ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList().toString()
        );
    }

    @ExceptionHandler(MissingTokenException.class)
    public ResponseEntity<Object> handleMissingToken(HttpServletRequest request, MissingTokenException ex) {
        return generateErrorResponse(
                BAD_REQUEST,
                request.getRequestURI(),
                "Missing token",
                ex.getMessage()
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Object> handleInvalidToken(HttpServletRequest request, InvalidTokenException ex) {
        return generateErrorResponse(
                BAD_REQUEST,
                request.getRequestURI(),
                "Invalid token",
                ex.getMessage()
        );
    }

    @ExceptionHandler(InvalidAccountCredentialsException.class)
    public ResponseEntity<Object> handleInvalidAccountCredentials(HttpServletRequest request,
                                                           InvalidAccountCredentialsException ex) {

        return generateErrorResponse(
                BAD_REQUEST,
                request.getRequestURI(),
                "Invalid account data",
                ex.getMessage()
        );
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> handleAccountNotFound(HttpServletRequest request,
                                                     AccountNotFoundException ex) {

        return generateErrorResponse(
                NOT_FOUND,
                request.getRequestURI(),
                "Account not found",
                ex.getMessage()
        );
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Object> handleTaskNotFound(HttpServletRequest request,
                                                     TaskNotFoundException ex) {

        return generateErrorResponse(
                NOT_FOUND,
                request.getRequestURI(),
                "Task not found",
                ex.getMessage()
        );
    }

    @ExceptionHandler(NotAvailableTaskException.class)
    public ResponseEntity<Object> handleNotAvailableTask(HttpServletRequest request,
                                                         NotAvailableTaskException ex) {

        return generateErrorResponse(
                FORBIDDEN,
                request.getRequestURI(),
                "Not available task operation",
                ex.getMessage()
        );
    }

    @ExceptionHandler(EmailOccupiedException.class)
    public ResponseEntity<Object> handleEmailOccupied(HttpServletRequest request,
                                                      EmailOccupiedException ex) {

        return generateErrorResponse(
                CONFLICT,
                request.getRequestURI(),
                "Occupied value",
                ex.getMessage()
        );
    }

    private ResponseEntity<Object> generateErrorResponse(HttpStatus httpStatus,
                                                         String requestUri,
                                                         String errorTitle,
                                                         String errorMessage) {

        ProblemDetail body = ProblemDetail.forStatus(httpStatus.value());

        body.setDetail(errorMessage);
        body.setInstance(URI.create(requestUri));
        body.setTitle(errorTitle);

        return ResponseEntity
                .status(httpStatus.value())
                .contentType(APPLICATION_PROBLEM_JSON)
                .body(body);
    }

    private String extractURI(WebRequest request) {
        try {
            return ((ServletWebRequest) request).getRequest().getServletPath();
        } catch (ClassCastException ex) {
            return "/";
        }
    }
}
