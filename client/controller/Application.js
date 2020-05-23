
export class Application {

    constructor() {

    }

    renderView(){
        const container = new Container('main', 'main');
        const toolbar = new Toolbar('toolbar', 'toolbar');
        const form = new Form('form', 'form', 'Drop');
    }

    setupEventHandlers(){
        document.getElementById('button').addEventListener('click', this.submitForm().bind(this));
    }

    submitForm() {

    }

    updateView() {

    }
}