package co.istad.iteecomerc.feature.product;

import co.istad.iteecomerc.feature.product.dto.CreateProductRequest;
import co.istad.iteecomerc.feature.product.dto.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService service;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createNew(@Valid @RequestBody CreateProductRequest request){
        return service.createNew(request);
    }

    @GetMapping
    public Page<ProductResponse> findAll(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "25") int pageSize
    ) {
        return service.findAll(pageNumber, pageSize);
    }
}
