package com.github.nemiro54.telegrambot.command;

import com.github.nemiro54.telegrambot.repository.entity.GroupSub;
import com.github.nemiro54.telegrambot.repository.entity.TelegramUser;
import com.github.nemiro54.telegrambot.service.SendBotMessageService;
import com.github.nemiro54.telegrambot.service.TelegramUserService;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;

import java.util.stream.Collectors;

import static com.github.nemiro54.telegrambot.command.CommandUtils.getChatId;

/**
 * {@link Command} for getting list of {@link GroupSub}
 */
public class ListGroupSubCommand implements Command {
    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;

    public ListGroupSubCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        TelegramUser telegramUser = telegramUserService.findByChatId(getChatId(update))
                .orElseThrow(NotFoundException::new);
        String message;
        if (CollectionUtils.isEmpty(telegramUser.getGroupSubs())) {
            message = "Пока нет подписок на группы. Чтобы добавить подписки напиши /addgroupsub";
        } else {
            String collectedGroups = telegramUser.getGroupSubs().stream()
                    .map(it -> String.format("%s, ID = %s\n", it.getTitle(), it.getId()))
                    .collect(Collectors.joining());
            message = String.format("Вот группы, на которые ты подписан: \n\n%s", collectedGroups);
        }
        sendBotMessageService.sendMessage(telegramUser.getChatId(), message);
    }
}
