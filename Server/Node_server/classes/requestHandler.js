/**
 * Created by bertof on 28/05/17.
 */
'use strict';

const authenticator = require("./authenticator");

const errorNeedsAuthenticationMissing = {"ok": false, "error": "needsAuthentication parameter missing"};
const errorNoTokenPassed = {"ok": false, "error": "no token passed"};
const errorInvalidToken = {"ok": false, "error": "invalid token"};
const errorSomethingWentWrong = {"ok": false, "error": "something went wrong"};

const messageValidRequest = {"ok": true, "info": "valid request"};

module.exports = {
    checkValidity: (request, parameters = {"needsAuthentication": true}) => {

        //Check parameters section
        if (!(parameters.hasOwnProperty("needsAuthentication") && typeof (parameters.needsAuthentication) === "boolean")) {
            console.error("ERROR:", errorNeedsAuthenticationMissing.error);
            return errorSomethingWentWrong;
        }

        //Check authentication section
        if (parameters.hasOwnProperty("needsAuthentication") && parameters.needsAuthentication === true) {
            if (!request.query.hasOwnProperty("token")) {
                console.error("ERROR:", errorNoTokenPassed.error);
                return errorNoTokenPassed;
            } else if (!authenticator.authenticate(request.query.token)) {
                console.error("ERROR:", errorInvalidToken.error);
                return errorInvalidToken;
            }
        }

        //Valid request
        return messageValidRequest;
    }
};