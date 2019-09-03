package me.hchome.demouser.controller;

import me.hchome.demouser.data.Client;
import me.hchome.demouser.data.service.ClientService;
import me.hchome.demouser.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/clients")
public class ClientController {

    @Autowired
    private ClientService service;

    private UserService userService;

    @PostMapping
    public CompletableFuture<Client> registerClient(@RequestBody Client client) {
        return service.registerClient(client);
    }

    @GetMapping("/.well-known/jwks.json")
    public CompletableFuture<Client> getClient(@AuthenticationPrincipal Authentication authentication) {
        return service.loadClientByClientId(authentication.getName());
    }

    @PatchMapping(params = "uid")
    public CompletableFuture<Client> addUser(@AuthenticationPrincipal Authentication authentication, @RequestParam("uid") String userId) {
        return service.loadClientByClientId(userId).thenCompose(client -> service.addUserToClient(client, userId));
    }

}
