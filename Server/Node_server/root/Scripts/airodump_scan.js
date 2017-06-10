/**
 * Created by bertof on 09/06/17.
 */

/**
 * Created by bertof on 21/05/17.
 */

let Command = require("../../classes/command");
let outputHandler = require("../../classes/outputHandler");
let authenticator = require("../../classes/authenticator");
let path = require("path");

module.exports = {
    get: function (req, res) {

        if (req.query.hasOwnProperty("token") && authenticator.authenticate(req.query.token) === true) {

            let parameters = "";
            if (req.query.hasOwnProperty("parameters") && typeof req.query.parameters === "string") {
                parameters = req.query.parameters;
            }

            console.log("EXECUTING: " + path.basename(__filename) + parameters);
            let nmap = new Command("sh ./root/Scripts/" + path.basename(__filename, ".js") + ".sh", function (child) {
                outputHandler.handle(req, res, child);
            });
            // nmap.emitter.on("outdata", (data) => {
            //     // console.log("Outdata:", data);
            // });
            // nmap.emitter.on("errdata", (data) => {
            //     // console.log("Error:", data);
            // });
            nmap.startExecution();
        } else {
            res.send("<pre>Invalid token passed</pre>");
        }


    }
};
