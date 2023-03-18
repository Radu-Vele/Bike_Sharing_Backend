package com.backend.se_project_backend.config.jwt;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.mongodb.lang.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter used for securing any request
 * - generates a filter bean
 * - uses the spring implementation OncePerRequestFilter
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     *
     * @param httpServletRequest our known request, used to extract data
     * @param httpServletResponse response
     * @param filterChain list of any other filters to be executed
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, @NonNull FilterChain filterChain) throws IOException {
        try {
            String authorization = httpServletRequest.getHeader("Authorization"); //get the JWT
            String token = null;
            String username = null;

            if(authorization == null || !authorization.startsWith("Bearer")) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return; //early return
            }

            // extract token
            token = authorization.substring(7); //after keyword Bearer
            username = jwtUtility.getUsernameFromToken(token);


            if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails
                        = userDetailsService.loadUserByUsername(username);

                if (jwtUtility.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                    );
                    //Context holder is updated
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

            filterChain.doFilter(httpServletRequest, httpServletResponse);

        } catch (Exception e) {
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpServletResponse.getWriter().write(e.getMessage());
        }
    }
}
