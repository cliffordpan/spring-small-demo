package me.hchome.demouser.data.service;

import me.hchome.demouser.data.Client;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

public interface ClientService extends UserDetailsService {

    @Transactional(readOnly = true)
    @Async
    CompletableFuture<Client> loadClientByClientId(String username);

    @Transactional
    @Async
    CompletableFuture<Client> registerClient(final Client client);

    @Transactional
    @Async("securityExecutor")
    CompletableFuture<Client> addUserToClient(Client client, String uid);
}
