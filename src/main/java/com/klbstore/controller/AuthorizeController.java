package com.klbstore.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.klbstore.dao.NguoiDungDAO;

@Controller
public class AuthorizeController {
    String url = "http://localhost:2021/api/v1/auth";

    // @Autowired
    // SecurityRestTemplate securityRestTemplate;

    @Autowired
    NguoiDungDAO nguoiDungDAO;

    // @GetMapping("/rest/templates")
    // public String demo1(Model model) {
    //     model.addAttribute("db", securityRestTemplate.get(url));
    //     return "view";
    // }

    // @GetMapping("/rest/url")
    // public String demo2(Model model) {
    //     model.addAttribute("db", securityRestTemplate.get(url));
    //     return "view";
    // } 

    // @RequestMapping("/rest/url/{username}")
    // public String postDemo(Model model, @PathVariable("username") String username) {
    //     Optional<Account> OK = accountDAO.findByUsername(username);
    //     if (!OK.isEmpty()) {
    //         model.addAttribute("db", securityRestTemplate.post(url, OK));
    //     } else {
    //         model.addAttribute("message", "Người dùng không tồn tại!");
    //     }
    //     return "view";
    // }
}

