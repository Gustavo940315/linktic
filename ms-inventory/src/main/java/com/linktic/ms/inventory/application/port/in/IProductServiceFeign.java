package com.linktic.ms.inventory.application.port.in;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MS-PRODUCTS")
public interface IProductServiceFeign {

    @GetMapping("api/v1/product/{id}")
    ResponseEntity<?> getById(@PathVariable long id);
}
