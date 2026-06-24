package co.istad.iteecomerc.feature.product;


import co.istad.iteecomerc.feature.product.dto.CreateProductRequest;
import co.istad.iteecomerc.feature.product.dto.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product mapCreateProductRequestToProduct(CreateProductRequest createProductRequest);

    ProductResponse mapProductToProductResponse(Product product);
}
