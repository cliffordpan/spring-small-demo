package me.hchome.demouser.data.repository;

import me.hchome.demouser.data.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author clifford on 2019-09-02
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE email=:uid")
    User findOneByEmail(@Param("uid")String uid);
}
