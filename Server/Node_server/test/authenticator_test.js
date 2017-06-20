/**
 * Created by bertof on 28/05/17.
 */
'use strict';

let chai = require("chai");
let assert = chai.assert;
let expect = chai.expect;

const authenticator = require("../classes/authenticator");
const validToken = require("../config.json").authenticationToken;
const invalidToken = validToken + "RANDOM STRING";

describe("Authenticator module tests", () => {
    describe("#authenticate", () => {
        context("Passing a valid token", () => {
            it("returns true", (done) => {
                let result = authenticator.authenticate(validToken);
                expect(result).to.be.true;
                done();
            });
        });
        context("Passing an invalid token", () => {
            it("returns false", (done) => {
                let result = authenticator.authenticate(invalidToken);
                expect(result).to.be.false;
                done();
            });
        });
    });
});