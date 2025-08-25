package com.crediya.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Log4j2
@Configuration
//@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity
public class AuthorizationJwt implements WebFluxConfigurer {

    private final String issuerUri;
    private final String clientId;
    private final String jsonExpRoles;

    private final ObjectMapper mapper;
    private static final String ROLE = "ROLE_";
    private static final String AZP = "azp";

    public AuthorizationJwt(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri,
                         @Value("${spring.security.oauth2.resourceserver.jwt.client-id}") String clientId,
                         @Value("${jwt.json-exp-roles}") String jsonExpRoles,
                         ObjectMapper mapper) {
        this.issuerUri = issuerUri;
        this.clientId = clientId;
        this.jsonExpRoles = jsonExpRoles;
        this.mapper = mapper;
    }

//    @Bean
//    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
//        http
//            .authorizeExchange(authorize -> authorize.anyExchange().permitAll()) // Permitir todas las solicitudes temporalmente
//            .oauth2ResourceServer(oauth2 ->
//                    oauth2.jwt(jwtSpec ->
//                            jwtSpec
//                            .jwtDecoder(jwtDecoder())
//                            .jwtAuthenticationConverter(grantedAuthoritiesExtractor())
//                    )
//            );
//        return http.build();
//    }
//
//    public ReactiveJwtDecoder jwtDecoder() {
//        var defaultValidator = JwtValidators.createDefaultWithIssuer(issuerUri);
//        var audienceValidator = new JwtClaimValidator<String>(AZP,
//                azp -> azp != null && !azp.isEmpty() && azp.equals(clientId));
//        var tokenValidator = new DelegatingOAuth2TokenValidator<>(defaultValidator, audienceValidator);
//        var jwtDecoder = NimbusReactiveJwtDecoder
//                .withIssuerLocation(issuerUri)
//                .build();
//
//        jwtDecoder.setJwtValidator(tokenValidator);
//        return jwtDecoder;
//    }
//
//    public Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
//        var jwtConverter = new JwtAuthenticationConverter();
//        jwtConverter.setJwtGrantedAuthoritiesConverter(jwt ->
//                getRoles(jwt.getClaims(), jsonExpRoles)
//                .stream()
//                .map(ROLE::concat)
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList()));
//        return new ReactiveJwtAuthenticationConverterAdapter(jwtConverter);
//    }
//
//    private List<String> getRoles(Map<String, Object> claims, String jsonExpClaim){
//        List<String> roles = List.of();
//        try {
//            var json = mapper.writeValueAsString(claims);
//            var chunk = mapper.readTree(json).at(jsonExpClaim);
//            return mapper.readerFor(new TypeReference<List<String>>() {})
//                    .readValue(chunk);
//        } catch (IOException e) {
//            log.error(e.getMessage());
//            return roles;
//        }
//    }
}
