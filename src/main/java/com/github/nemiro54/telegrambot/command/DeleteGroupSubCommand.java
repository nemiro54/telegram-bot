package com.github.nemiro54.telegrambot.command;

import com.github.nemiro54.telegrambot.repository.entity.GroupSub;
import com.github.nemiro54.telegrambot.repository.entity.TelegramUser;
import com.github.nemiro54.telegrambot.service.GroupSubService;
import com.github.nemiro54.telegrambot.service.SendBotMessageService;
import com.github.nemiro54.telegrambot.service.TelegramUserService;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.nemiro54.telegrambot.command.CommandName.DELETE_GROUP_SUB;
import static com.github.nemiro54.telegrambot.command.CommandUtils.getChatId;
import static com.github.nemiro54.telegrambot.command.CommandUtils.getMessage;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * Delete Group subscription {@link Command}
 */
public class DeleteGroupSubCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;
    private final GroupSubService groupSubService;

    public DeleteGroupSubCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService,
                                 GroupSubService groupSubService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
        this.groupSubService = groupSubService;
    }

    @Override
    public void execute(Update update) {
        if (getMessage(update).equalsIgnoreCase(DELETE_GROUP_SUB.getCommandName())) {
            sendGroupIdList(getChatId(update));
            return;
        }
        String groupId = getMessage(update).split(SPACE)[1];
        String chatId = getChatId(update);
        if (isNumeric(groupId)) {
            Optional<GroupSub> optionalGroupSub = groupSubService.findById(Integer.valueOf(groupId));
            if (optionalGroupSub.isPresent()) {
                GroupSub groupSub = optionalGroupSub.get();
                TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
                groupSub.getUsers().remove(telegramUser);
                groupSubService.save(groupSub);
                sendBotMessageService.sendMessage(chatId, String.format("Удалил подписку на группу %s", groupSub.getTitle()));
            } else {
                sendBotMessageService.sendMessage(chatId, "Не нашел такую группу :(");
            }
        } else {
            sendBotMessageService.sendMessage(chatId, """
                    Неправильный формат ID группы.
                    ID должен быть целым положительным числом.""");
        }
    }

    private void sendGroupIdList(String chatId) {
        String message;
        List<GroupSub> groupSubs = telegramUserService.findByChatId(chatId)
                .orElseThrow(NotFoundException::new)
                .getGroupSubs();
        if (CollectionUtils.isEmpty(groupSubs)) {
            message = "Пока нет подписок на группы. Чтобы добавить подписку напиши '/addGroupSub'";
        } else {
            String userGroupSubData = groupSubs.stream()
                    .map(group -> String.format("%s - %s \n", group.getTitle(), group.getId()))
                    .collect(Collectors.joining());

            message = String.format("""
                    Чтобы удалить подписку на группу - передай комадну вместе с ID группы.\s
                    Например: /deleteGroupSub 16\s

                    Я подготовил список всех групп, на которые ты подписан:)\s

                    имя группы - ID группы\s

                    %s""", userGroupSubData);
        }

        sendBotMessageService.sendMessage(chatId, String.format(message, message));
    }
}
