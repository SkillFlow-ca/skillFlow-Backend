package com.skillflow.skillflowbackend.service;

import com.paypal.base.rest.PayPalRESTException;
import com.skillflow.skillflowbackend.model.PaymentSkillFlow;

public interface PaymentIService {
    public String createPaymentAndReturnApprovalUrl(long idPanier) throws PayPalRESTException ;
    public void confirmPayment(String paymentId, String payerId) throws PayPalRESTException ;

    }
