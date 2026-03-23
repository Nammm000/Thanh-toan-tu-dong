package tech.getarrays.inventorymanager.services.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import tech.getarrays.inventorymanager.constents.PaymentConstants;

import static tech.getarrays.inventorymanager.util.AuthenticationCodeUtil.hmacSHA256;

@Service
public class MomoPaymentService {

    public Map<String, Object> createPayment(Map<String, String> request) throws Exception {

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
}
