package com.myapp.web.controller;

import com.myapp.data.model.Address;
import com.myapp.data.model.User;
import com.myapp.service.user.UserService;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("controller0")
public class Controller0 {

    @Autowired
    private Address address;

//    @Autowired
//    private UserService userService;

//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        dateFormat.setLenient(false);
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
//    }

//    @InitBinder
//    protected void initBinder(WebDataBinder binder) {
//        binder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
//    }

//    @ModelAttribute
//    public void ma(@RequestParam String name, Model model) {
//        model.addAttribute("gender", "男");
//    }

    @GetMapping("get0")
    @ResponseBody
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST,reason = "任性")
//    @CrossOrigin("http://localhost:8888")
    public User get0(HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse) {

//        RequestContext requestContext=new RequestContext(httpServletRequest,httpServletResponse);
//        System.out.println(requestContext.getLocale());
//        System.out.println(requestContext.getTimeZone());
//        System.out.println(httpServletRequest.getHeader("accept-language"));
//        System.out.println(httpServletRequest.getContextPath());
//        System.out.println(httpServletRequest.getServletPath());

        String name = httpServletRequest.getParameter("name");
//        throw new IllegalStateException();

        System.out.println(address.getId());
        return new User("张三123");
    }

    @GetMapping("get1")
    @ResponseBody
    public ResponseEntity get1() {
        return ResponseEntity.ok(new User("张三"));
    }

    @PostMapping(value = "post")
    @ResponseBody
    public void post(HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse,
                     MultipartHttpServletRequest multipartHttpServletRequest,
                     @RequestParam(required = false) String name,
                     @RequestParam(required = false) MultipartFile file,
                     @RequestParam(value = "name") String[] nameArr,
                     @RequestParam(value = "name") List<String> nameList,
                     @RequestHeader("Accept") String accept,
                     @RequestHeader("Accept") String[] acceptArr
//                     @RequestParam Date date
    ) throws Exception {
//        String name1 = multipartHttpServletRequest.getParameter("name");
//        String name2 = httpServletRequest.getParameter("name");
//        MultipartFile file1 = multipartHttpServletRequest.getFile("file");
        throw new IllegalStateException();
    }

//    @GetMapping({"test0", "test00"})
//    public String test0() {
//        ModelAndView mav = new ModelAndView("test0");
//        mav.addObject("word", "Hello");
//        return "test0";
//    }

//    @GetMapping("test1")
//    public String test1() {
//        return "forward:test0";
//    }

    @RequestMapping("test1")
    @ResponseBody
    public void test1(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("test3").forward(request, response);
    }

    @RequestMapping("test2")
    public void test2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect("test3");
    }

    @RequestMapping("test3")
    public void test3(HttpServletRequest request, HttpServletResponse response) throws Exception {
    }


}
