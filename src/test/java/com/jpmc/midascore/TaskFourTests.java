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
public class TaskFourTests {
    static final Logger logger = LoggerFactory.getLogger(TaskFourTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;  // IntelliJ ab isko sahi jagah se uthayega

    @Autowired
    private UserRepository userRepository;

    @Test
    void task_four_verifier() throws InterruptedException {
        userPopulator.populate();
        // Sahi file ka naam:
        String[] transactionLines = fileLoader.loadStrings("/test_data/poiuytrewq.uiop");

        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        // API Call aur processing ke liye wait karo
        Thread.sleep(10000);

        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");

        // Wilbur ka Balance Check
        Iterable<UserRecord> users = userRepository.findAll();
        for (UserRecord user : users) {
            if ("wilbur".equals(user.getName())) {
                logger.info("!!! YOUR ANSWER IS HERE !!!");
                logger.info("User: " + user.getName());
                logger.info("Balance: " + user.getBalance());
                logger.info("!!! ------------------- !!!");
            }
        }

        logger.info("----------------------------------------------------------");
        logger.info("----------------------------------------------------------");
    }
}