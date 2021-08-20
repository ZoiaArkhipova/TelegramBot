package app.service.telegram;

import app.data.TelegramUser;
import app.service.PresentAdviserService;
import app.service.telegram.commands.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.stream.Stream;

@Service
public final class TelegramRandomPresentBot extends TelegramLongPollingCommandBot {

    private static final String BOT_NAME = "RandomPresentBot";
    private static final String BOT_TOKEN = "1245434636:AAGtThzj6S6UZxyPNMgX1mYVleNJF5snvKM";

    private final TelegramUserService userService;

    public TelegramRandomPresentBot(TelegramUserService userService, PresentAdviserService presentAdviserService) {
        super();
        this.userService = userService;
        register(new TelegramStartCommand(userService));
        //register(new TelegramSetNameCommand(userService));
        register(new TelegramStopCommand(userService));
        register(new TelegramSetGenderCommand(userService));
        register(new TelegramGetRecipientInfoCommand(userService));
        register(new TelegramSetAgeCommand(userService));
        register(new TelegramAdviceCommand(userService, presentAdviserService));
        TelegramHelpCommand telegramHelpCommand = new TelegramHelpCommand(this);
        register(telegramHelpCommand);
        registerDefaultAction(((absSender, message) -> {
            SendMessage text = new SendMessage();
            text.setChatId(message.getChatId());
            text.setText(message.getText() + " command not found!");

            try {
                absSender.execute(text);
            } catch (TelegramApiException e) {}

            telegramHelpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[] {});
        }));

    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        if (!update.hasMessage()) {
            throw new IllegalStateException("Update doesn't have a body!");
        }

        Message msg = update.getMessage();
        User user = msg.getFrom();

        if (!canSendMessage(user, msg)) {
            return;
        }

        String clearMessage = msg.getText();
        String messageForUsers = String.format("%s:\n%s", msg.getText());
                //userService.getDisplayedName(user), msg.getText());

        SendMessage answer = new SendMessage();

        answer.setText(clearMessage);
        answer.setChatId(msg.getChatId());
        replyToUser(answer, user, clearMessage);

        answer.setText(messageForUsers);
        Stream<TelegramUser> users = userService.users();
        users.filter(a -> !a.getUser().equals(user))
                .forEach(a -> {
                    answer.setChatId(a.getChat().getId());
                    sendMessageToUser(answer, a.getUser(), user);
                });
    }

    private boolean canSendMessage(User user, Message msg) {

        SendMessage answer = new SendMessage();
        answer.setChatId(msg.getChatId());

        if (!msg.hasText() || msg.getText().trim().length() == 0) {
            answer.setText("You shouldn't send empty messages!");
            replyToUser(answer, user, msg.getText());
            return false;
        }

        if(!userService.hasUser(user)) {
            answer.setText("Firstly you should start bot! Use /start command!");
            replyToUser(answer, user, msg.getText());
            return false;
        }

/*        if (userService.getDisplayedName(user) == null) {
            answer.setText("You must set a name before sending messages.\nUse '/set_name <displayed_name>' command.");
            replyToUser(answer, user, msg.getText());
            return false;
        }*/

        return true;
    }

    private void sendMessageToUser(SendMessage message, User receiver, User sender) {
        try {
            execute(message);
        } catch (TelegramApiException e) {}
    }

    private void replyToUser(SendMessage message, User user, String messageText) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
        }
    }

    @PostConstruct
    public void start() {
        System.out.println("START");
    }
}