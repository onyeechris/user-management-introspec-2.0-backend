package com.activedge.usermgt.security;

import com.activedge.usermgt.model.Authority;
import com.activedge.usermgt.service.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.activedge.usermgt.config.Constants.HEADER_STRING;
import static com.activedge.usermgt.config.Constants.TOKEN_PREFIX;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJWTFromRequest(httpServletRequest);
            String requestURI = httpServletRequest.getRequestURI();
            if ("/auth/forgot-password".equals(requestURI)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String username = tokenProvider.getUsernameFromJWT(jwt);
                if(username != null) {
//                    Collection<? extends GrantedAuthority> authorities = tokenProvider.isAuthenticated(jwt) ? tokenProvider.getAuthoritiesFromJWT(jwt)
                    List<String> authorities = tokenProvider.isAuthenticated(jwt) ? tokenProvider.getAuthoritiesFromJWT(jwt)
                            : Arrays.asList(AuthoritiesConstants.ROLE_PRE_VERIFICATION_USER);
                    List permissions = tokenProvider.getPermissionFromJWT(jwt);
                    System.out.println(">>> Authorities: " + authorities);
                    System.out.println(">>> Permissions: " + permissions);

//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                            username, null,authorities.stream().collect(Collectors.toList())
//                    );
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null,authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_STRING);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }

}
