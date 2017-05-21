/**
 * Created by bertof on 19/05/17.
 */
var express = require('express');
var app = express();

app.get('/', function (req, res) {
    res.send('Hello World')
});

app.listen(4646);
