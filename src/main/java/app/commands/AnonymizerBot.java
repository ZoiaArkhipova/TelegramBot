package app.commands;

import app.service.PresentAdviserService;
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
public final class AnonymizerBot extends TelegramLongPollingCommandBot {

    private static final String BOT_NAME = "ZoiaRandomPresentBot";
    private static final String BOT_TOKEN = "1245434636:AAGtThzj6S6UZxyPNMgX1mYVleNJF5snvKM";

    private final AnonymousService mAnonymouses;

    public AnonymizerBot(PresentAdviserService presentAdviserService) {
        super();
        this.mAnonymouses = new AnonymousService();
        register(new StartCommand( mAnonymouses));
        register(new SetNameCommand(mAnonymouses));
        register(new StopCommand(mAnonymouses));
        register(new SetGenderCommand(mAnonymouses));
        register(new MyNameCommand(mAnonymouses));
        register(new GetRecipientInfoCommand(mAnonymouses));
        register(new SetAgeCommand(mAnonymouses));
        register(new AdviceCommand(mAnonymouses, presentAdviserService));
        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);
        registerDefaultAction(((absSender, message) -> {
            SendMessage text = new SendMessage();
            text.setChatId(message.getChatId());
            text.setText(message.getText() + " command not found!");

            try {
                absSender.execute(text);
            } catch (TelegramApiException e) {}

            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[] {});
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
        String messageForUsers = String.format("%s:\n%s", mAnonymouses.getDisplayedName(user), msg.getText());

        SendMessage answer = new SendMessage();

        answer.setText(clearMessage);
        answer.setChatId(msg.getChatId());
        replyToUser(answer, user, clearMessage);

        answer.setText(messageForUsers);
        Stream<Anonymous> anonymouses = mAnonymouses.anonymouses();
        anonymouses.filter(a -> !a.getUser().equals(user))
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

        if(!mAnonymouses.hasAnonymous(user)) {
            answer.setText("Firstly you should start bot! Use /start command!");
            replyToUser(answer, user, msg.getText());
            return false;
        }

        if (mAnonymouses.getDisplayedName(user) == null) {
            answer.setText("You must set a name before sending messages.\nUse '/set_name <displayed_name>' command.");
            replyToUser(answer, user, msg.getText());
            return false;
        }

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
   //         LOG.log(Level.getLevel(LogLevel.SUCCESS.getValue()), LogTemplate.MESSAGE_SENT.getTemplate(), user.getId(), messageText);
        } catch (TelegramApiException e) {
    //        LOG.error(LogTemplate.MESSAGE_EXCEPTION.getTemplate(), user.getId(), e);
        }
    }

    @PostConstruct
    public void start() {
        System.out.println("START");
    }
}