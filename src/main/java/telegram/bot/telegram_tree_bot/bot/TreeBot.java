package telegram.bot.telegram_tree_bot.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegram.bot.telegram_tree_bot.entity.Tree;
import telegram.bot.telegram_tree_bot.entity.dto.TreeDTO;
import telegram.bot.telegram_tree_bot.service.CategoryTreeServiceImpl;
import telegram.bot.telegram_tree_bot.service.CategoryTreeServiceImpl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TreeBot extends TelegramLongPollingBot {
//  STATIC FIELDS
    private static final Logger LOGGER = LoggerFactory.getLogger(TreeBot.class);
    private static final String START = "/start";
    private static final String HELP = "/help";
    private static final String ADD_ELEMENT = "/addElement";
    private static final String VIEW_TREE = "/viewTree";
    private static final String REMOVE = "/removeElement";

//  ENDING OF STATIC METHODS

//    BEANS
    @Autowired
    private CategoryTreeServiceImpl service;
//    ENDING OF BEANS

//  METHODS FOR BOT WORKING
    @Value("${bot.name}")
    private String name;

    public TreeBot(@Value("${bot.token}") String botToken){
        super(botToken);
    }
    @Override
    public String getBotUsername() {
        return name;
    }
    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() && !update.getMessage().hasText()){
            return;
        }
        Message message = update.getMessage();
        var chatId = update.getMessage().getChatId();
        var text = message.getText();
        var userName = update.getMessage().getChat().getUserName();
        List<String> command = regexChecker(text);
        try {
            switch (command.get(0)){
                case START -> {
                    startCommand(chatId, userName);
                    return;
                }
                case ADD_ELEMENT -> {
                    createElement(chatId, text);
                    return;
                }
                case REMOVE -> {
                    removeElement(chatId, text);
                    return;
                }
                case HELP -> {
                    helpCommand(chatId);
                    return;
                }
                case VIEW_TREE -> {
                    viewTree(chatId);
                    return;
                }

            }
        }catch (IndexOutOfBoundsException e){
            LOGGER.error(e.getMessage());
        }
    }
//  ENDING OF METHODS FOR WORKING


// METHODS FOR COMMANDS

    private void startCommand(Long chatId, String username){
        var text = """
                        Добро пожаловать в Бот, %s!
                        
                        Бот поможет вам создать дерево, удалить дерево и посмотреть дерево.
                        
                        Для этого вам следует воспользоваться следуюшими командами.
                        
                        /addElement -> создает дерево,
                        /viewTree -> что бы посмотреть дерево,
                        /removeElement -> что бы удалить элемент
                        
                        Дополнительные команды:
                        /help -> Получение справки
                        """;
        var formatted = String.format(text, username);
        messageSender(chatId, formatted);
    }

    private void createElement(Long chatId, String text){
        List<String> commands = regexChecker(text);
        if (commands.size() == 2){
            service.createCategoryTreeParent(commands.get(1));
            var message = """
                            Дерево успешно сохранено
                            """;
            messageSender(chatId, message);
        } else if (commands.size() == 3) {
            Tree tree = service.createCategoryTreeChildren(commands.get(1), commands.get(2));
            if (Objects.isNull(tree)){
                var message = """
                       Я не смог найти Родителя по имени %s  
                 """;
                String format = String.format(message, commands.get(1));
                messageSender(chatId, format);
            }else {
                var message = """
                            Дерево успешно сохранено
                            """;
                messageSender(chatId, message);
            }
        }else {
            var message = """
                           Неверный формат ввода
                            """;
            messageSender(chatId, message);
        }
    }

    public void removeElement(Long chatId, String text){
        List<String> commands = regexChecker(text);
        if (commands.size() == 2){
            boolean result = service.deleteCategoryTree(commands.get(1));
            if (result){
                var message = """
                           Дерево успешно удалено
                            """;
                messageSender(chatId, message);
                return;
            }else {
                var message = """
                           Не смог удалить дерево
                            """;
                messageSender(chatId, message);
                return;
            }
        }
        return;
    }

    public void helpCommand(Long chatId){
        var text = """
                    Команды:
                    
                    /addElement -> <parent> Создать root дерево
                    /addElement -> <parent> <child> Создать дочернее дерево
                    /viewTree -> Посмотреть дерево
                    /removeElement -> <name> Удалить дерево
                    """;
        messageSender(chatId, text);
        return;
    }

    public void viewTree(Long chatId){
        List<TreeDTO> all = service.getAll();
        StringBuilder stringBuilder = new StringBuilder();
        for (TreeDTO tree : all) {
            stringBuilder.append(tree.toString());
        }
        String tree = stringBuilder.toString();
        messageSender(chatId, tree);
        return;
    }
//  ENDING OF METHODS FOR COMMANDS


//  EXTRA METHODS
    private List<String> regexChecker(String command){
        var regex = "(/\\S+)(?:\\s+<(\\S+)>)?(?:\\s+<(\\S+)>)?";
        ArrayList<String> objects = new ArrayList<>();
        if (command.matches(regex)){
            Pattern compile = Pattern.compile(regex);
            Matcher matcher = compile.matcher(command);
            if (matcher.matches()){
                objects.add(matcher.group(1));
                if (matcher.group(2) != null){
                    objects.add(matcher.group(2));
                }
                if (matcher.group(3) != null)
                    objects.add(matcher.group(3));
            }
        }
       return objects;
    }

    private void messageSender(Long chatId, String message){
        SendMessage sender = new SendMessage();
        sender.setChatId(chatId);
        sender.setText(message);

        try {
            execute(sender);
        } catch (TelegramApiException e) {
            LOGGER.error("Ошибка отпраки");
        }
    }
//  ENDING OF EXTRA METHODS
}
