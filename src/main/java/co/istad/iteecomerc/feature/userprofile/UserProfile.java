package co.istad.iteecomerc.feature.userprofile;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    private String userId; //from keycloak
    private String gender;
    private String biography;
    private String address;
    private String profilepPicture;

}
