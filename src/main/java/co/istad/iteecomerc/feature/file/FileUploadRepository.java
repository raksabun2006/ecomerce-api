package co.istad.iteecomerc.feature.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileUploadRepository extends JpaRepository<FileUplaod , Long> {
    List<FileUplaod> findByName(String name);

    boolean existsByName(String name);
}
