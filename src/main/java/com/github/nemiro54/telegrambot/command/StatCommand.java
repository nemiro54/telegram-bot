package com.github.nemiro54.telegrambot.command;

import com.github.nemiro54.telegrambot.command.annotation.AdminCommand;
import com.github.nemiro54.telegrambot.dto.StatisticDTO;
import com.github.nemiro54.telegrambot.service.SendBotMessageService;
import com.github.nemiro54.telegrambot.service.StatisticService;
import com.github.nemiro54.telegrambot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Collectors;

/**
 * Statistics {@link Command}.
 */
@AdminCommand
public class StatCommand implements Command {

    private final StatisticService statisticService;
    private final SendBotMessageService sendBotMessageService;

    public final static String STAT_MESSAGE = """
            <b>Статистика по боту:</b>
            
            - активных пользователей: %s
            - неактивных пользователей: %s
            - средннее количество групп на одного пользователя: %s
            
            <b>Информация по активным группам:</b>
            %s""";

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    public StatCommand(SendBotMessageService sendBotMessageService, StatisticService statisticService) {
        this.sendBotMessageService = sendBotMessageService;
        this.statisticService = statisticService;
    }

    @Override
    public void execute(Update update) {
        StatisticDTO statisticDTO = statisticService.countBotStatistic();

        String collectedGroups = statisticDTO.getGroupStatDTOs().stream()
                .map(it -> String.format("%s (id = %s) - %s подписчиков", it.getTitle(), it.getId(), it.getActiveUserCount()))
                .collect(Collectors.joining("\n"));

        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), String.format(STAT_MESSAGE,
                statisticDTO.getActiveUserCount(),
                statisticDTO.getInactiveUserCount(),
                statisticDTO.getAverageGroupCountByUser(),
                collectedGroups));
    }
}
