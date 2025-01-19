package com.skillflow.skillflowbackend.controller;

import com.paypal.api.payments.Links;
import com.paypal.base.rest.PayPalRESTException;
import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Blog;
import com.skillflow.skillflowbackend.model.PaymentSkillFlow;
import com.skillflow.skillflowbackend.model.enume.StatusBlog;
import com.skillflow.skillflowbackend.service.PaymentIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "**", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/payment/")
@Validated
@Slf4j
public class PaymentController {
    @Autowired
    private PaymentIService paymentService;
    @PostMapping("/create-payment/{idPanier}")
    public ResponseEntity<String> createPayment(@PathVariable long idPanier) throws PayPalRESTException {
        String approvalUrl = paymentService.createPaymentAndReturnApprovalUrl(idPanier);
        return ResponseEntity.ok(approvalUrl);
    }
    @GetMapping("/confirm-payment")
    public ResponseEntity<String> confirmPayment(@RequestParam("paymentId") String paymentId,
                                                 @RequestParam("PayerID") String payerId) {
        System.out.println("PaymentId: " + paymentId);
        System.out.println("PayerId: " + payerId);
        if (paymentId == null || payerId == null) {
            return ResponseEntity.badRequest().body("PaymentId or PayerId is missing");
        }

        try {
            paymentService.confirmPayment(paymentId, payerId);
            return ResponseEntity.ok("Payment confirmed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error confirming payment: " + e.getMessage());
        }
    }
    @GetMapping("/get-payments")
    public ResponseModel<PaymentSkillFlow> getPaymentsForAdmin(
                                                                   @RequestParam(required = false,defaultValue="1")int pageNo,
                                                                   @RequestParam(required = false,defaultValue="10")int size) {
        Pageable pageRequestData = PageRequest.of(pageNo - 1, size);
        ResponseModel<PaymentSkillFlow> payments = paymentService.getPaymentsForAdmin(pageRequestData);
        return payments;
    }
}
