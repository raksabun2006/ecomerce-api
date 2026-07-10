package co.istad.iteecomerc.feature.auth;

import co.istad.iteecomerc.feature.auth.dto.RegisterRequest;
import co.istad.iteecomerc.feature.auth.dto.RegisterResponse;
import co.istad.iteecomerc.feature.userprofile.UserProfile;
import co.istad.iteecomerc.feature.userprofile.UserProfileRepository;
import co.istad.iteecomerc.security.KeycloakAdminClientProps;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private static final String DEFAULT_ROLE = "customer";

    private final Keycloak keycloak;
    private final KeycloakAdminClientProps props;
    private final AuthMapper authMapper;
    private final UserProfileRepository userProfileRepository;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {

        // Validate password matching
        if (!registerRequest.password().equals(registerRequest.confirmedPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Passwords don't match!"
            );
        }

        RealmResource realmResource = keycloak.realm(props.getTargetRealm());
        UsersResource usersResource = realmResource.users();

        // Block duplicate email up front
        List<UserRepresentation> existingByEmail =
                usersResource.searchByEmail(registerRequest.email(), true);
        if (!existingByEmail.isEmpty()) {
            log.info("Registration blocked: email already in use - {}", registerRequest.email());
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already in use: " + registerRequest.email()
            );
        }

        // Build user representation
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(registerRequest.username());
        userRepresentation.setEmail(registerRequest.email());
        userRepresentation.setFirstName(registerRequest.firstName());
        userRepresentation.setLastName(registerRequest.lastName());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        userRepresentation.setRequiredActions(List.of("VERIFY_EMAIL"));

        // Custom attributes
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("phoneNumber", List.of(registerRequest.phoneNumber()));
        userRepresentation.setAttributes(attributes);

        // Credential
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(registerRequest.password());
        credential.setTemporary(false);
        userRepresentation.setCredentials(List.of(credential));

        try (Response response = usersResource.create(userRepresentation)) {
            log.info("Response status code: {}", response.getStatus());

            if (response.getStatus() == HttpStatus.CREATED.value()) {
                String userId = CreatedResponseUtil.getCreatedId(response);
                UserRepresentation createdUser = usersResource.get(userId).toRepresentation();
                log.info("Created user: {}", createdUser.getId());

                try {
                    try {
                        RoleRepresentation customerRole = realmResource.roles()
                                .get(DEFAULT_ROLE)
                                .toRepresentation();

                        usersResource.get(userId)
                                .roles()
                                .realmLevel()
                                .add(List.of(customerRole));

                        log.info("Assigned default role '{}' to user {}", DEFAULT_ROLE, userId);
                    } catch (Exception e) {

                        log.error("Failed to assign default role '{}' to user {}: {}",
                                DEFAULT_ROLE, userId, e.getMessage());
                    }

                    UserProfile userProfile = new UserProfile();
                    userProfile.setUserId(userId);
                    userProfileRepository.save(userProfile);
                    try {
                        usersResource.get(userId).sendVerifyEmail();
                        log.info("Verification email sent to user {}", userId);
                    } catch (Exception e) {
                        log.warn("Failed to send verification email for user {}: {}", userId, e.getMessage());
                    }

                    return authMapper.mapUserRepresentaionToRegisterResponse(createdUser);

                } catch (Exception e) {

                    log.error("Failed to persist local user profile, rolling back Keycloak user {}", userId, e);
                    try {
                        usersResource.get(userId).remove();
                        log.info("Rolled back Keycloak user {}", userId);
                    } catch (Exception cleanupEx) {
                        log.error("CRITICAL: failed to clean up orphaned Keycloak user {}", userId, cleanupEx);
                    }
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Failed to complete registration"
                    );
                }

            } else if (response.getStatus() == HttpStatus.CONFLICT.value()) {
                log.info("Username or email already exists!");
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Username or email already exists!"
                );

            } else {
                log.error("Unexpected error creating user, status: {}", response.getStatus());
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to create user, status: " + response.getStatus()
                );
            }
        }
    }
}