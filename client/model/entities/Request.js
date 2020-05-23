export class Request {

    constructor(method, url, data = {}) {
        this.method = method;
        this.url = url;
        this.data = data;
    };

    getMethod() {
        return this.method;
    }

    getUrl() {
        return this.url;
    }

    getData() {
        return this.data;
    }
}