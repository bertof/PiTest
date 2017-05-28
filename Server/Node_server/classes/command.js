/**
 * Created by bertof on 21/05/17.
 */
'use strict';

let shell = require("shelljs");
const Emitter = require("events");

class CommandEmitter extends Emitter {
}

class Command {
    constructor(args, cb) {
        this.executionArguments = args.split(" ");
        this.callback = cb;
        this.emitter = new CommandEmitter();
        this.data = [];
        this.code = null;

        let id = 0;

        this.appendOutData = (data) => {
            data.split("\n").forEach((singleData) => {
                if (singleData !== '') {
                    let line = {"id": id++, "time": +new Date, "type": "STDOUT", "text": singleData};
                    this.data.push(line);
                    this.emitter.emit("outdata", line);
                }
            })
        };

        this.appendErrData = (data) => {
            let line = {"id": id++, "time": +new Date, "type": "STDERR", "text": data};
            this.data.push(line);
            this.emitter.emit("errdata", line);
        };
    }

    startExecution() {
        this.startTime = +new Date;
        this.childProcess = shell.exec(
            this.executionArguments.join(" "),
            {
                async: true,
                // shell: "/usr/bin/bash",
                silent: true
            }
        );

        this.childProcess.stdout.on("data", (data) => this.appendOutData(data));
        this.childProcess.stderr.on("data", (data) => this.appendErrData(data));
        this.childProcess.on("close", (errorCode) => {
            this.code = errorCode;
            if (typeof this.callback === "function") {
                this.callback({
                    "startTime": this.startTime,
                    "endTime": +new Date,
                    "code": this.getExecCode(),
                    "output": this.getData()
                });
            }
        });
    }

    getData() {
        return this.data;
    }

    getExecCode() {
        return this.code;
    }

}

module.exports = Command;