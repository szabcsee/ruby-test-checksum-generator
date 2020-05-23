import {Application} from "./controller/Application.js";

const application = new Application();

application.renderView();
application.setupEventHandlers();