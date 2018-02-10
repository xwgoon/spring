package com.myapp.web.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("controller1")
public class Controller1 {

    @PostMapping(value = "post")
    @ResponseBody
    public void post(@RequestParam Date date) {
    }

}
