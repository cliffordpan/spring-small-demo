package me.hchome.demouser.controller;

import me.hchome.demouser.data.User;
import me.hchome.demouser.data.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 * Welcome controller
 *
 * @author Junjie(Cliff) Pan
 */
@RestController
public class WelcomeController {

    private UserService service;

    /**
     * Testing api response correctly
     * */
    @GetMapping(path = {"/welcome", "/"})
    public Map<String, String> get() {
        return Collections.singletonMap("Foo", "Bar");
    }


}
