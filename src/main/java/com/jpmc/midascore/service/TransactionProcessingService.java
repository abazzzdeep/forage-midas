package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionProcessingService {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final RestTemplate restTemplate;

    public TransactionProcessingService(
            UserRepository userRepository,
            TransactionRecordRepository transactionRecordRepository,
            RestTemplate restTemplate
    ) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public void process(Transaction transaction) {

        // 🔹 Load users
        UserRecord sender = userRepository
                .findById(transaction.getSenderId())
                .orElseThrow();

        UserRecord recipient = userRepository
                .findById(transaction.getRecipientId())
                .orElseThrow();

        // 🔹 Call Incentive API
        Incentive incentive = restTemplate.postForObject(
                "http://localhost:8080/incentive",
                transaction,
                Incentive.class
        );

        float incentiveAmount = (incentive != null) ? incentive.getAmount() : 0f;

        // 🔹 Update balances
        // Sender loses ONLY transaction amount
        sender.setBalance(sender.getBalance() - transaction.getAmount());

        // Recipient gains transaction + incentive
        recipient.setBalance(
                recipient.getBalance()
                        + transaction.getAmount()
                        + incentiveAmount
        );

        // 🔹 Persist users
        userRepository.save(sender);
        userRepository.save(recipient);

        // 🔹 Record transaction (store incentive separately if field exists)
        TransactionRecord record = new TransactionRecord(
                transaction.getSenderId(),
                transaction.getRecipientId(),
                transaction.getAmount(),
                incentiveAmount
        );

        transactionRecordRepository.save(record);
    }
}
