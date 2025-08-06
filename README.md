public long countUniquePartitionKeys(DynamoDbTable<OrderItem> table, String partitionKeyName) {
    // Create a set to store unique partition key values
    Set<String> uniqueKeys = new HashSet<>();
    
    // Create a scan request
    ScanEnhancedRequest scanRequest = ScanEnhancedRequest.builder()
            .attributesToProject(partitionKeyName)
            .build();
    
    // Execute the scan and collect unique partition keys
    PageIterable<OrderItem> results = table.scan(scanRequest);
    results.items().forEach(item -> uniqueKeys.add(item.getCustomerId()));
    
    return uniqueKeys.size();
}
