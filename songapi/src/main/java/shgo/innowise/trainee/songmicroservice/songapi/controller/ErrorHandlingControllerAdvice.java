package shgo.innowise.trainee.songmicroservice.songapi.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import shgo.innowise.trainee.songmicroservice.songapi.response.ValidationErrorResponse;
import shgo.innowise.trainee.songmicroservice.songapi.response.Violation;

import java.util.List;

/**
 * Controller advice for handling validation errors.
 */
@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    /**
     * Exception handler for ConstraintViolationException.
     *
     * @param ex ConstraintViolationException
     * @return validation error response
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintViolationException(ConstraintViolationException ex) {
        final List<Violation> violations = ex.getConstraintViolations().stream()
                .map(violation -> new Violation(getFieldName(violation.getPropertyPath()), violation.getMessage()))
                .toList();
        return new ValidationErrorResponse(violations);
    }

    private String getFieldName(Path propertyPath){
        String field = null;
        for(Path.Node node : propertyPath){
            field = node.toString();
        }
        return field;
    }
}
