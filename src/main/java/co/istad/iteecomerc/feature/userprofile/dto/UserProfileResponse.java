package co.istad.iteecomerc.feature.userprofile.dto;

import lombok.Builder;

@Builder
public record UserProfileResponse(
        String userId,
        String username,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String gender,
        String address,
        String biography,
        String profilePicture
) {
}
