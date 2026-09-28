package tech.getarrays.inventorymanager.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.getarrays.inventorymanager.dto.PaymentRequestDTO;
import tech.getarrays.inventorymanager.dto.PaymentResponseDTO;
import tech.getarrays.inventorymanager.services.payment.MomoPaymentService;
import tech.getarrays.inventorymanager.services.payment.VNPayPaymentService;
import tech.getarrays.inventorymanager.services.payment.ZaloPayPaymentService;
import tech.getarrays.inventorymanager.services.payment.PaymentStrategyFactory;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentStrategyFactory paymentStrategyFactory;

    @Autowired
    private MomoPaymentService momoPaymentService;

    @Autowired
    private VNPayPaymentService vnPayPaymentService;

    @Autowired
    private ZaloPayPaymentService zaloPayPaymentService;

    /**
     * Unified payment creation endpoint using Strategy Pattern
     * POST /api/payment/create
     * Body: { "amount": 100000, "orderId": "ORDER123", "method": "MOMO" }
     */
    @PostMapping("/create")
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @RequestBody PaymentRequestDTO request,
            HttpServletRequest httpRequest) {
        try {
            log.info("Creating payment for order {} with method {}", request.getOrderInfo(), request.getMethod());
            PaymentResponseDTO response = paymentStrategyFactory
                    .getStrategy(request.getMethod())
                    .createPayment(request, httpRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Invalid payment method: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error creating payment: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get all available payment methods
     * GET /api/payment/methods
     */
//    @GetMapping("/methods")
//    public ResponseEntity<?> getAvailableMethods() {
//        return ResponseEntity.ok(paymentStrategyFactory.getAvailableMethods());
//    }

    // ========== Legacy endpoints (kept for backward compatibility) ==========

    @Deprecated
    @PostMapping("/createMomo")
    public Map<String, Object> createPaymentMomo(@RequestBody(required = true) Map<String, String> request) throws Exception {
        return momoPaymentService.createMomoPayment(request);
    }

    @PostMapping("/momo/ipn")
    public ResponseEntity<?> handleIpn(@RequestBody Map<String, Object> payload) {
        return momoPaymentService.handleIpn(payload);
    }

    @Deprecated
    @PostMapping("/createVNPay")
    public ResponseEntity<?> createPaymentVNPay(@RequestBody(required = true) Map<String, String> req) throws Exception {
        return vnPayPaymentService.createVNPayPayment(req);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> paymentReturn(HttpServletRequest request) throws Exception {
        return vnPayPaymentService.paymentReturn(request);
    }

    @Deprecated
    @PostMapping("/createZaloPay")
    public Map<String, Object> createOrder(@RequestBody(required = true) Map<String, String> request) throws Exception {
        return zaloPayPaymentService.createZaloPayPayment(request);
    }

    @PostMapping("/callback")
    public ResponseEntity<String> callback(@RequestBody(required = true) Map<String, Object> payload) throws Exception {
        return zaloPayPaymentService.callback(payload);
    }

}
