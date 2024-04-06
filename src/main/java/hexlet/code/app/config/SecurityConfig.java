package hexlet.code.app.config;

import hexlet.code.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import jakarta.servlet.DispatcherType;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            HandlerMappingIntrospector introspector) throws Exception {
        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
//                        .requestMatchers("/index.html", "/", "/assets/**", "/swagger-ui/**").permitAll()
//                        .requestMatchers("/welcome", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
//                        .anyRequest().authenticated())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .oauth2ResourceServer((rs) -> rs.jwt((jwt) -> jwt.decoder(jwtDecoder)))
//                .httpBasic(Customizer.withDefaults())
//                .build();


                .anonymous(AbstractHttpConfigurer::disable)         // AnonymousAuthenticationFilter
                .csrf(AbstractHttpConfigurer::disable)              // CsrfFilter
                .sessionManagement(AbstractHttpConfigurer::disable) // DisableEncodeUrlFilter, SessionManagementFilter
                .exceptionHandling(AbstractHttpConfigurer::disable) // ExceptionTranslationFilter
                .headers(AbstractHttpConfigurer::disable)           // HeaderWriterFilter
                .logout(AbstractHttpConfigurer::disable)            // LogoutFilter
                .requestCache(AbstractHttpConfigurer::disable)      // RequestCacheAwareFilter
                .servletApi(AbstractHttpConfigurer::disable)        // SecurityContextHolderAwareRequestFilter
                .securityContext(AbstractHttpConfigurer::disable)   // SecurityContextPersistenceFilter
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    @Bean
    public AuthenticationProvider daoAuthProvider(AuthenticationManagerBuilder auth) {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
