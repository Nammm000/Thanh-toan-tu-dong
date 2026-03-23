package tech.getarrays.inventorymanager.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private long price;
    private String orderInfo;
}
