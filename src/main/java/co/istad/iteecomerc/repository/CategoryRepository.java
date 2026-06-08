package co.istad.iteecomerc.repository;

import co.istad.iteecomerc.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);

    List<Category> findByParentCategory_Id(Integer parentId);
}