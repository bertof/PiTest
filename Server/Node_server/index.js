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
let scripts = require("./root/scripts");

let exec = require("./root/exec");

//Scripts
let airodump_scan = require("./root/Scripts/airodump_scan");
let hello_world = require("./root/Scripts/hello_world");

//Setting up roots
app.route("/")
    .get((req, res) => root.get(req, res));

app.route("/info")
    .get((req, res) => info.get(req, res));

app.route("/auth")
    .get((req, res) => auth.get(req, res));

app.route("/exec")
    .get((req, res) => exec.get(req, res));

app.route("/scripts")
    .get((req, res) => scripts.get(req, res));

app.route("/scripts/airodump_scan")
    .get((req, res) => airodump_scan.get(req, res));

app.route("/scripts/hello_world")
    .get((req, res) => hello_world.get(req, res));

//Set up listening port
app.listen(config.serverPort);

module.exports = app;