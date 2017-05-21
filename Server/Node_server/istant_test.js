/**
 * Created by bertof on 21/05/17.
 */

let Command = require("./command/command");

let nodeVersion = new Command("nmap", ["localhost"], function (data) {
    console.log("DATA", data);
});

nodeVersion.startExecution();