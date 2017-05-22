/**
 * Created by bertof on 21/05/17.
 */

module.exports = {
    get: function (req, res) {
        res.send("<pre>PiTest server running: " + require("../package.json").version + "</pre>");
    }
};