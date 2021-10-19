package com.example.demo.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Component
public class ErrorHandlingController {

	@ExceptionHandler(DataAccessException.class)
	public String dataAccessExceptionHandler(DataAccessException e, Model model) {

		return "400";

	}

	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e, Model model) {

		return "400";
	}

}
