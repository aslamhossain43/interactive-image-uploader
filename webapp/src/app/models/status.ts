export interface IStatus {
    status?: string;
    message?: string;
}

export class Status implements IStatus {
    constructor(public status?: string, public message?: string) { }
}