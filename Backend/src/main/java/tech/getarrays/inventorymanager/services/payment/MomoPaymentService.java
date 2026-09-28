package tech.getarrays.inventorymanager.services.payment;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import tech.getarrays.inventorymanager.constents.PaymentConstants;
import tech.getarrays.inventorymanager.dto.PaymentRequestDTO;
import tech.getarrays.inventorymanager.dto.PaymentResponseDTO;

import static tech.getarrays.inventorymanager.util.AuthenticationCodeUtil.hmacSHA256;

@Service
public class MomoPaymentService implements PaymentStrategy {

    public Map<String, Object> createMomoPayment(Map<String, String> request) throws Exception {

        String orderId = UUID.randomUUID().toString();
        String requestId = UUID.randomUUID().toString();
        String amount = request.get("price");
        String orderInfo = request.get("orderInfo");  // nội dung giao dịch thanh toán
        String extraData = ""; // pass empty  if your merchant does not have stores

        String rawHash = "accessKey=" + PaymentConstants.accessMOMOKey +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&ipnUrl=" + PaymentConstants.ipnUrlMOMO +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + PaymentConstants.partnerMOMOCode +
                "&redirectUrl=" + PaymentConstants.redirectUrlResultMOMO +
                "&requestId=" + requestId +
                "&requestType=captureWallet";

        String signature = hmacSHA256(rawHash, PaymentConstants.secretMOMOKey);

        Map<String, Object> body = new HashMap<>();
        body.put("partnerCode", PaymentConstants.partnerMOMOCode);
        body.put("accessKey", PaymentConstants.accessMOMOKey);
        body.put("requestId", requestId);
        body.put("amount", amount);
        body.put("extraData", "");
        body.put("orderId", orderId);
        body.put("orderInfo", orderInfo);
        body.put("redirectUrl", PaymentConstants.redirectUrlResultMOMO);
        body.put("ipnUrl", PaymentConstants.ipnUrlMOMO);
        body.put("requestType", "captureWallet");
        body.put("signature", signature);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(PaymentConstants.MOMOendpoint, requestEntity, Map.class);
    }

    public ResponseEntity<?> handleIpn(Map<String, Object> payload) {

        String resultCode = payload.get("resultCode").toString();

        if ("0".equals(resultCode)) {
            // Payment success
            // Update order status in DB
        } else {
            // Payment failed
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public PaymentRequestDTO.PaymentMethod getMethod() {
        return PaymentRequestDTO.PaymentMethod.MOMO;
    }

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO request, HttpServletRequest httpRequest) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("price", request.getAmount().toString());
        params.put("orderInfo", "Payment for order " + request.getOrderInfo());

        Map<String, Object> response = createMomoPayment(params);
        String payUrl = (String) response.get("payUrl");
        String qrCodeUrl = (String) response.get("qrCodeUrl");

        return new PaymentResponseDTO(payUrl, qrCodeUrl);
    }
}
