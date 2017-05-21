/**
 * Created by bertof on 21/05/17.
 */
'use strict';

let shell = require("shelljs");

class Command {
    constructor(executable, args, cb) {
        let execName = executable;
        this.getExecutable = () => {
            return execName
        };
        let executionArguments = args;
        this.getExecutionArguments = () => {
            return executionArguments;
        };
        let callback = cb;
        this.getCallback = () => {
            return callback;
        };
        let childProcess;
        this.getChildProcess = () => {
            return childProcess;
        };
        let outData = "";
        this.getOutData = () => {
            return outData
        };
        this.appendOutData = (newData) => {
            outData += newData;
        };
        let errData = "";
        this.getErrData = () => {
            return errData
        };
        this.appendErrData = (newData) => {
            errData += newData
        };
        let code = null;
        this.getCode = () => {
            return code
        };
        this.setCode = (newCode) => {
            code = newCode
        };
    }

    startExecution() {

        this.childProcess = shell.exec(this.getExecutable() + " " + this.getExecutionArguments().join(" "), {
            async: true,
            shell: "/usr/bin/bash",
            silent: true
        });
        this.childProcess.stdout.on("data", (data) => this.appendOutData(data));
        this.childProcess.stderr.on("data", (data) => this.appendErrData(data));
        this.childProcess.on("close", (errorCode) => {
            this.setCode(errorCode);
            this.getCallback()({stdout: this.getStdOut(), sterr: this.getStdErr(), code: this.getExecCode()});
        });
    }

    getStdOut() {
        return this.getOutData();
    }

    getStdErr() {
        return this.getErrData();
    }

    getExecCode() {
        return this.getCode();
    }
}

module.exports = Command;