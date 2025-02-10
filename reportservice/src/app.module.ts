import {Module} from "@nestjs/common";
import { GeneratorModule } from './generator/generator.module';
import {GeneratorControllerV1} from "./generator/api/v1/generatorControllerV1";
import {GeneratorControllerV2} from "./generator/api/v2/generatorControllerV2";
import {KafkaModule} from "./generator/kafka/kafka.module";


@Module({
    imports: [GeneratorModule, KafkaModule],
    controllers: [GeneratorControllerV1, GeneratorControllerV2],
    providers: [GeneratorControllerV2, KafkaModule],
})
export class AppModule {

}