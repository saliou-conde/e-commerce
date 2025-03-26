package ch.akros.order.controller.dto;

public record OrderLineResponse(
        Integer id,
        double quantity
) {
}
