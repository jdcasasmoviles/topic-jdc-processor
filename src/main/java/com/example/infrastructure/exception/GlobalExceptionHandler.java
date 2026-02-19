package com.example.infrastructure.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.stream.Collectors;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class);

    @Override
    public Response toResponse(Throwable exception) {
        LOG.error("Exception caught by global handler", exception);

        if (exception instanceof ConstraintViolationException) {
            return handleValidationException((ConstraintViolationException) exception);
        } else if (exception instanceof BusinessException) {
            return handleBusinessException((BusinessException) exception);
        } else if (exception instanceof jakarta.ws.rs.WebApplicationException) {
            return ((jakarta.ws.rs.WebApplicationException) exception).getResponse();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ErrorResponse.of("Internal Server Error", exception.getMessage()))
                .build();
    }

    private Response handleValidationException(ConstraintViolationException exception) {
        String message = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ErrorResponse.of("Validation Error", message))
                .build();
    }

    private Response handleBusinessException(BusinessException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ErrorResponse.of("Business Error", exception.getMessage()))
                .build();
    }
}