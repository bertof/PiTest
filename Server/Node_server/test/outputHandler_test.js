/**
 * Created by bertof on 12/06/17.
 */
"use strict";

let chai = require("chai");
let assert = chai.assert;
let expect = chai.expect;

const outputHandler = require("../classes/outputHandler");
const validToken = require("../config.json").authenticationToken;

const testString = "First line output\nSecond line output\nThird line output";
const testEmptyString = "";

const testJSONOutput = {
    "startTime": 1497278107082,
    "endTime": 1497278107135,
    "code": 1,
    "output": [{
        "id": 0,
        "time": 1497278107134,
        "type": 'STDERR',
        "text": 'Cannot connect to the Docker daemon at unix:///var/run/docker.sock. Is the docker daemon running?\n'
    }]
};


describe("OutputHandler", () => {
    describe("convertRawText", () => {
        context("Passing a valid string", () => {
            it("returns a JSON array of output objects", (done) => {
                let result = outputHandler.convertRawText(testString);

                expect(result).to.haveOwnPropertyDescriptor("startTime");
                expect(result).to.haveOwnPropertyDescriptor("endTime");
                expect(result).to.haveOwnPropertyDescriptor("code");
                expect(result).to.haveOwnPropertyDescriptor("output");
                expect(result.startTime).to.be.a("number");
                expect(result.endTime).to.be.a("number");
                expect(result.code).to.be.a("number");
                expect(result.output).to.be.a("array");

                for (let i = 0; i < result.output.length; i++) {
                    expect(result.output[i].id).to.be.a("number");
                    expect(result.output[i].time).to.be.a("number");
                    expect(["STDOUT", "STDERR"]).to.include(result.output[i].type);
                    expect(result.output[i].text).to.be.a("string");
                    expect(result.output[i].text.length).to.be.greaterThan(0);
                }

                done();

            });
        });
        context("Passing a empty string", () => {
            it("returns a JSON array of output objects", (done) => {
                let result = outputHandler.convertRawText(testEmptyString);

                expect(result).to.haveOwnPropertyDescriptor("startTime");
                expect(result).to.haveOwnPropertyDescriptor("endTime");
                expect(result).to.haveOwnPropertyDescriptor("code");
                expect(result).to.haveOwnPropertyDescriptor("output");
                expect(result.startTime).to.be.a("number");
                expect(result.endTime).to.be.a("number");
                expect(result.code).to.be.a("number");
                expect(result.output).to.be.a("array");

                for (let i = 0; i < result.output.length; i++) {
                    expect(result.output[i].id).to.be.a("number");
                    expect(result.output[i].time).to.be.a("number");
                    expect(["STDOUT", "STDERR"]).to.include(result.output[i].type);
                    expect(result.output[i].text).to.be.a("string");
                    expect(result.output[i].text.length).to.be.greaterThan(0);
                }

                done();
            });
        });
    });
    describe("convertChild", () => {
        context("Passing a valid child object", () => {
            it("Returns a ordered valid child object", (done) => {
                let result = outputHandler.convertChild(testJSONOutput);

                expect(result).to.haveOwnPropertyDescriptor("startTime");
                expect(result).to.haveOwnPropertyDescriptor("endTime");
                expect(result).to.haveOwnPropertyDescriptor("code");
                expect(result).to.haveOwnPropertyDescriptor("output");
                expect(result.startTime).to.be.a("number");
                expect(result.endTime).to.be.a("number");
                expect(result.code).to.be.a("number");
                expect(result.output).to.be.a("array");

                for (let i = 0; i < result.output.length; i++) {
                    expect(result.output[i].id).to.be.a("number");
                    expect(result.output[i].time).to.be.a("number");
                    expect(["STDOUT", "STDERR"]).to.include(result.output[i].type);
                    expect(result.output[i].text).to.be.a("string");
                    expect(result.output[i].text.length).to.be.greaterThan(0);
                }

                done();
            })
        })
    })
});
