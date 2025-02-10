package org.d3javu.bd.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.d3javu.bd.models.post.PostForReport;
import org.d3javu.bd.service.PostService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class KafkaService {

    private final PostService postService;
    private final KafkaProducer<String, List<PostForReport>> kafkaProducer;
    private final KafkaConsumer<String, byte[]> kafkaConsumer;

    public KafkaService(PostService postService, KafkaProducer<String, List<PostForReport>> kafkaProducer, KafkaConsumer<String, byte[]> kafkaConsumer) {
        this.postService = postService;
        this.kafkaProducer = kafkaProducer;
        this.kafkaConsumer = kafkaConsumer;
        this.kafkaConsumer.subscribe(List.of("pdfResponse"));
    }

    public byte[] getPdfOfTopN(Integer n){
        var posts = this.postService.findTopN(n);
        this.kafkaProducer.send(new ProducerRecord<>("pdfRequest", posts));
        var pdf = this.kafkaConsumer.poll(Duration.ofMillis(5000));
        System.out.println();
        return StreamSupport.stream(pdf.spliterator(), false)
                .map(ConsumerRecord::value)
                .toList()
                .get(0);
    }

}
