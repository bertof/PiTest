/**
 * Created by bertof on 22/05/17.
 */

"use strict";

module.exports = {

    /** Handler of the text
     * Gets info from the command object child and generates the response on res based on the query
     * @param req used to fetch the query
     * @param res used to send the response
     * @param child result of the execution, used to fetch the data to answer
     * @param callback optional callback
     */
    handleChild: (req, res, child, callback) => {

        console.log(child);

        let outputObject = module.exports.convertChild(child);

        let outputText = outputObject.output.map(function (output) {
            return output.text
        }).join("\n");

        let outputJSON = JSON.stringify(outputObject);

        console.log("OUTPUT", outputText);

        if (req.query.type === undefined) {
            req.query.type = "default";
        }

        switch (req.query.type.toLowerCase()) {
            case "none":
                break;

            case "json":
                res.type("json");
                res.send(outputJSON);
                break;

            case "pre":
                res.type("html");
                res.send("<pre>" + outputText + "</pre>");
                break;

            case "debug":
                res.type("html");
                res.send(
                    "<pre>OUTPUT\n" +
                    "===============================================================================\n" +
                    outputText +
                    "\n" +
                    "===============================================================================\n" +
                    "JSON\n" +
                    "===============================================================================\n" +
                    JSON.stringify(outputObject, null, 2) +
                    "\n" +
                    "===============================================================================" +
                    "</pre>");
                break;

            case "raw":
                res.send(outputText);
                break;

            default :
                res.type("json");
                res.send(outputJSON);
                break;
        }

        //Calling the callback
        if (typeof callback === "function") {
            callback(null, {"json": outputJSON, "text": outputText});
        }
    },

    /** Handler for raw message output
     * @param req request object
     * @param res response object
     * @param message string of the message
     */
    handleRawMessage: (req, res, message) => {
        module.exports.handleChild(req, res, module.exports.convertRawText(message));
    },

    /** Generates an output JSON from a string
     *
     * @param string
     * @returns {Array}
     */
    convertRawText: (string) => {

        let stringArray = string.split(/[\r]*[\n]+/).filter((string) => {
            return string.length > 0
        });

        let result = [];
        for (let i = 0; i < stringArray.length; i++) {
            result[i] = {
                "id": i,
                "time": +new Date(),
                "type": "STDOUT",
                "text": stringArray[i]
            };
        }

        return {
            "startTime": result[0] !== undefined ? result[0].time : +new Date(),
            "endTime": result[result.length - 1] !== undefined ? result[result.length - 1].time : +new Date(),
            "code": 0,
            "output": result
        };
    },

    convertChild: (child) => {

        child.output = child.output.sort((a, b) => {
            return a.id - b.id;
        });

        return child;

    }
};