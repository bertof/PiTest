/**
 * Created by bertof on 22/05/17.
 */

const validToken = require("../config.json").authenticationToken;

module.exports = {
    authenticate: (token) => {
        return token === validToken;
    }
};