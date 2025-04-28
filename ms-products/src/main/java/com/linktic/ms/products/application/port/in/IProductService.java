package com.linktic.ms.products.application.port.in;


import com.linktic.ms.products.application.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

public interface IProductService {

    ResponseEntity<?> create(ProductDTO productDTO);

    ResponseEntity<?> getById(long id);

    ResponseEntity<?> update(long id, ProductDTO productDTO);

    ResponseEntity<?> delete(long id);

    ResponseEntity<?> getByPage(int page, int size);

}
