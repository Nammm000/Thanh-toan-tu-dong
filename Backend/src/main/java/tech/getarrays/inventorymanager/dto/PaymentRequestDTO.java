package tech.getarrays.inventorymanager.dto;

import lombok.Data;



@Data
public class PaymentRequestDTO {

    public enum PaymentMethod {
        VNPAY,
        MOMO,
        ZALOPAY
    }

    private Long amount;

    private String orderInfo;

    private PaymentMethod method;
}
