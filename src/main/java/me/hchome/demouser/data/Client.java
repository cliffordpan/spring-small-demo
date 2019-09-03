package me.hchome.demouser.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table(name = "t_clients")
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String clientId;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    @Lob
    private byte[] privateKey;

    @JsonIgnore
    @Lob
    private byte[] publicKey;

    @OneToMany(cascade = CascadeType.REMOVE)
    private Collection<User> users;

    @Version
    private int version;


}
