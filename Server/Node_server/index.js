/**
 * Created by bertof on 19/05/17.
 */
let express = require('express');
let app = express();

const config = require("./config.json");

//Roots
let root = require("./root/root");
let info = require("./root/info");
let auth = require("./root/auth");
let commands = require("./root/commands");

//Commands
let nmap = require("./root/Commands/nmap");

//Setting up roots
app.route("/")
    .get((req, res) => root.get(req, res));

app.route("/info")
    .get((req, res) => info.get(req, res));

app.route("/auth")
    .get((req, res) => auth.get(req, res));

app.route("/Commands")
    .get((req, res) => commands.get(req, res));

app.route("/Commands/nmap")
    .get((req, res) => nmap.get(req, res));

//Set up listening port
app.listen(config.serverPort);

module.exports = app;