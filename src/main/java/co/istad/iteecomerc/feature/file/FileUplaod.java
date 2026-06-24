package co.istad.iteecomerc.feature.file;

import com.google.errorprone.annotations.NoAllocation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
public class FileUplaod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false , unique = true)
    private String name ;

    private String caption;

    @Column(nullable = false)
    private Long size;
    @Column(nullable = false)
    private String mediaType;



}
