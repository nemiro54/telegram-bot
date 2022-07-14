package com.github.nemiro54.telegrambot.command;

import com.github.nemiro54.telegrambot.repository.entity.TelegramUser;
import com.github.nemiro54.telegrambot.service.SendBotMessageService;
import com.github.nemiro54.telegrambot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Start {@link Command}.
 */
public class StartCommand  implements Command {

    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;

    public final static String START_MESSAGE = """
            Привет. Я Javarush Telegram Bot.
            Я помогу тебе быть в курсе последних статей тех авторов, которые тебе интересны.

            Нажимай /addGroupSub чтобы подписаться на группу статей в JavaRush.
            Не знаешь о чем я? Напиши /help, чтобы узнать что я умею.""";

    public StartCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        String chatId = update.getMessage().getChatId().toString();

        telegramUserService.findByChatId(chatId)
                .ifPresentOrElse(user -> {
                            user.setActive(true);
                            telegramUserService.save(user);
                        },
                        () -> {
                            TelegramUser telegramUser = new TelegramUser();
                            telegramUser.setActive(true);
                            telegramUser.setChatId(chatId);
                            telegramUserService.save(telegramUser);
                        });

        sendBotMessageService.sendMessage(chatId, START_MESSAGE);
    }
}
