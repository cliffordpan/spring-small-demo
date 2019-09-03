package me.hchome.demouser.controller;

import lombok.Data;
import me.hchome.demouser.data.Client;
import me.hchome.demouser.data.service.ClientService;
import me.hchome.demouser.data.service.UserService;
import me.hchome.demouser.security.key.ClientUsersAuthenticationKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/clients")

public class ClientController {

    @Data
    private static class JWKS {
        private Collection<JWK> keys;
    }

    @Data
    private static class JWK {
        private String alg;
        private String use;
        private String e;
        private String n;
        private String kty;
        private String kid;

    }

    @Autowired
    private ClientService service;

    @Autowired
    private UserService userService;

    @Autowired
    private ClientUsersAuthenticationKeyGenerator generator;

    @PostMapping
    public CompletableFuture<Client> registerClient(@RequestBody Client client) {
        return service.registerClient(client);
    }

    @GetMapping("/.well-known/jwks.json")
    public CompletableFuture<JWKS> getClient(@AuthenticationPrincipal Authentication authentication) {
        return service.loadClientByClientId(authentication.getName())
                .thenApplyAsync(client -> {
                    JWKS jwks = new JWKS();
                    JWK jwk = new JWK();
                    byte[] bytes = client.getPublicKey();
                    try {
                        RSAPublicKey publicKey = (RSAPublicKey) generator.getPublic(bytes);
                        jwk.setAlg(publicKey.getAlgorithm());
                        jwk.setE(publicKey.getPublicExponent().toString(16));
                        jwk.setN(publicKey.getModulus().toString(16));
                        jwk.setUse("sig");
                        jwk.setKty(generator.getAlgorithm());
                        jwk.setKid(String.valueOf(client.getId()));
                        jwks.keys = Collections.singleton(jwk);
                        return jwks;
                    } catch (InvalidKeySpecException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    @PatchMapping(params = "uid")
    public CompletableFuture<Client> addUser(@AuthenticationPrincipal Authentication authentication, @RequestParam("uid") String userId) {
        return service.loadClientByClientId(userId).thenCompose(client -> service.addUserToClient(client, userId));
    }

}
