package co.istad.iteecomerc.feature.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    boolean existsById(Integer id);



    boolean existsByName(String name);


}
