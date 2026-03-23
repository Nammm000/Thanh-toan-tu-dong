package tech.getarrays.inventorymanager.services.payment;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tech.getarrays.inventorymanager.constents.PaymentConstants;
import tech.getarrays.inventorymanager.util.zalopay.crypto.HMACUtil;

import java.text.SimpleDateFormat;
import java.util.*;

import static tech.getarrays.inventorymanager.util.TimeUtil.getCurrentVnTimeString;

@Service
public class ZaloPayPaymentService {

    public Map<String, Object> createOrder(Map<String, String> request) throws Exception {

        final JSONObject embed_data = new JSONObject("{\"preferred_payment_method\": [\"zalopay_wallet\"]}"); // "{\"preferred_payment_method\": [\"vietqr\"]}"
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.put("vietqr");
//        embed_data.put("preferred_payment_method", jsonArray);

        final JSONArray items = new JSONArray(); // request2.get("items")

        long appTime = System.currentTimeMillis();
//        String appTransId = getCurrentVnTimeString("yyMMdd") + "_" + appTime;

        Map<String, Object> order = new HashMap<>();
        order.put("app_id", PaymentConstants.APP_ID);
        order.put("app_user", "demo_user");
        order.put("app_trans_id", getCurrentVnTimeString("yyMMdd") + "_" + appTime);
        order.put("app_time", appTime);
        order.put("expire_duration_seconds", 300);
        order.put("amount", Long.parseLong(request.get("price")));
        order.put("item", items.toString());
        order.put("description", "QR Payment #" + order.get("app_trans_id")); // QR Payment | Lazadaa - Thanh toán đơn hàng
        order.put("embed_data", embed_data.toString());
        order.put("bank_code", ""); // zalopayapp VTB
        // Đối với mô hình Thanh toán QR, App to App, thì bank_code là không bắt buộc
        // Đối với mô hình Mobile Web to App, thì bank_code bắt buộc phải là zalopayapp
        // Đối với mô hình Cổng ZaloPay, thì lấy bank_code qua https://sbgateway.zalopay.vn/api/getlistmerchantbanks
//        order.put("callback_url", "https://yourdomain.com/api/payment/callback"); // mac dinh la url zalopay

        String data = PaymentConstants.APP_ID + "|" + order.get("app_trans_id") + "|" + "demo_user" + "|"
                + order.get("amount") + "|" + appTime + "|" + order.get("embed_data") +"|"+ order.get("item");

        String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, PaymentConstants.KEY1, data);
        order.put("mac", mac);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(PaymentConstants.ENDPOINTZALOPAY, order, Map.class);

        return response.getBody();
    }

    public ResponseEntity<String> callback(Map<String, Object> payload) throws Exception {

        String data = (String) payload.get("data");
        String reqMac = (String) payload.get("mac");

        String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, PaymentConstants.KEY2, data);

        if (!mac.equals(reqMac)) {
            return ResponseEntity.badRequest().body("Invalid MAC");
        }

        // ✅ Payment success → update DB
        return ResponseEntity.ok("success");
    }
}
