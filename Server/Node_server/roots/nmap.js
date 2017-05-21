/**
 * Created by bertof on 21/05/17.
 */

let Command = require("../command/command");

module.exports = {
    get: function (req, res) {
        let nmap = new Command("nmap", ["192.168.1.0/24"], function (data) {
            console.log(data);
            res.send("<pre>" + (data.code === 0 ? data.stdout : data.stderr) + "</pre>");
        });
        nmap.startExecution()
    }
};
