package tech.getarrays.inventorymanager.services.payment;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.getarrays.inventorymanager.constents.PaymentConstants;
import static tech.getarrays.inventorymanager.util.AuthenticationCodeUtil.hmacSHA512;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayPaymentService {

    public ResponseEntity<?> createPayment(Map<String, String> req) throws Exception {

        String orderId = String.valueOf(System.currentTimeMillis());
        String amount = req.get("price"); // VND
        String orderInfo = req.get("orderInfo");

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", PaymentConstants.VERSION);
        vnpParams.put("vnp_Command", PaymentConstants.COMMAND);
        vnpParams.put("vnp_TmnCode", PaymentConstants.VNP_TMN_CODE);
        vnpParams.put("vnp_Amount", amount);
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", orderId);
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang " + orderId + orderInfo);
        vnpParams.put("vnp_OrderType", PaymentConstants.ORDER_TYPE);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", PaymentConstants.VNP_RETURN_URL);
        vnpParams.put("vnp_IpAddr", "13.160.92.202"); // HttpServletRequest req.getRemoteAddr()

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        vnpParams.put("vnp_CreateDate", formatter.format(cld.getTime()));
        cld.add(Calendar.MINUTE, 15);
        vnpParams.put("vnp_ExpireDate", formatter.format(cld.getTime()));

        // Sort params
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        String lastFieldName = fieldNames.get(fieldNames.size() - 1);
        for (String fieldName : fieldNames) {
            String value = vnpParams.get(fieldName);
            if (value != null && value.length() > 0) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=')
                        .append(URLEncoder.encode(value, StandardCharsets.US_ASCII));

                if (!fieldName.equals(lastFieldName)) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        String secureHash = hmacSHA512(PaymentConstants.VNP_SECRET_KEY, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        String paymentUrl = PaymentConstants.VNP_PAY_URL + "?" + query.toString();

        return ResponseEntity.ok(paymentUrl);
    }

    public ResponseEntity<?> paymentReturn(HttpServletRequest request) throws Exception {

        Map<String, String> fields = new HashMap<>();

        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnpSecureHash = fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String name = fieldNames.get(i);
            String value = fields.get(name);

            hashData.append(name).append('=')
                    .append(URLEncoder.encode(value, StandardCharsets.US_ASCII));

            if (i < fieldNames.size() - 1) {
                hashData.append('&');
            }
        }

        String calculatedHash = hmacSHA512(PaymentConstants.VNP_SECRET_KEY, hashData.toString());

        if (!calculatedHash.equalsIgnoreCase(vnpSecureHash)) {
            return ResponseEntity.badRequest().body("Invalid signature ❌");
        }

        // Signature valid → now check payment status
        String responseCode = fields.get("vnp_ResponseCode");

        if ("00".equals(responseCode)) {
            return ResponseEntity.ok("Payment Success ✅");
        } else {
            return ResponseEntity.badRequest().body("Payment Failed ❌");
        }
    }
}
