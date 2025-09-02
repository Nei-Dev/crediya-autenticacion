package com.crediya.api.config;

import com.crediya.api.constants.paths.AuthPath;
import com.crediya.api.constants.paths.UserPath;
import com.crediya.model.user.UserRole;
import com.crediya.model.user.gateways.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.HttpMethod.POST;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityFilter implements WebFluxConfigurer {
    
    private static final String BEARER = "Bearer ";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String[] ALLOWED_PATHS_SWAGGER = {
        "/v3/api-docs/**",
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/webjars/**"
    };

    private final AuthPath authPath;
    private final UserPath userPath;
    private final TokenService tokenService;

    @Bean
    public SecurityWebFilterChain filterChain(
        ServerHttpSecurity http,
        AuthenticationWebFilter jwtAuthFilter
    ) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .authorizeExchange(authorize -> authorize
                .pathMatchers(authPath.getLogin()).permitAll()
                .pathMatchers(ALLOWED_PATHS_SWAGGER).permitAll()
                .pathMatchers(POST, userPath.getUser()).hasAnyRole(UserRole.MANAGER.name(), UserRole.ADMIN.name())
                .anyExchange().authenticated()
            )
            .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }
    
    @Bean
    public ReactiveAuthenticationManager jwtReactiveAuthenticationManager() {
        return authentication -> {
            String token = authentication.getCredentials().toString();
            return tokenService.validateToken(token)
                .map(userClaims -> {
                    List<GrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority(ROLE_PREFIX.concat(userClaims.role().name()))
                    );
                    return new UsernamePasswordAuthenticationToken(
                        userClaims.email(), null, authorities
                    );
                });
        };
    }
    
    @Bean
    public AuthenticationWebFilter jwtAuthFilter(ReactiveAuthenticationManager jwtReactiveAuthenticationManager) {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(jwtReactiveAuthenticationManager);
        
        filter.setServerAuthenticationConverter(exchange -> {
            String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (header != null && header.startsWith(BEARER)) {
                String token = header.substring(BEARER.length());
                return Mono.just(new UsernamePasswordAuthenticationToken(null, token));
            }
            return Mono.empty();
        });
        
        return filter;
        
    }
//
//    // ✅ Manejador para 401 - No autenticado
//    @Bean
//    public ServerAuthenticationEntryPoint customAuthenticationEntryPoint() {
//        return (exchange, ex) -> {
//            ServerHttpResponse response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            response.getHeaders().add("Content-Type", "application/json");
//
//            String body = """
//                {
//                    "error": "Unauthorized",
//                    "message": "Token de autenticación requerido",
//                    "status": 401
//                }
//                """;
//
//            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes());
//            return response.writeWith(Mono.just(buffer));
//        };
//    }
//
//    // ✅ Manejador para 403 - Sin permisos
//    @Bean
//    public ServerAccessDeniedHandler customAccessDeniedHandler() {
//        return (exchange, denied) -> {
//            ServerHttpResponse response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            response.getHeaders().add("Content-Type", "application/json");
//
//            String body = """
//                {
//                    "error": "Forbidden",
//                    "message": "No tienes permisos para acceder a este recurso",
//                    "status": 403
//                }
//                """;
//
//            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes());
//            return response.writeWith(Mono.just(buffer));
//        };
//    }
}
