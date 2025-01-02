import {ApiProperty} from "@nestjs/swagger";


export class CompactModel{

    @ApiProperty()
    id: number

    @ApiProperty()
    title: string

    @ApiProperty()
    body: string

    @ApiProperty()
    likesCount: number

    @ApiProperty()
    createdAt: string

    @ApiProperty()
    authorFirstName: string

    @ApiProperty()
    authorLastName: string

    @ApiProperty()
    postLink: string

}
