package org.d3javu.bd.rest.handler;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackageClasses = RestController.class)
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
}
