package com.dynamodb.dynamodbexample.repository;

import com.dynamodb.dynamodbexample.model.PaymentStor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentStorRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private DynamoDbTable<PaymentStor> paymentStorTable;

    @Autowired
    public PaymentStorRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }

    @PostConstruct
    public void initialize() {
        paymentStorTable = dynamoDbEnhancedClient.table("PaymentStor", TableSchema.fromBean(PaymentStor.class));
        
        // Create the table if it doesn't exist
        try {
            paymentStorTable.describeTable();
        } catch (ResourceNotFoundException e) {
            paymentStorTable.createTable();
        }
    }

    public PaymentStor save(PaymentStor paymentStor) {
        paymentStorTable.putItem(paymentStor);
        return paymentStor;
    }

    public Optional<PaymentStor> findByCifAndTransaction(String cif, String transaction) {
        Key key = Key.builder()
                .partitionValue(cif)
                .sortValue(transaction)
                .build();
        return Optional.ofNullable(paymentStorTable.getItem(key));
    }

    public List<PaymentStor> findAllByCif(String cif) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                .partitionValue(cif)
                .build());
        
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .build();
        
        List<PaymentStor> paymentStors = new ArrayList<>();
        paymentStorTable.query(queryRequest).items().forEach(paymentStors::add);
        
        return paymentStors;
    }

    public List<PaymentStor> findAll() {
        ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder().build();
        PageIterable<PaymentStor> pagedResult = paymentStorTable.scan(scanRequest);
        
        List<PaymentStor> paymentStors = new ArrayList<>();
        pagedResult.items().forEach(paymentStors::add);
        
        return paymentStors;
    }

    public void deleteByCifAndTransaction(String cif, String transaction) {
        Key key = Key.builder()
                .partitionValue(cif)
                .sortValue(transaction)
                .build();
        paymentStorTable.deleteItem(key);
    }

    public boolean existsByCifAndTransaction(String cif, String transaction) {
        return findByCifAndTransaction(cif, transaction).isPresent();
    }
}