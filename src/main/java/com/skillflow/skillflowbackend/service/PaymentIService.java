package com.skillflow.skillflowbackend.service;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.Job;
import com.skillflow.skillflowbackend.model.PaymentSkillFlow;
import org.springframework.data.domain.Pageable;

public interface PaymentIService {
    public String createPaymentAndReturnApprovalUrl(long idPanier) throws PayPalRESTException ;
    public void confirmPayment(String paymentId, String payerId) throws PayPalRESTException ;

    public ResponseModel<PaymentSkillFlow> getPaymentsForAdmin(Pageable pageable);

    }
