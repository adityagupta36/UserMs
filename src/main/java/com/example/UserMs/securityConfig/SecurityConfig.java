package com.example.UserMs.securityConfig;

import com.example.UserMs.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/ums/createUser").permitAll().anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults()).build();
    }

     //[Currently we have created a UserDetailsServiceIpl class]
     //here we can create a bean and implement using lambda function or create a class
     // as shown below to load user by taking username from client request
     // and loads user info such as name, pass, roles and set it to UserDetails
     // which will be later crosschecked with authentication object's credentials
 /**
     *
     *
     * @Service
     * public class UserDetailsServiceImpl implements UserDetailsService {
     *     @Autowired
     *     UserRepo userRepo;
     *
     *     @Override
     *     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     *         User user = userRepo.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("User NOt Found"));
     *         return org.springframework.security.core.userdetails.User.builder().username(user.getUserName()).password(user.getPassword()).roles(user.getRole()).build();
     *     }
     * }
     *
     *
     *
     *
     * //inside SecurityConfig.java
     *
     * @Bean
     * public UserDetailsService userDetailsService(UserRepository userRepository) {
     *     return new UserDetailsService() {
     *         @Override
     *         public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     *             return userRepository.findByUsername(username)
     *                     .map(u -> User.builder()
     *                             .username(u.getUsername())
     *                             .password(u.getPassword())
     *                             .roles(u.getRole().name())
     *                             .disabled(!u.isEnabled())
     *                             .build()
     *                     )
     *                     .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
     *         }
     *     };
     * }
     *
     * OR
     *
     *          @Bean
     *           public UserDetailsService userDetailsService(UserRepo userRepository) {
     *               return username -> userRepository.findByUserName(username)
     *                              .map(u -> User.builder()
     *                                          .username(u.getUserName())
     *                                          .password(u.getPassword())
     *                                          .roles(u.getRole()) // Spring auto-prefixes with "ROLE_" if needed
     *                                          .build()).orElseThrow(() -> new UsernameNotFoundException("User Not Found!"));
     *        }
     *
     *
     *
     */






}
