package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Component
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final RestTemplate restTemplate = new RestTemplate(); // API Call ke liye

    public KafkaConsumer(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core")
    @Transactional
    public void listen(Transaction transaction) {
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        if (sender != null && recipient != null && sender.getBalance() >= transaction.getAmount()) {
            // 1. API Call karke Incentive mangvao
            Incentive incentive = restTemplate.postForObject("http://localhost:8080/incentive", transaction, Incentive.class);
            float incentiveAmount = (incentive != null) ? incentive.getAmount() : 0;

            // 2. Sender ke paise kato (Sirf amount)
            sender.setBalance(sender.getBalance() - transaction.getAmount());

            // 3. Recipient ko paise do (Amount + Incentive)
            recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

            userRepository.save(sender);
            userRepository.save(recipient);

            // 4. Record save karo (Incentive ke saath)
            TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount);
            transactionRecordRepository.save(record);
        }
    }
}