package tech.getarrays.inventorymanager.services.payment;

import jakarta.servlet.http.HttpServletRequest;
import tech.getarrays.inventorymanager.dto.PaymentRequestDTO;
import tech.getarrays.inventorymanager.dto.PaymentResponseDTO;

public interface PaymentStrategy {

    PaymentRequestDTO.PaymentMethod getMethod();

    PaymentResponseDTO createPayment(
            PaymentRequestDTO request,
            HttpServletRequest httpRequest
    ) throws Exception;

}