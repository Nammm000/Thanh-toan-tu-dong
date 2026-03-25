package tech.getarrays.inventorymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponseDTO {

    private String paymentUrl;

    private String qrCode;

}
