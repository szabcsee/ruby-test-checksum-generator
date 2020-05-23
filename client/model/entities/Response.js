export class Response {

    constructor(responseText) {
        this.responseText = responseText;
    }
    getResponseText() {
        return this.responseText;
    }
    getParsedResponse() {
        return JSON.parse(this.responseText);
    }
}