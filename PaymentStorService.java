package com.dynamodb.dynamodbexample.service;

import com.dynamodb.dynamodbexample.model.PaymentStor;
import com.dynamodb.dynamodbexample.repository.PaymentStorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentStorService {

    private final PaymentStorRepository paymentStorRepository;

    @Autowired
    public PaymentStorService(PaymentStorRepository paymentStorRepository) {
        this.paymentStorRepository = paymentStorRepository;
    }

    public PaymentStor createPaymentStor(PaymentStor paymentStor) {
        if (paymentStor.getTransaction() == null || paymentStor.getTransaction().isEmpty()) {
            paymentStor.setTransaction(UUID.randomUUID().toString());
        }
        return paymentStorRepository.save(paymentStor);
    }

    public Optional<PaymentStor> getPaymentStorByCifAndTransaction(String cif, String transaction) {
        return paymentStorRepository.findByCifAndTransaction(cif, transaction);
    }

    public List<PaymentStor> getAllPaymentStorsByCif(String cif) {
        return paymentStorRepository.findAllByCif(cif);
    }

    public List<PaymentStor> getAllPaymentStors() {
        return paymentStorRepository.findAll();
    }

    public PaymentStor updatePaymentStor(String cif, String transaction, PaymentStor paymentStorDetails) {
        Optional<PaymentStor> optionalPaymentStor = paymentStorRepository.findByCifAndTransaction(cif, transaction);
        if (optionalPaymentStor.isPresent()) {
            PaymentStor existingPaymentStor = optionalPaymentStor.get();
            // Only update the data field, cif and transaction are keys and shouldn't be changed
            existingPaymentStor.setData(paymentStorDetails.getData());
            return paymentStorRepository.save(existingPaymentStor);
        }
        return null;
    }

    public boolean deletePaymentStor(String cif, String transaction) {
        if (paymentStorRepository.existsByCifAndTransaction(cif, transaction)) {
            paymentStorRepository.deleteByCifAndTransaction(cif, transaction);
            return true;
        }
        return false;
    }
}