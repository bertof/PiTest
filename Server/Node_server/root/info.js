/**
 * Created by bertof on 28/05/17.
 */

"use strict";

let requestHandler = require("../classes/requestHandler");

const packageFile = require("../package.json");

module.exports = {
    get: (req, res) => {
        let checkRequest = requestHandler.checkValidity(req, {"needsAuthentication": true});
        if (checkRequest.ok === true) {
            let payload = {};

            payload.timestamp = +new Date;
            payload.appName = packageFile.name;
            payload.version = packageFile.version;
            payload.author = packageFile.author;

            res.send(payload);

        } else {
            res.send(checkRequest);
        }
    }
};