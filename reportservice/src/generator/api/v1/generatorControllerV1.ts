import {Body, Controller, Header, Post, Res} from '@nestjs/common';
import {GeneratorService} from "../../generator.service";
import {Response} from "express";
import {ApiResponse} from "@nestjs/swagger";
import {CompactModel} from "../../models/compact";

@Controller('api/v1/generator')
export class GeneratorControllerV1 {

    constructor(private generatorService: GeneratorService) {}

    @ApiResponse({status:200, type: String})
    @Post("/generate")
    @Header('content-type', 'application/pdf')
    @Header('content-disposition', 'attachment')
    @Header('filename', 'test.pdf')
    generatePdf(@Res() response: Response, @Body() map: Map<string, Array<CompactModel>>){
        const pdfDoc = this.generatorService.generatePDF(map['posts']);
        pdfDoc.pipe(response);
        pdfDoc.end();
    }

}
