package com.Avinya.App.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Avinya.App.Security.Jwt.AuthEntryPointJwt;
import com.Avinya.App.Security.Jwt.AuthTokenFilter;
import com.Avinya.App.Security.Jwt.JwtUtils;
import com.Avinya.App.Security.Service.UserDetailsServiceImpl;

@Configuration
@EnableGlobalMethodSecurity(
  prePostEnabled = true)
public class WebSecurityConfig { 
@Autowired
UserDetailsServiceImpl userDetailsService;

@Autowired
JwtUtils jwtUtils;

@Autowired
private AuthEntryPointJwt unauthorizedHandler;

@Bean
public AuthTokenFilter authenticationJwtTokenFilter() {
  return new AuthTokenFilter();
}

@Bean
public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
}

@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
  return authConfig.getAuthenticationManager();
}

@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer.authenticationEntryPoint(unauthorizedHandler))
            .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                try {
                    authorizationManagerRequestMatcherRegistry
                            .antMatchers("/api/**").permitAll()
//                            .antMatchers("/api/checkIn/**").permitAll()
//                            .requestMatchers("/api/staff/**").permitAll()
//                            .requestMatchers("/api/org/**").permitAll()
                            .antMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                            .antMatchers("/**").permitAll()
                            .anyRequest()
                            .authenticated()
                            .and()
                            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                } catch (Exception e) {
                    throw new RuntimeException("Exception occurred during authorization configuration.", e);
                }
            })
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
            .authenticationProvider(authenticationProvider())
            .build();
}

@Bean
public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
}

}