package com.angrygrizley.rsoi_lab4.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class HomeController {

    @Autowired
    private UserService us;

    @GetMapping(value = "/private/addUser")
    public String index(@RequestParam (value="user") String user, @RequestParam (value="pass") String pass){
        us.save(new User(user, pass, new ArrayList<Role>(), true));
        return "User added";
    }
}
