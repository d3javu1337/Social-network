import {Injectable} from '@nestjs/common';
import {CompactModel} from "./models/compact";
import {TDocumentDefinitions, TFontDictionary} from "pdfmake/interfaces";
import PdfPrinter from "pdfmake";

@Injectable()
export class GeneratorService {

    generatePDF(posts: CompactModel[]) :PDFKit.PDFDocument{
        const fonts = {
            Roboto: {
                normal: 'fonts/Roboto-Regular.ttf',
                bold: 'fonts/Roboto-Medium.ttf',
                italics: 'fonts/Roboto-Italic.ttf',
                bolditalics: 'fonts/Roboto-MediumItalic.ttf'
            }
        } as TFontDictionary;

        const printer = new PdfPrinter(fonts);
        let docDefinition = {content: []} as TDocumentDefinitions;

        let data = [];

        for (let i = 0; i < posts.length; i++) {
            let post = posts[i];
            data.push({text : post.authorFirstName + ' ' + post.authorLastName + ' at ' + post.createdAt, fontSize: 15,});
            data.push('\r\n');
            data.push({text: post.title, fontSize: 18, bold: true});
            data.push({text: post.body, fontSize: 16});
            data.push('\r\n');
            data.push({text: 'Количество лайков: ' + post.likesCount, fontSize:14, bold: true});
            data.push('\r\n');
            data.push({text: 'Ссылка на пост в социальной сети', link: post.postLink});
            if(i != posts.length-1){
                data.push({text: '', pageBreak: 'after'});
            }
        }
        docDefinition.content = data;

        const options = {
            // ...
        }

        return printer.createPdfKitDocument(docDefinition, options);
    }

}
