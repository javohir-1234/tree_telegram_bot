package telegram.bot.telegram_tree_bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegram.bot.telegram_tree_bot.bot.TreeBot;

@Configuration
public class TreeBotConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(TreeBot treeBot) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(treeBot);
        return telegramBotsApi;
    }
}
