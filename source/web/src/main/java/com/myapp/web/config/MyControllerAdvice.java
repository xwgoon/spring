package com.myapp.web.config;

import org.springframework.format.datetime.DateFormatter;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class MyControllerAdvice extends ResponseEntityExceptionHandler {

//    @ModelAttribute
//    public void modelAttribute(Model model) {
//    }

//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        binder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
//    }

//    @ExceptionHandler(IOException.class)
//    @ResponseBody
//    public String exceptionHandler() {
//        return "IOException occurs!";
//    }


}
