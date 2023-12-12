package com.unitral.microdoc.filters;

import com.unitral.microdoc.constant.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.yaml.snakeyaml.scanner.Constant;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.lang.constant.Constable;
import java.nio.charset.StandardCharsets;

public class JwtTokenValidationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        String jwt=request.getHeader(Constants.JWT_HEADER);
        System.out.println("jwt  : "+jwt);
        if(null!=jwt) {
            try {
                SecretKey secretKey = Keys.hmacShaKeyFor(Constants.JWT_SECRETKEY.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parser().verifyWith(secretKey)
                        .build().parseSignedClaims(jwt)
                        .getPayload();
                System.out.println(claims.toString());
                Authentication auth = new UsernamePasswordAuthenticationToken(claims.get("username"), null, AuthorityUtils.commaSeparatedStringToAuthorityList(claims.get("authorities").toString()));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }catch (Exception e){
                throw new BadCredentialsException("Invalid Token Received");
        }
        }
        filterChain.doFilter(request,response);

    }

}
