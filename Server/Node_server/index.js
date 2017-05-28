/**
 * Created by bertof on 19/05/17.
 */
let express = require('express');
let app = express();

let root = require("./root/root");
let info = require("./root/info");
let auth = require("./root/auth");
let commands = require("./root/commands");

let nmap = require("./root/commands/nmap");

app.route("/")
    .get((req, res) => root.get(req, res));

app.route("/info")
    .get((req, res) => info.get(req, res));

app.route("/auth")
    .get((req, res) => auth.get(req, res));

app.route("/commands")
    .get((req, res) => commands.get(req, res));

app.route("/commands/nmap")
    .get((req, res) => nmap.get(req, res));

app.listen(4646);

module.exports = app;