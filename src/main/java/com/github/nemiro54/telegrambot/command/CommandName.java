package com.github.nemiro54.telegrambot.command;

/**
 * Enumeration for {@link Command}'s.
 */
public enum CommandName {
    START("/start"),
    STOP("/stop"),
    HELP("/help"),
    NO("/no"),
    STAT("/stat"),
    ADD_GROUP_SUB("/addGroupSub"),
    LIST_GROUP_SUB("/listGroupSub");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
