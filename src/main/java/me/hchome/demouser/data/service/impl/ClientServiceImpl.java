package me.hchome.demouser.data.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.hchome.demouser.data.Client;
import me.hchome.demouser.data.User;
import me.hchome.demouser.data.repository.ClientRepository;
import me.hchome.demouser.data.repository.UserRepository;
import me.hchome.demouser.data.service.ClientService;
import me.hchome.demouser.security.key.ClientUsersAuthenticationKeyGenerator;
import me.hchome.demouser.security.key.KeyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * Client Service register or load a client.
 *
 * @author Clifford Pan
 */
@Service("clientService")
public class ClientServiceImpl implements ClientService, InitializingBean {

    @Autowired
    private ClientRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ClientUsersAuthenticationKeyGenerator generator;

    private Cache<String, Client> cache;

    /**
     * Load a client map to a User
     *
     * @param username client id
     * @return Spring user detail
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = cache.getIfPresent(username);
        if (client == null) {
            client = repository.findOneByClientId(username);
            if (client == null) throw new UsernameNotFoundException("Client associates to this id isn't exists.");
        }
        return org.springframework.security.core.userdetails.User.withUsername(client.getClientId()).password(client.getPassword()).authorities(AuthorityUtils.createAuthorityList("ROLE_CLIENT")).build();
    }

    /**
     * Load a client details asynchronously.
     *
     * @param username client id
     * @return Complete Future for
     */
    @Transactional(readOnly = true)
    @Override
    @Async("securityExecutor")
    public CompletableFuture<Client> loadClientByClientId(String username) {
        Client client = cache.getIfPresent(username);
        if (client == null) {
            client = repository.findOneByClientId(username);
            if (client == null) throw new UsernameNotFoundException("Client associates to this id isn't exists.");
        }
        return CompletableFuture.completedFuture(client);
    }

    @Transactional
    @Override
    @Async("securityExecutor")
    public CompletableFuture<Client> registerClient(Client client) {
        client.setPassword(encoder.encode(client.getPassword()));
        KeyPair pair = generator.generateKeyPair();
        client.setPrivateKey(pair.getPrivate().getEncoded());
        client.setPublicKey(pair.getPublic().getEncoded());
        Client result = repository.save(client);
        cache.put(client.getClientId(), client);
        return CompletableFuture.completedFuture(result);
    }


    @Transactional
    @Override
    @Async("securityExecutor")
    public CompletableFuture<Client> addUserToClient(Client client, String uid) {
        Client c = cache.getIfPresent(client.getClientId());
        if (c == null) {
            c = repository.findOneByClientId(client.getClientId());
            if (client == null) throw new UsernameNotFoundException("Client associates to this id isn't exists.");
        }
        User u = userRepository.findOneByEmail(uid);
        c.getUsers().add(u);
        return CompletableFuture.completedFuture(repository.save(c));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cache = CacheBuilder.newBuilder()
                .expireAfterAccess(Duration.ofDays(1))
                .softValues()
                .maximumSize(1000)
                .build();
    }
}
