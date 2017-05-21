/**
 * Created by bertof on 19/05/17.
 */
let express = require('express');
let app = express();

let index = require("./roots/index");
let nmap = require("./roots/nmap");

app.route("/")
    .get((req, res) => index.get(req, res));

app.route("/nmap")
    .get((req, res) => nmap.get(req, res));

app.listen(4646);

module.exports = app;