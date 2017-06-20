/**
 * Created by bertof on 28/05/17.
 */
"use strict";

let requestHandler = require("../classes/requestHandler");
const fs = require("fs");

//TODO check if it works correctly

module.exports = {
    get: (req, res) => {
        let checkRequest = requestHandler.checkValidity(req, {"needsAuthentication": true});
        if (checkRequest.ok === true) {

            //Read content of Commands folder
            fs.readdir("root/Scripts", (err, files) => {

                //Filter "..", "." and hidden files, then remove ".js" extension
                files = files.filter((file) => {
                    return file.match(/^[^\.]/);
                }).filter((file) => {
                    return file.match(/\.js$/);
                }).map((file) => {
                        return file.replace(/\.js$/, "");
                    }
                );

                let payload = [];

                //Get info from each file and put it in a command object in the payload array
                files.forEach((fileNameWithoutExtension) => {
                    let scriptFile = require("./Scripts/" + fileNameWithoutExtension);

                    let script = {
                        "script": fileNameWithoutExtension,
                        "calls": Object.keys(scriptFile).filter((call) => {
                            return ["get", "post"].includes(call);
                        }),
                        "description": scriptFile.description !== undefined ? scriptFile.description : null

                    };

                    if (fileNameWithoutExtension.hasOwnProperty("parameters")) {
                        script.parameters = fileNameWithoutExtension.parameters;
                    }

                    payload.push(script);
                });

                //Send all the info gathered
                res.send(payload)
            });


        } else {
            res.send(checkRequest);
        }
    }
};