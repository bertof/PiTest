/**
 * Created by bertof on 28/05/17.
 */
"use strict";

let requestHandler = require("../classes/requestHandler");

module.exports = {
    get: (req, res) => {
        res.send(requestHandler.checkValidity(req, {"needsAuthentication": true}));
    }
};