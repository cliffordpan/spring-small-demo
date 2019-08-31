package me.hchome.demouser.controller;

import me.hchome.demouser.data.User;
import me.hchome.demouser.data.service.UserService;
import me.hchome.demouser.data.validation.Create;
import me.hchome.demouser.data.validation.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

/**
 * @author Junjie(Cliff) Pan
 */
@RestController
@RequestMapping(path = {"/users"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public Iterable<User> getUsers() {
        return this.service.getAll();
    }

    @GetMapping(path = "/{id:\\d+}")
    public User getUser(@PathVariable("id") long id) {
        return this.service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@RequestBody @Validated({Create.class}) User user) {
        this.service.insert(user);
    }

    @PreAuthorize("authentication.principal.id == #id")
    @PutMapping(path = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") long id, @RequestBody @Validated({Update.class}) User user) {
        service.update(id, user);
    }

    @PreAuthorize("authentication.principal.id == #id")
    @DeleteMapping(path = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) {
        service.delete(id);
    }
}
