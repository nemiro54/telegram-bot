package com.github.nemiro54.telegrambot.command;

import com.github.nemiro54.telegrambot.javarushclient.JavaRushGroupClient;
import com.github.nemiro54.telegrambot.javarushclient.dto.GroupDiscussionInfo;
import com.github.nemiro54.telegrambot.javarushclient.dto.GroupRequestArgs;
import com.github.nemiro54.telegrambot.repository.entity.GroupSub;
import com.github.nemiro54.telegrambot.service.GroupSubService;
import com.github.nemiro54.telegrambot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Collectors;

import static com.github.nemiro54.telegrambot.command.CommandName.ADD_GROUP_SUB;
import static com.github.nemiro54.telegrambot.command.CommandUtils.getChatId;
import static com.github.nemiro54.telegrambot.command.CommandUtils.getMessage;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * Add Group subscription {@link Command}.
 */
public class AddGroupSubCommand implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final JavaRushGroupClient javaRushGroupClient;
    private final GroupSubService groupSubService;

    public AddGroupSubCommand(SendBotMessageService sendBotMessageService, JavaRushGroupClient javaRushGroupClient, GroupSubService groupSubService) {
        this.sendBotMessageService = sendBotMessageService;
        this.javaRushGroupClient = javaRushGroupClient;
        this.groupSubService = groupSubService;
    }

    @Override
    public void execute(Update update) {
        if (getMessage(update).equalsIgnoreCase(ADD_GROUP_SUB.getCommandName())) {
            sendGroupIdList(getChatId(update));
            return;
        }

        String groupId = getMessage(update).split(SPACE)[1];
        String chatId = String.valueOf(getChatId(update));
        if (isNumeric(groupId)) {
            GroupDiscussionInfo groupById = javaRushGroupClient.getGroupById(Integer.parseInt(groupId));
            if (isNull(groupById.getId())) {
                sendGroupNotFound(chatId, groupId);
            }
            GroupSub savedGroupSub = groupSubService.save(chatId, groupById);
            sendBotMessageService.sendMessage(chatId, String.format("Теперь Вы подписаны на группу %s :)", savedGroupSub.getTitle()));
        } else {
            sendNotValidGroupID(chatId, groupId);
        }
    }

    private void sendGroupNotFound(String chatId, String groupId) {
        String groupNotFoundMessage = "Группы с ID = \"%s\" не существует";
        sendBotMessageService.sendMessage(chatId, String.format(groupNotFoundMessage, groupId));
    }

    private void sendNotValidGroupID(String chatId, String groupId) {
        String groupNotFoundMessage = "Неправильный ID группы = \"%s\"";
        sendBotMessageService.sendMessage(chatId, String.format(groupNotFoundMessage, groupId));
    }

    private void sendGroupIdList(String chatId) {
        String groupIds = javaRushGroupClient.getGroupList(GroupRequestArgs.builder().build()).stream()
                .map(group -> String.format("%s - %s\n", group.getTitle(), group.getId()))
                .collect(Collectors.joining());

        String message = """
                Чтобы подписаться на группу - передай команду вместе с ID группы.
                Например: /addGroupSub 30\s

                я подготовил список всех групп - выбирай какую хочешь :)\s

                имя группы - ID группы\s

                %s""";

        sendBotMessageService.sendMessage(chatId, String.format(message, groupIds));
    }
}
