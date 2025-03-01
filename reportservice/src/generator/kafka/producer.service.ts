import {Injectable, OnApplicationShutdown, OnModuleInit} from "@nestjs/common";
import {Kafka, Producer, ProducerRecord} from "kafkajs";
import process from "node:process";

@Injectable()
export class ProducerService implements OnModuleInit, OnApplicationShutdown{

    private readonly kafka: Kafka = new Kafka({
        brokers: process.env.BROKERS.split(',')
    });


    private readonly producer: Producer = this.kafka.producer();

    async onModuleInit() {
        await this.producer.connect();
    }

    async produce(record: ProducerRecord) {
        await this.producer.send(record);
    }

    async onApplicationShutdown() {
        await this.producer.disconnect();
    }
}