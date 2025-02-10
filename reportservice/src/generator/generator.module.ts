import { Module } from '@nestjs/common';
import { GeneratorControllerV1 } from './api/v1/generatorControllerV1';
import { GeneratorService } from './generator.service';
import {GeneratorControllerV2} from "./api/v2/generatorControllerV2";
import {ConsumerService} from "./kafka/consumer.service";
import {ProducerService} from "./kafka/producer.service";

@Module({
  controllers: [GeneratorControllerV1, GeneratorControllerV2],
  providers: [GeneratorService, ConsumerService, ProducerService],
  exports: [GeneratorService],
})
export class GeneratorModule {}
