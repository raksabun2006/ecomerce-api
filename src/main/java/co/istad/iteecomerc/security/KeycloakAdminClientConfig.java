package co.istad.iteecomerc.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class KeycloakAdminClientConfig {

    private final KeycloakAdminClientProps props;

    @Bean
    public Keycloak keycloakAdminClient() {
        log.info("KeycloakAdminClientProps: {}", props);
        return KeycloakBuilder.builder()
                .serverUrl(props.getServerUrl())
                .realm(props.getTargetRealm()) // <--- Switch from getTargetRealm() to getRealm() ("master")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(props.getClientId())
                .clientSecret(props.getClientSecret())
                .build();
    }

}
