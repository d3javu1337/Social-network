import * as process from "node:process";
import {NestFactory} from "@nestjs/core";
import {AppModule} from "./app.module";
import {DocumentBuilder, SwaggerModule} from "@nestjs/swagger";
import {json, urlencoded} from "express";


async function start(){
    const PORT = process.env.PORT || 5488;
    const app = await NestFactory.create(AppModule)

    const config = new DocumentBuilder()
        .setTitle('test')
        .setDescription('test')
        .setVersion('1.0.0')
        .build();
    const document = SwaggerModule.createDocument(app, config);
    SwaggerModule.setup('/api/v1/docs', app, document);

    app.use(json({limit : '50mb'}))
    app.use(urlencoded({limit : '50mb', extended: true}))

    await app.listen(PORT, () => console.log(`started on ${PORT}`));
}

start();
