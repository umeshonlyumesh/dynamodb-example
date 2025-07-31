package com.dynamodb.dynamodbexample.controller;

import com.dynamodb.dynamodbexample.model.PaymentStor;
import com.dynamodb.dynamodbexample.service.PaymentStorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentStorController {

    private final PaymentStorService paymentStorService;

    @Autowired
    public PaymentStorController(PaymentStorService paymentStorService) {
        this.paymentStorService = paymentStorService;
    }

    @PostMapping
    public ResponseEntity<PaymentStor> createPaymentStor(@RequestBody PaymentStor paymentStor) {
        PaymentStor createdPaymentStor = paymentStorService.createPaymentStor(paymentStor);
        return new ResponseEntity<>(createdPaymentStor, HttpStatus.CREATED);
    }

    @GetMapping("/{cif}/{transaction}")
    public ResponseEntity<PaymentStor> getPaymentStorByCifAndTransaction(
            @PathVariable String cif,
            @PathVariable String transaction) {
        Optional<PaymentStor> paymentStor = paymentStorService.getPaymentStorByCifAndTransaction(cif, transaction);
        return paymentStor.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{cif}")
    public ResponseEntity<List<PaymentStor>> getAllPaymentStorsByCif(@PathVariable String cif) {
        List<PaymentStor> paymentStors = paymentStorService.getAllPaymentStorsByCif(cif);
        return new ResponseEntity<>(paymentStors, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PaymentStor>> getAllPaymentStors() {
        List<PaymentStor> paymentStors = paymentStorService.getAllPaymentStors();
        return new ResponseEntity<>(paymentStors, HttpStatus.OK);
    }

    @PutMapping("/{cif}/{transaction}")
    public ResponseEntity<PaymentStor> updatePaymentStor(
            @PathVariable String cif,
            @PathVariable String transaction,
            @RequestBody PaymentStor paymentStorDetails) {
        PaymentStor updatedPaymentStor = paymentStorService.updatePaymentStor(cif, transaction, paymentStorDetails);
        if (updatedPaymentStor != null) {
            return new ResponseEntity<>(updatedPaymentStor, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{cif}/{transaction}")
    public ResponseEntity<Void> deletePaymentStor(
            @PathVariable String cif,
            @PathVariable String transaction) {
        boolean deleted = paymentStorService.deletePaymentStor(cif, transaction);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}