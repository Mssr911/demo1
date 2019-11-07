package com.newspoint.demo.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
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
        List<User> userList = repository.findAllByOrderByBirthDate();
        Integer numberOfUsers = userList.size();

        service.setAgeForAllUsers(userList);

        model.addAttribute("usersList", userList);
        model.addAttribute("numberOfUsers", numberOfUsers);
        return "users";
    }



    @GetMapping("users/oldest")
    public String oldestUserWithPhoneNumber(Model model) {
        List<User> userList = repository.findAllByOrderByBirthDate();
        User oldest = null;
        for (User user : userList) {
            if (user.getPhoneNo() != null) {
                oldest = user;
                break;
            }
        }
        model.addAttribute("user", oldest);
        return "theOldest";
    }


    @GetMapping("/users/add")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/users/add")
    public String uploadUsers(@RequestBody MultipartFile file, Model model) {
        logger.info("File " + file.getOriginalFilename() + " loaded.");

        List<User> userList = repository.saveAll(service.parseDocument(file));
        logger.info("Users from " + file.getOriginalFilename() + " saved in database.");

        model.addAttribute("usersList", userList);

        return "newUsers";

    }

    @GetMapping("/users/lastName/{lastName}")
    public String findByLastName(@PathVariable String lastName, Model model) {

        List<User> list = repository.findAllByLastName(lastName);
        service.setAgeForAllUsers(list);

        model.addAttribute("usersList", list);
        model.addAttribute("last_name", lastName);

        return "allByLastName";


    }

    @DeleteMapping("/users/delete/{id}")
    public String deleteSingleUser(@PathVariable Integer id, Model model) {

        model.addAttribute("id", id);
        try {
            model.addAttribute("firstName", repository.getOne(id).getFirstName());
            model.addAttribute("lastName", repository.getOne(id).getLastName());
            model.addAttribute("phoneNo", repository.getOne(id).getPhoneNo());
            repository.deleteById(id);
            return "deletedUser";
        } catch(EntityNotFoundException e) {
            return "userNotExists";
        }


    }
// All users with pagination
    @GetMapping
    ResponseEntity<Page<User>> showAllUsers(Pageable pageable) {
        logger.info("Got request.");
        return ResponseEntity.ok(repository.findAll(pageable));

    }


}
