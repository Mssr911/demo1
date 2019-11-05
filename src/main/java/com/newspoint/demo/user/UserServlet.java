package com.newspoint.demo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Controller
public class UserServlet {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository repository;
    private UserService service;

    public UserServlet(UserRepository repository, UserService service) {
        this.repository = repository;
        this.service = service;
    }


    @GetMapping("/users")
    public String allUsers(Model model) {
        List<User> userList = repository.findAll(Sort.by("birthDate").ascending());
        model.addAttribute("usersList", userList);
        return "users";
    }
    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping(value = "/upload")
    public String uploadSimple(@RequestBody MultipartFile file, Model model) {
        logger.info("File " + file.getOriginalFilename() + " loaded.");

        List<User> userList = repository.saveAll(service.parseDocument(file));
        logger.info("Users from " + file.getOriginalFilename() + " saved in database.");

        model.addAttribute("usersList", userList);

        return "newUsers";

    }






}
