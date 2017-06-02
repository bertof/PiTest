/**
 * Created by bertof on 21/05/17.
 */
"use strict";

let requestHandler = require("../classes/requestHandler");
let outputHandler = require("../classes/outputHandler");

const message = "PiTest server running";

module.exports = {
    get: (req, res) => {

        const payload = {"output": [{"id": 0, "text": message}]};

        outputHandler.handle(req, res, payload);
    }
};
