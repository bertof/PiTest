/**
 * Created by bertof on 22/05/17.
 */

module.exports = {
    /** Handler of the text
     * Gets info from the command object child and generates the response on res based on the query
     * @param req used to fetch the query
     * @param res used to send the response
     * @param child result of the execution, used to fetch the data to answer
     * @param callback optional callback
     */
    handle(req, res, child, callback){

        child.output = child.output.sort((a, b) => {
            return a.id - b.id;
        });

        let outputText = child.output.map(function (output) {
            return output.text
        }).join("\n");

        let outputJSON = JSON.stringify(child, null, 2);

        console.log("OUTPUT", outputText);

        if (req.query.type === undefined) {
            req.query.type = "default";
        }

        switch (req.query.type.toLowerCase()) {
            case "none":
                break;

            case "json":
                res.send(outputJSON);
                break;

            case "text":
                res.send("<pre>" + outputText + "</pre>");
                break;

            case "debug":
                res.send("<pre>OUTPUT<br/>\n<br/>\n" + outputText + "<br/>\n<br/>\nJSON<br/>\n<br/>\n" + outputJSON + "</pre>");
                break;

            default :
                res.send(outputJSON);
                break;
        }
        if (typeof callback === "function") {
            callback(null, {"json": outputJSON, "text": outputText});
        }
    }
};