package com.example.jobSeaching.controller;

import com.example.jobSeaching.config.VNPayConfig;
import com.example.jobSeaching.entity.MembershipOrder;
import com.example.jobSeaching.entity.enums.MembershipType;
import com.example.jobSeaching.entity.enums.PaymentStatus;
import com.example.jobSeaching.repository.MembershipOrderRepository;
import com.example.jobSeaching.repository.MembershipRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/vnpay")
public class VNPayController {

    private final MembershipOrderRepository membershipOrderRepository;

    public VNPayController(MembershipOrderRepository membershipOrderRepository) {
        this.membershipOrderRepository = membershipOrderRepository;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create_payment_url")
    public Map<String, Object> createPaymentUrl(
            @RequestParam("membershipType") String membershipTypeStr,
            @RequestParam(value = "bankCode", required = false) String bankCode,
            @RequestParam(value = "language", required = false, defaultValue = "vn") String language,
            HttpServletRequest request
    ) throws Exception {
        // Parse membershipType từ string
        MembershipType membershipType;
        try {
            membershipType = MembershipType.valueOf(membershipTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("MembershipType không hợp lệ");
        }

        int amount = membershipType.getPrice();

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long vnp_Amount = amount * 100L;
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        // Lưu MembershipOrder (inject repository trước)
        MembershipOrder order = new MembershipOrder();
        order.setTxnRef(vnp_TxnRef);
        order.setAmount(amount);
        order.setStatus(PaymentStatus.PENDING);
        order.setMembershipType(membershipType);
        // TODO: set user hiện tại
        order.setCreatedAt(LocalDateTime.now());
        membershipOrderRepository.save(order);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(vnp_Amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", language);
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cal.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cal.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cal.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Tạo chuỗi hashData và query
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + query;

        Map<String, Object> res = new HashMap<>();
        res.put("code", "00");
        res.put("message", "success");
        res.put("data", paymentUrl);
        return res;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/vnpay-return")
    public ResponseEntity<String> vnpayReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String paramName = params.nextElement();
            fields.put(paramName, request.getParameter(paramName));
        }

        String vnp_SecureHash = fields.remove("vnp_SecureHash");
        String generatedHash = VNPayConfig.hashAllFields(fields);

        if (vnp_SecureHash != null && vnp_SecureHash.equals(generatedHash)) {
            String responseCode = fields.get("vnp_ResponseCode");
            String txnRef = fields.get("vnp_TxnRef");

            MembershipOrder order = membershipOrderRepository.findByTxnRef(txnRef).orElse(null);
            if (order == null) {
                return ResponseEntity.status(404).body("Đơn hàng không tồn tại");
            }

            if ("00".equals(responseCode)) {
                order.setStatus(PaymentStatus.PENDING); // Đã thanh toán, chờ admin duyệt
                membershipOrderRepository.save(order);

                return ResponseEntity.ok("Thanh toán thành công. Vui lòng đợi Admin duyệt gói.");
            } else {
                order.setStatus(PaymentStatus.FAILED);
                membershipOrderRepository.save(order);
                return ResponseEntity.status(400).body("Thanh toán thất bại. Mã lỗi: " + responseCode);
            }
        } else {
            return ResponseEntity.status(403).body("Chuỗi kiểm tra sai. Không xác thực được nguồn gửi.");
        }
}
}
