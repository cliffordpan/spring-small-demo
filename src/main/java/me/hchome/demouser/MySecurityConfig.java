package me.hchome.demouser;

import me.hchome.demouser.security.key.ClientUsersAuthenticationKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @author Junjie(Cliff) Pan
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Configuration
public class MySecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ClientUsersAuthenticationKeyGenerator clientUsersAuthenticationKeyGenerator() {
        return new ClientUsersAuthenticationKeyGenerator();
    }

    @Configuration
    @Order(1)
    public static class ClientServerConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        @Qualifier("clientService")
        private UserDetailsService service;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(service);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/clients/**").authorizeRequests()
                    .regexMatchers(HttpMethod.POST, "/clients").permitAll()
                    .regexMatchers("/clients/.well-known/jwks.json").hasRole("CLIENT")
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                    .and()
                    .csrf().disable()
                    .formLogin().disable()
                    .httpBasic();
        }
    }

    @Configuration
    public static class UserSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        @Qualifier("userService")
        private UserDetailsService service;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/", "/welcome").permitAll()
                .mvcMatchers(HttpMethod.GET, "/users", "/users/*").authenticated()
                .mvcMatchers(HttpMethod.POST, "/users").permitAll()
                .mvcMatchers(HttpMethod.PUT, "/users/{id:\\d+}").authenticated()
                .mvcMatchers(HttpMethod.DELETE, "/users/{id:\\d+}").authenticated()
                .anyRequest().denyAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic().and()
                .formLogin().disable()
                .csrf().disable();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(service);
        }
    }

//    @Autowired
//    private UserDetailsService userDetailsService;
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .mvcMatchers(HttpMethod.GET, "/", "/welcome").permitAll()
//                .mvcMatchers(HttpMethod.GET, "/users", "/users/*").authenticated()
//                .mvcMatchers(HttpMethod.POST, "/users").permitAll()
//                .mvcMatchers(HttpMethod.PUT, "/users/{id:\\d+}").authenticated()
//                .mvcMatchers(HttpMethod.DELETE, "/users/{id:\\d+}").authenticated()
//                .anyRequest().denyAll()
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .httpBasic().and()
//                .formLogin().disable()
//                .csrf().disable();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
//    }

}
