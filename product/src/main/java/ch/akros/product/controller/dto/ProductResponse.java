package ch.akros.product.controller.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Integer id,
        String name,
        String description,
        Double availableQuantity,
        BigDecimal price,
        String categoryName,
        Integer categoryId
) {
}
