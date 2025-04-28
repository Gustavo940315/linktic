package com.linktic.ms.products.application.usecase;


import com.linktic.ms.products.application.dto.ProductDTO;
import com.linktic.ms.products.application.event.ProductCreatedEvent;
import com.linktic.ms.products.application.port.in.IProductService;
import com.linktic.ms.products.application.port.out.IProductRepository;
import com.linktic.ms.products.application.port.out.ISqsSenderService;
import com.linktic.ms.products.config.exception.MyHandleException;
import com.linktic.ms.products.model.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@Log4j2
@Service
@AllArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;

    private final ISqsSenderService sqsSenderService;

    @Override
    public ResponseEntity<?> create(ProductDTO productDTO) {
        log.info("Starting creation process for: {}", productDTO.getName());

        validateProductDoesNotExist(productDTO.getName());

        ProductEntity productEntity = mapDtoToEntity(productDTO, null);
        ProductEntity newProduct = productRepository.save(productEntity);

        log.info("Successfully created product with ID: {}", newProduct.getId());

        ProductCreatedEvent event = ProductCreatedEvent.builder()
                .productId(newProduct.getId())
                .quantity(productDTO.getQuantity())
                .build();

        log.info("Sending creation event to SQS");
        sqsSenderService.sendProductCreatedEvent(event);

        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @Override
    public ResponseEntity<?> getById(long id) {
        log.info("Fetching product with ID: {}", id);

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new MyHandleException("Product not found with ID: " + id));

        return ResponseEntity.ok(product);
    }

    @Override
    public ResponseEntity<?> update(long id, ProductDTO productDTO) {
        log.info("[Update Product] Updating product with ID: {}", id);

        productRepository.findById(id)
                .orElseThrow(() -> new MyHandleException("Product not found with ID: " + id));

        ProductEntity productEntity = mapDtoToEntity(productDTO, id);
        productRepository.save(productEntity);

        log.info("[Update Product] Successfully updated product with ID: {}", id);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> delete(long id) {
        log.info("Start process delete product with ID: {}", id);

        productRepository.findById(id)
                .orElseThrow(() -> new MyHandleException("Product not found with ID: " + id));

        productRepository.deleteById(id);

        log.info("Successfully deleted product with ID: {}", id);

        log.info("Sending delete event to SQS");
        sqsSenderService.sendProductDeleteEvent(id);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> getByPage(int page, int size) {
        log.info("[Get Products Page] Fetching page {} with size {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> products = productRepository.findAll(pageable);

        return ResponseEntity.ok(products);
    }

    private void validateProductDoesNotExist(String name) {
        log.debug("Checking existence of product: {}", name);

        productRepository.findByName(name.toLowerCase())
                .ifPresent(product -> {
                    log.warn("Product with name '{}' already exists.", name);
                    throw new MyHandleException("Product with name '" + name + "' already exists.");
                });
    }
    private ProductEntity mapDtoToEntity(ProductDTO dto, Long id) {
        return ProductEntity.builder()
                .id(id)
                .name(dto.getName().toLowerCase())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();
    }
}
