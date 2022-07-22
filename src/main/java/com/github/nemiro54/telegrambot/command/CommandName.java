package com.github.nemiro54.telegrambot.command;

/**
 * Enumeration for {@link Command}'s.
 */
public enum CommandName {
    START("/start"),
    STOP("/stop"),
    HELP("/help"),
    ADMIN_HELP("/admin_help"),
    NO("/no"),
    STAT("/stat"),
    ADD_GROUP_SUB("/addgroupsub"),
    DELETE_GROUP_SUB("/deletegroupsub"),
    LIST_GROUP_SUB("/listgroupsub");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
