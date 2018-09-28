package com.myapp.web.controller;

import com.myapp.data.model.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("controller1")
public class Controller1 {

    @PostMapping("postForm")
    public Param postForm(Param param) {
        return param;
    }

    @PostMapping("postJson")
    public Param postJson(@RequestBody Param param) {
        return param;
    }

}
