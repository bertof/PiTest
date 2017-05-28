/**
 * Created by bertof on 21/05/17.
 */
'use strict';

let chai = require("chai");
let assert = chai.assert;
let expect = chai.expect;

chai.config.includeStack = true;

describe("Base app tests", () => {
    context("Everything ok", () => {
        it("Should return no error", (done)=>{
            console.log("OK");
            done();
        });
    });
});