/**
 * Created by bertof on 28/05/17.
 */
"use strict";

let requestHandler = require("../classes/requestHandler");
const fs = require("fs");

module.exports = {
    get: (req, res) => {
        let checkRequest = requestHandler.checkValidity(req, {"needsAuthentication": true});
        if (checkRequest.ok === true) {

            //Read content of commands folder
            fs.readdir("root/commands", (err, files) => {

                //Filter "..", "." and hidden files, then remove ".js" extension
                files = files.filter((file) => {
                    return file.match(/^[^\.]/);
                }).map((file) => {
                        return file.replace(/\.js$/, "");
                    }
                );

                let payload = [];

                //Get info from each file and put it in a command object in the payload array
                files.forEach((fileNameWithoutExtension) => {
                    let commandFile = require("./commands/" + fileNameWithoutExtension);

                    let command = {
                        "command": fileNameWithoutExtension,
                        "calls": Object.keys(commandFile).filter((call) => {
                            return ["get", "post"].includes(call);
                        })
                    };

                    if (fileNameWithoutExtension.hasOwnProperty("parameters")) {
                        command.parameters = fileNameWithoutExtension.parameters;
                    }

                    payload.push(command);
                });

                //Send all the info gathered
                res.send(payload)
            });


        } else {
            res.send(checkRequest);
        }
    }
};