package co.istad.iteecomerc.feature.product;

import co.istad.iteecomerc.feature.category.Category;
import co.istad.iteecomerc.feature.category.CategoryRepository;
import co.istad.iteecomerc.feature.product.dto.CreateProductRequest;
import co.istad.iteecomerc.feature.product.dto.ProductResponse;
import co.istad.iteecomerc.utils.GenerateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;  

    @Override
    public ProductResponse createNew(CreateProductRequest createProductRequest) {

        if (productRepository.existsProductByName(createProductRequest.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Product name has already been used");
        }

        Category category = categoryRepository
                .findById(createProductRequest.parentCategory())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Category has not been found"));
        Product product = productMapper
                .mapCreateProductRequestToProduct(createProductRequest);

        product.setCategory(category);
        product.setCode(GenerateUtils.generateProductCode());
        product.setSlug(GenerateUtils.generateSlug(createProductRequest.name()));
        product.setIsAvailable(true);
        product.setIsDelete(false);

        product = productRepository.save(product);

        return productMapper.mapProductToProductResponse(product);
    }

    @Override
    public Page<ProductResponse> findAll(int pageNumber, int pageSize) {
        Sort sortById = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sortById);

        Page<Product> products = productRepository.findAll(pageRequest);

        return products.map(productMapper::mapProductToProductResponse);
    }

    @Override
    public void deleteByCode(String code) {
        Product product = productRepository.findByCode(code)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND
                        , "Product has been delete"));
        productRepository.save(product);
    }

    @Override
    public ProductResponse updateByCode(String code, CreateProductRequest request) {
        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found!"));

        product.setName(request.name());
        product.setUnitPrice(request.unitPrice());
        product.setDescription(request.description());
        product.setQty(request.qty());
        Product updatedProduct = productRepository.save(product);

        return productMapper.mapProductToProductResponse(updatedProduct);
    }

}