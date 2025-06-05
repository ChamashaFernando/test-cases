package lk.chamasha.test.cases.exception;

import lk.chamasha.test.cases.controller.response.ErrorResponse;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppControllerAdviser{
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({ChangeSetPersister.NotFoundException.class})
    public ErrorResponse handleNotFoundException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(e.getMessage());
        return errorResponse;

    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler({NotCreatedException.class})
    public ErrorResponse handleNotCreatedException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(e.getMessage());
        return errorResponse;
    }
}
