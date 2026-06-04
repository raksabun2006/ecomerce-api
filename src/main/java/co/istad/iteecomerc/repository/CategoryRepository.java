package co.istad.iteecomerc.repository;

import co.istad.iteecomerc.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public interface CategoryRepository extends JpaRepository <Category , Integer>{

}
