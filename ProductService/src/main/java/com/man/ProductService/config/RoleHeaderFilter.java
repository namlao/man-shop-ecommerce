//package com.man.ProductService.config;
//
//
//import java.io.IOException;
//import java.util.List;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//public class RoleHeaderFilter extends OncePerRequestFilter {
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException, java.io.IOException {
//
//        String username = request.getHeader("X-Auth-Users");
//        String role = request.getHeader("X-Auth-Roles"); 
//
//        if (username != null && role != null) {
//            List<GrantedAuthority> authorities =
//                    List.of(new SimpleGrantedAuthority(role));
//
//            UsernamePasswordAuthenticationToken auth =
//                    new UsernamePasswordAuthenticationToken(username, null, authorities);
//
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
