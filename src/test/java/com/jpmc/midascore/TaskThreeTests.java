package com.jpmc.midascore;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TaskThreeTests {
    static final Logger logger = LoggerFactory.getLogger(TaskThreeTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private UserRepository userRepository;

    @Test
    void task_three_verifier() throws InterruptedException {
        userPopulator.populate();
        String[] transactionLines = fileLoader.loadStrings("/test_data/mnbvcxz.vbnm");

        // Transactions bhejo
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        // Thoda wait karo processing ke liye
        Thread.sleep(5000);

        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");

        // Waldorf ka balance check karne wala code
        Iterable<UserRecord> users = userRepository.findAll();
        boolean found = false;
        for (UserRecord user : users) {
            if ("waldorf".equals(user.getName())) {
                logger.info("!!! YOUR ANSWER IS HERE !!!");
                logger.info("User: " + user.getName());
                logger.info("Balance: " + user.getBalance());
                logger.info("!!! ------------------- !!!");
                found = true;
            }
        }

        if (!found) {
            logger.info("Waldorf user not found yet.");
        }

        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");
    }
}