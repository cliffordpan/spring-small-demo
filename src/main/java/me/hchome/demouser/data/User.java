package me.hchome.demouser.data;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.hchome.demouser.data.validation.Create;
import me.hchome.demouser.data.validation.Update;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author Junjie(Cliff) Pan
 */
@Data
@Entity
@Table(name = "t_users")
@Access(AccessType.FIELD)
@NamedQueries({
        @NamedQuery(query = "SELECT u FROM User u WHERE u.email = :email", name = "User.findByEmail"),
        @NamedQuery(query = "SELECT u FROM User u", name = "User.findAll"),
        @NamedQuery(query = "UPDATE User u SET u.email=:email,u.name=:name,u.password=:password,u.version=:version WHERE u.id=:id and u.version = :oldVersion",
                name = "User.update")
})
public class User implements Serializable, UserDetails {

    public User() {
    }

    public User(String email, String name) {
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @Column(unique = true, nullable = false, length = 50)
    @Email(groups = {Default.class, Create.class})
    @NotBlank(groups = {Default.class, Create.class})
    @Null(groups = {Update.class})
    private String email;

    @Column(nullable = false, length = 50)
    @Pattern(regexp = "^\\w+[\\w\\s]*$", groups = {Default.class, Create.class})
    @NotBlank(groups = {Create.class})
    private String name;

    @Column(nullable = false, length = 70)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(groups = {Create.class})
    private String password; // Add this field for spring security

    @Version
    private int version;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("USER");
    }

    @PrePersist
    @PreUpdate
    public void beforeSave() {
        setName(getName().trim());
        setEmail(getEmail().trim());
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
