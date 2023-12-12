package com.unitral.microdoc.config;

import com.unitral.microdoc.entity.UserAuthentication;
import com.unitral.microdoc.repository.UserAuthenticationRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
    private final UserAuthenticationRepo userAuthRepo;

    public CustomUserDetailsService(UserAuthenticationRepo userAuthRepo) {
        this.userAuthRepo = userAuthRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthentication user= userAuthRepo.findByUsername(username);

        if(user==null){
            throw  new UsernameNotFoundException ("user not found");
        }
        System.out.println("user1"+user.toString());
        return new CustomUser(user);
    }
}
