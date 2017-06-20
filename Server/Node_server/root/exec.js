/**
 * Created by bertof on 31/05/17.
 */

let Command = require("../classes/command");
let outputHandler = require("../classes/outputHandler");
let requestHandler = require("../classes/requestHandler");

module.exports = {
    get: function (req, res) {

        let checkReq = requestHandler.checkValidity(req, {"needsAuthentication": true});

        if (checkReq.ok === true) {

            let command = "";
            if (req.query.hasOwnProperty("command") && typeof req.query.command === "string") {
                command = req.query.command;
            }

            console.log("EXECUTING: " + command);
            let concreteCommand = new Command(command, (child) => {
                outputHandler.handleChild(req, res, child);
            });
            // concreteCommand.emitter.on("outdata", (data) => {
            //     // console.log("Outdata:", data);
            // });
            // concreteCommand.emitter.on("errdata", (data) => {
            //     // console.log("Error:", data);
            // });
            concreteCommand.startExecution();
        }
        else {
            res.send(checkReq);
        }


    }
};
