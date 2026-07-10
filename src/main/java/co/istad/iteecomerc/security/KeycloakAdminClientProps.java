package co.istad.iteecomerc.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keycloak")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class KeycloakAdminClientProps {
    private String serverUrl;
    private String realm;
    private String targetRealm;
    private String clientId;
    private String clientSecret;
}