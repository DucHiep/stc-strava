package application.enpoint;


import application.model.User;
import application.repository.UserRepository;
import application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserEnpoint {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository user;
    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }
    @GetMapping("/register")
    public String show(Model model){
        List<User> userList = user.findAll();
        model.addAttribute("userList",userList);
        return "signup_form";
    }
}
