import {Injectable, OnApplicationShutdown, OnModuleInit} from "@nestjs/common";
import {Consumer, ConsumerRunConfig, ConsumerSubscribeTopics, Kafka} from "kafkajs";
import * as process from "node:process";

@Injectable()
export class ConsumerService implements OnApplicationShutdown {

    private readonly kafka: Kafka = new Kafka({
        brokers: process.env.BROKERS.split(',')
    });

    private consumers: Consumer[] = [];

    async consume(topic: ConsumerSubscribeTopics, config: ConsumerRunConfig){
        const consumer = this.kafka.consumer({groupId: 'report-service'});
        await consumer.connect();
        await consumer.subscribe(topic);
        await consumer.run(config);
        this.consumers.push(consumer);
    }

    async onApplicationShutdown() {
        for(const consumer of this.consumers) {
            await consumer.disconnect();
        }
    }

}