package com.skillflow.skillflowbackend.service.serviceImpl;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.skillflow.skillflowbackend.config.PaypalConstants;
import com.skillflow.skillflowbackend.dto.ResponseModel;
import com.skillflow.skillflowbackend.model.*;
import com.skillflow.skillflowbackend.model.enume.PaymentStatus;
import com.skillflow.skillflowbackend.model.enume.StatusENR;
import com.skillflow.skillflowbackend.model.enume.StatusPanier;
import com.skillflow.skillflowbackend.repository.EnrollmentRepository;
import com.skillflow.skillflowbackend.repository.PanierRepository;
import com.skillflow.skillflowbackend.repository.PaymentRepository;
import com.skillflow.skillflowbackend.service.PaymentIService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentIService {
    private final APIContext  apiContext;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PanierRepository panierRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Value("${paypalUrl.cancel-url}")
    private String cancelUrl;

    @Value("${paypalUrl.success-url}")
    private String successUrl;


    public Payment createPayment(Double total,String currency, String method, String intent, String description, String cancelUrl, String successUrl, String invoiceNumber) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(String.format(Locale.forLanguageTag(currency),"%.2f", total)));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setInvoiceNumber(invoiceNumber); // Set the invoice number

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }


    @Override
    public String createPaymentAndReturnApprovalUrl(long idPanier) throws PayPalRESTException {
        Panier panier = panierRepository.findById(idPanier).orElseThrow(() -> new RuntimeException("Panier not found"));
        String invoiceNumber = String.valueOf(idPanier) + "-" + System.currentTimeMillis();

        // Create a payment but do NOT save anything to the database yet
        Payment payment = createPayment(panier.getTotalAmount(), "USD", "paypal",
                "sale", "Payment for courses",
                cancelUrl, successUrl, invoiceNumber);

        // Return the approval URL to redirect the user
        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                return link.getHref();
            }
        }

        throw new RuntimeException("Approval URL not found in PayPal response");
    }


    @Override
    public void confirmPayment(String paymentId, String payerId) throws PayPalRESTException {
        // Execute the payment
        Payment payment = executePayment(paymentId, payerId);

        if (payment.getState().equals("approved")) {
            if (paymentRepository.existsByTransactionId(payment.getId())) {
                throw new RuntimeException("Payment already exists");
            }
            else {
                // Fetch the Panier linked to this payment
                String invoiceNumber = payment.getTransactions().get(0).getInvoiceNumber();
                String[] parts = invoiceNumber.split("-");
                if (parts.length != 2) {
                    throw new RuntimeException("Invalid invoice number format");
                }
                Long panierId = Long.valueOf(parts[0]);
                Panier panier = panierRepository.findById(panierId)
                        .orElseThrow(() -> new RuntimeException("Panier not found"));

                // Save the payment and update the Panier
                PaymentSkillFlow paymentSkillFlow = new PaymentSkillFlow();
                paymentSkillFlow.setPanier(panier);
                paymentSkillFlow.setCreatedAt(Instant.now());
                paymentSkillFlow.setTransactionId(payment.getId());
                paymentSkillFlow.setAmount(panier.getTotalAmount());
                paymentSkillFlow.setPaymentDate(Instant.now());
                paymentSkillFlow.setPaymentStatus(PaymentStatus.PAID);
                panier.setStatusPanier(StatusPanier.PAID);

                // Handle enrollments
                for (Course cc : panier.getCourseList()) {
                    Enrollment enrollment = new Enrollment();
                    enrollment.setUser(panier.getUser());
                    enrollment.setCourse(cc);
                    enrollment.setCreatedAt(Instant.now());
                    enrollment.setEnrollmentDate(Instant.now());
                    enrollment.setStatusEnr(StatusENR.ACTIVE);
                    enrollmentRepository.save(enrollment);
                }

                paymentRepository.save(paymentSkillFlow);
                panierRepository.save(panier);
            }
        } else {
            throw new RuntimeException("Payment not approved");
        }
    }

    @Override
    public ResponseModel<PaymentSkillFlow> getPaymentsForAdmin(Pageable pageable) {
        Page<PaymentSkillFlow> payment = paymentRepository.findAllPayments(pageable);
        return buildResponse(payment);
    }

    private ResponseModel<PaymentSkillFlow> buildResponse(Page<PaymentSkillFlow> payment) {
        List<PaymentSkillFlow> listPayment = payment.toList()
                .stream()
                .collect(Collectors.toList());
        return ResponseModel.<PaymentSkillFlow>builder()
                .pageNo(payment.getNumber())
                .pageSize(payment.getSize())
                .totalElements(payment.getTotalElements())
                .totalPages(payment.getTotalPages())
                .data(listPayment)
                .isLastPage(payment.isLast())
                .build();
    }

}
