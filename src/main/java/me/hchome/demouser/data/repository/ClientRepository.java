package me.hchome.demouser.data.repository;

import me.hchome.demouser.data.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {

    @Query("SELECT c FROM Client c WHERE c.clientId = :clientId")
    Client findOneByClientId(@Param("clientId") final String clientId);

    @Query("SELECT c FROM Client c WHERE c.clientId = :clientId")
    CompletableFuture<Client> findOneByClientIdAsync(@Param("clientId") final String clientId);
}
