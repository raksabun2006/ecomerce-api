package co.istad.iteecomerc.feature.userprofile;

import co.istad.iteecomerc.feature.userprofile.dto.UserProfileResponse;
import org.keycloak.admin.client.resource.UserPoliciesResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public abstract class UserProfileMapper {

    public UserProfileResponse mapUserRepresentationToUserProfileResponse(UserRepresentation userRepresentation) {
        return UserProfileResponse.builder()
                .userId(userRepresentation.getId())
                .username(userRepresentation.getUsername())
                .email(userRepresentation.getEmail())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .phoneNumber(userRepresentation.getAttributes().get("phoneNumber").getFirst())
                .build();
    }
}
