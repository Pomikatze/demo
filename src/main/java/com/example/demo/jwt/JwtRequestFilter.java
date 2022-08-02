package com.example.demo.jwt;

import com.example.demo.entity.SudisEntity;
import com.example.demo.service.SudisService;
import feign.Request;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    private final SudisService sudisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String cookie = request.getHeader("Cookie");
        if (cookie != null && cookie.startsWith("JSESSIONID")) {
            SudisEntity sudisEntity = sudisService.findBySessionId(cookie.substring(11));

            if (sudisEntity.getToken() != null){
                String jwt = sudisEntity.getToken();

                String username = jwtTokenUtil.getUsernameFromToken(jwt);
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,
                        null,
                        jwtTokenUtil.getRolesFromToken(jwt).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }

        //Стандартная реализация Jwt фильтра
//        String authHeader = request.getHeader("Authorization");
//        String username = null;
//        String jwt = null;
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            jwt = authHeader.substring(7);
//
//            username = jwtTokenUtil.getUsernameFromToken(jwt);
//            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,
//                    null,
//                    jwtTokenUtil.getRolesFromToken(jwt).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
//            SecurityContextHolder.getContext().setAuthentication(token);
//        }
        filterChain.doFilter(request, response);
    }
}
