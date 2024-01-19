package com.unitral.microdoc.config;


import com.unitral.microdoc.filters.JwtTokenGeneratorFilter;
import com.unitral.microdoc.filters.JwtTokenValidationFilter;
import com.unitral.microdoc.repository.UserAuthenticationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {


    private final UserAuthenticationRepo userAuthRepo;

    @Autowired
    public SecurityConfig(UserAuthenticationRepo userAuthRepo) {
        this.userAuthRepo = userAuthRepo;
    }


    @Bean
    public UserDetailsService getUserDetailsService(){
        return new CustomUserDetailsService( userAuthRepo);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getUserDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
//
//
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers("/*").hasRole("ADMIN")
//                        .requestMatchers("/type").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .oauth2Login(Customizer.withDefaults());

/*        http.addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class);
        http.addFilterBefore(new JwtTokenValidationFilter(),BasicAuthenticationFilter.class);*/


        return http.build();
    }

}
