package co.istad.iteecomerc.feature.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileUploadRepository extends JpaRepository<FileUplaod , Long> {


    boolean existsByName(String name);
    Optional<FileUplaod> findByName(String name);
}
