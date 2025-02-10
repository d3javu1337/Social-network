import {Controller, OnModuleInit} from "@nestjs/common";
import {GeneratorService} from "../../generator.service";
import {ConsumerService} from "../../kafka/consumer.service";
import {CompactModel} from "../../models/compact";
import {ProducerService} from "../../kafka/producer.service";
import * as fs from "node:fs";

@Controller()
export class GeneratorControllerV2 implements OnModuleInit {

    constructor(private generatorService: GeneratorService,
                private consumerService: ConsumerService,
                private producerService: ProducerService) {}


    async onModuleInit(){
        console.log(123);
        await this.consumerService.consume(
            { topics: ['pdfRequest'] },
            {
                eachMessage: async ({topic, partition, message}) => {
                    let msg = message.value;
                    let decoded = msg.toString();
                    let shit = this.generatorService.generatePDF(this.convert(decoded));

                    await this.savePdfToFile(shit, '123.pdf');

                    await this.producerService.produce({
                        topic: 'pdfResponse',
                        messages:[{
                            value: Buffer.from(await new Blob(await fs.createReadStream('./123.pdf').toArray()).arrayBuffer())
                        }]
                    });
                    fs.rm('./123.pdf', (err) => {console.log(err)});
                }
            }
        )
    }



    convert(data: string): CompactModel[] {
        let arr: CompactModel[] = [];
        let parsed = JSON.parse(data);
        for (let item in parsed) {
            let cm =  new CompactModel();
            cm.id = parsed[item]['id'];
            cm.title = parsed[item]['title'];
            cm.body = parsed[item]['body'];
            cm.createdAt = parsed[item]['createdAt'];
            cm.likesCount = parsed[item]['likesCount'];
            cm.postLink = parsed[item]['postLink'];
            cm.authorFirstName = parsed[item]['authorFirstName'];
            cm.authorLastName = parsed[item]['authorLastName'];
            arr.push(cm);
        }
        return arr;
    }

    savePdfToFile(pdf : PDFKit.PDFDocument, fileName : string) : Promise<void> {
        return new Promise<void>((resolve, reject) => {

            let pendingStepCount = 2;

            const stepFinished = () => {
                if (--pendingStepCount == 0) {
                    resolve();
                }
            };

            const writeStream = fs.createWriteStream(fileName);
            writeStream.on('close', stepFinished);
            pdf.pipe(writeStream);

            pdf.end();

            stepFinished();
        });
    }

}