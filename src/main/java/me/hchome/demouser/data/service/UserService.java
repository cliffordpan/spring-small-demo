package me.hchome.demouser.data.service;

import me.hchome.demouser.data.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Junjie(Cliff) Pan
 */
public interface UserService extends UserDetailsService  {

    Iterable<User> getAll();

    User get(final long id);

    User insert(final User user);

    User update(final long id, final User user);

    void delete(final long id);
}
