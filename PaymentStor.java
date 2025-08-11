import java.util.*;
import java.util.stream.Collectors;

public class TransactionFilter {

    public static List<Transaction> filterLatestVersions(List<Transaction> transactions) {
        // Group by transactionUid, then pick the max version per group
        Map<Integer, Optional<Transaction>> latestById = transactions.stream()
            .collect(Collectors.groupingBy(
                Transaction::getTransactionUid,
                Collectors.maxBy(Comparator.comparing(Transaction::getVersion))
            ));
        
        // Extract the transactions from Optional and collect to a list
        List<Transaction> latestTransactions = latestById.values().stream()
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        
        return latestTransactions;
    }

    public static List<Transaction> filterByCriteria(List<Transaction> transactions, /* criteria params */) {
        // Apply your filtering criteria on the latest transactions
        return transactions.stream()
            .filter(t -> /* your condition, e.g., t.getProcessStatus() == 1 */)
            .collect(Collectors.toList());
    }
}
