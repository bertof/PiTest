package com.bertof.pitestremote.Object;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by bertof on 11/06/17.
 */

public class CommandItem {

    public String getCommand() {
        return command;
    }

    public ArrayList<String> getCalls() {
        return calls;
    }

    private String command;
    private ArrayList<String> calls;

    static public ArrayList<CommandItem> fromJSON(String JSONDefinition) {
        Gson gson = new Gson();
        CommandItem commandItem[] = gson.fromJson(JSONDefinition, CommandItem[].class);
        return new ArrayList<>(Arrays.asList(commandItem));
    }
}
