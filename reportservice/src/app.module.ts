import {Module} from "@nestjs/common";
import { GeneratorModule } from './generator/generator.module';


@Module({
    imports: [GeneratorModule],
    // imports: [MockData]
})
export class AppModule {

}