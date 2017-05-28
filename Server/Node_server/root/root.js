/**
 * Created by bertof on 21/05/17.
 */
"use strict";

let requestHandler = require("../classes/requestHandler");

module.exports = {
    get: (req, res) => {
        res.send("<pre>PiTest server running: " + require("../package.json").version + "</pre>");
    }
};