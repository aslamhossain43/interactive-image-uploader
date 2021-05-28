export interface IFile {
    id?: number;
    image?: string;
}

export class File implements IFile {
    constructor(public id?: number, public file?: string) { }
}