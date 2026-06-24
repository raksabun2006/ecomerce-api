package co.istad.iteecomerc.feature.product;

import co.istad.iteecomerc.feature.product.dto.CreateProductRequest;
import co.istad.iteecomerc.feature.product.dto.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    /**
     * create new Product
     * @param createProductRequest requesting for creating product
     * @return ProductResponse
     * @author bun_raksa_student_ite-gen3_istad
     * @since 23-06-2026
     */

    ProductResponse createNew(CreateProductRequest createProductRequest);

    /**
     *
     * @param pageNumber
     * @param pageSize
     * @author bun_raksa_ite-gen3
     * @return
     */
    Page<ProductResponse> findAll(int pageNumber , int pageSize);
}
