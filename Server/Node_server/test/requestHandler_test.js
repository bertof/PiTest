/**
 * Created by bertof on 28/05/17.
 */
/**
 * Created by bertof on 28/05/17.
 */
'use strict';

let chai = require("chai");
let assert = chai.assert;
let expect = chai.expect;

const requestHandler = require("../classes/requestHandler");

const validToken = require("../config.json").authenticationToken;
const invalidToken = validToken + "RANDOM STRING";

describe("RequestHandler module tests", () => {
    describe("#checkValidity", () => {
        describe("Check authentication section", () => {
            const parameters = {"needsAuthentication": true};
            context("Passing a valid token", () => {
                it("returns a valid request object", (done) => {
                    let result = requestHandler.checkValidity({"query": {"token": validToken}}, parameters);
                    expect(result.ok).to.be.true;
                    expect(result.info).to.be.equal("valid request");
                    done();
                });
            });
            context("Passing a invalid token", () => {
                it("returns a valid request object", (done) => {
                    let result = requestHandler.checkValidity({"query": {"token": invalidToken}}, parameters);
                    expect(result.ok).to.be.false;
                    expect(result.error).to.be.equal("invalid token");
                    done();
                });
            });
            context("Passing a query missing the token", () => {
                it("returns a valid request object", (done) => {
                    let result = requestHandler.checkValidity({"query": {}}, parameters);
                    expect(result.ok).to.be.false;
                    expect(result.error).to.be.equal("no token passed");
                    done();
                });
            });
        });
        describe("Skip authentication", () => {
            const parameters = {"needsAuthentication": false};
            context("Passing a query", () => {
                it("returns a valid request object", (done) => {
                    let result = requestHandler.checkValidity({}, parameters);
                    expect(result.ok).to.be.true;
                    expect(result.info).to.be.equal("valid request");
                    done();
                });
            });
        });
    });
});