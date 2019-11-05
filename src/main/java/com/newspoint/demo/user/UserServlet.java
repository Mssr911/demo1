package com.newspoint.demo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        Integer numberOfUsers = userList.size();
        model.addAttribute("usersList", userList);
        model.addAttribute("numberOfUsers", numberOfUsers);
        return "users";
    }

    @GetMapping("users/oldest")
    public String oldestUser(Model model) {
        List<User> userList = repository.findAll(Sort.by("birthDate").ascending());
        User oldest = null;
        for (User user : userList) {
            if(user.getPhone_no()!=null) {
                oldest = user;
                break;
            }
        }
        model.addAttribute("user", oldest);
        return "the_oldest";
    }

//    @GetMapping("/users")
//    ResponseEntity<List<User>> findAllUsers() {
//        return ResponseEntity.ok(repository.findAll());
//    }

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
