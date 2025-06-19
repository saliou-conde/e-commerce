package ch.akros.product.controller.dto;

import java.math.BigDecimal;
import java.util.Comparator;

public record ProductResponse(
        Integer id,
        String name,
        String description,
        Double availableQuantity,
        BigDecimal price,
        String categoryName,
        Integer categoryId
) implements Comparable<ProductResponse> {

    @Override
    public int compareTo(ProductResponse o) {
        return Comparator.nullsFirst(Integer::compareTo).compare(this.id, o.id);
    }
}
