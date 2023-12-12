package com.unitral.microdoc.filters;

import com.unitral.microdoc.constant.Constants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtTokenGeneratorFilter extends OncePerRequestFilter {


    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(null!=authentication){
            SecretKey secretKey= Keys.hmacShaKeyFor(Constants.JWT_SECRETKEY.getBytes(StandardCharsets.UTF_8));
            String jwt= Jwts.builder().setIssuer("microdoc").setSubject("JWT Token")
                    .claim("username",authentication.getName())
                    .claim("authorities",authentication.getAuthorities().toString())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime()+300000))
                    .signWith(secretKey).compact();
            System.out.println("jwt : "+jwt);
            response.setHeader(Constants.JWT_HEADER,jwt);
        }

        filterChain.doFilter(request,response);

    }

    @Override
    protected boolean shouldNotFilter(@NotNull HttpServletRequest request) throws ServletException {
        return false;
    }
}
