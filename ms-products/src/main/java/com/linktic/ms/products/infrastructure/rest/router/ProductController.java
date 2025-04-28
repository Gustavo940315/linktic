package com.linktic.ms.products.infrastructure.rest.router;

import com.linktic.ms.products.application.dto.ProductDTO;
import com.linktic.ms.products.application.port.in.IProductService;
import com.linktic.ms.products.infrastructure.rest.router.doc.IProductDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController implements IProductDoc {

    private final IProductService productService;

    @Override
    public ResponseEntity<?> createProduct(ProductDTO productDTO) {
        return productService.create(productDTO);
    }

    @Override
    public ResponseEntity<?> getById(long id) {
        return productService.getById(id);
    }

    @Override
    public ResponseEntity<?> update(long id, ProductDTO productDTO) {
        return productService.update(id, productDTO);
    }

    @Override
    public ResponseEntity<?> delete(long id) {
        return productService.delete(id);
    }

    @Override
    public ResponseEntity<?> getByPage(int page, int size) {
        return productService.getByPage(page, size);
    }
}