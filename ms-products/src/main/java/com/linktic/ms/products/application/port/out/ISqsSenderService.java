package com.linktic.ms.products.application.port.out;

import com.linktic.ms.products.application.event.ProductCreatedEvent;

public interface ISqsSenderService {
    void sendProductCreatedEvent(ProductCreatedEvent event);
    void sendProductDeleteEvent(long productId);
}
