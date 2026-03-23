package tech.getarrays.inventorymanager.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//import tech.getarrays.inventorymanager.dto.PaymentRequestDTO;
//import tech.getarrays.inventorymanager.filters.JwtRequestFilter;
import tech.getarrays.inventorymanager.services.payment.MomoPaymentService;
import tech.getarrays.inventorymanager.services.payment.VNPayPaymentService;
import tech.getarrays.inventorymanager.services.payment.ZaloPayPaymentService;

//import java.util.HashMap;
import java.util.Map;
//import java.util.UUID;

//import static tech.getarrays.inventorymanager.util.AuthenticationCodeUtil.hmacSHA256;

@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    MomoPaymentService momoPaymentService;

    @Autowired
    VNPayPaymentService vnPayPaymentService;

    @Autowired
    ZaloPayPaymentService zaloPayPaymentService;

    @PostMapping("/createMomo")
    public Map<String, Object> createPaymentMomo(@RequestBody(required = true) Map<String, String> request) throws Exception {

        return momoPaymentService.createPayment(request);
    }

    @PostMapping("/momo/ipn")
    public ResponseEntity<?> handleIpn(@RequestBody Map<String, Object> payload) {

        return momoPaymentService.handleIpn(payload);
    }

    @PostMapping("/createVNPay")
    public ResponseEntity<?> createPaymentVNPay(@RequestBody(required = true) Map<String, String> req) throws Exception {

        return vnPayPaymentService.createPayment(req);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> paymentReturn(HttpServletRequest request) throws Exception {

        return vnPayPaymentService.paymentReturn(request);
    }

    @PostMapping("/createZaloPay")
    public Map<String, Object> createOrder(@RequestBody(required = true) Map<String, String> request) throws Exception {

        return zaloPayPaymentService.createOrder(request);
    }

    @PostMapping("/callback")
    public ResponseEntity<String> callback(@RequestBody(required = true) Map<String, Object> payload) throws Exception {

        return zaloPayPaymentService.callback(payload);
    }

}
