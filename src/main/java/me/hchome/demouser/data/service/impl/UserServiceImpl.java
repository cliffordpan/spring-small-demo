package me.hchome.demouser.data.service.impl;

import me.hchome.demouser.data.User;
import me.hchome.demouser.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.*;


/**
 * @author Junjie(Cliff) Pan
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private EntityManager manager;

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TypedQuery<User> query = manager.createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", username);
        User user = query.getSingleResult();
        if (user == null) throw new UsernameNotFoundException("User name not found");
        return user;
    }


    @Override
    @Transactional(readOnly = true)
    public Iterable<User> getAll() {
        TypedQuery<User> query = manager.createNamedQuery("User.findAll", User.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public User get(long id) {
        return manager.find(User.class, id);
    }

    @Override
    @Transactional
    public User insert(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        manager.persist(user);
        return user;
    }

    @Override
    @Transactional
    public User update(long id, User user) {
        User u = manager.find(User.class, id);
        if(!StringUtils.isEmpty(user.getEmail())){
            u.setEmail(user.getEmail());
        }

        if(!StringUtils.isEmpty(user.getName())){
            u.setName(user.getName());
        }

        if(!StringUtils.isEmpty(user.getPassword())){
            u.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        u.setVersion(user.getVersion());
        Query query = manager.createNamedQuery("User.update");
        query.setParameter("email",u.getEmail());
        query.setParameter("name", u.getName());
        query.setParameter("password",u.getPassword());
        query.setParameter("version",u.getVersion());
        query.setParameter("id",u.getId());
        query.setParameter("oldVersion",user.getVersion());
        manager.detach(u);
        int numUpdated = query.executeUpdate();
        if(numUpdated > 0) return this.get(u.getId());
        else throw new OptimisticLockException("Version or id isn't match");
    }

    @Override
    @Transactional
    public void delete(long id) {
        User user = manager.find(User.class, id);
        if (user == null) throw new EntityNotFoundException();
        manager.remove(user);
    }
}
