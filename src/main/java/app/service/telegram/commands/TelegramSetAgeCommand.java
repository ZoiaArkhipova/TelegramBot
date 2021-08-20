package app.service.telegram.commands;

import app.data.presentrecipient.Age;
import app.service.telegram.TelegramUserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class TelegramSetAgeCommand extends TelegramRandomPresentBotCommand {

    private final TelegramUserService userService;

    public TelegramSetAgeCommand(TelegramUserService userService) {
        super("choose_age", "choose recipient's age (BABY,CHILD,TEENAGER,ADULT or OLDSTER)\n");
        this.userService = userService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (!userService.hasUser(user)) {
            message.setText("Firstly you should start the bot! Execute '/start' command!");
            execute(absSender, message, user);
            return;
        }

        StringBuilder sb = new StringBuilder();
        if (arguments == null || arguments.length == 0)
            sb.append("You haven't selected age. Therefore any age will be selected");
        else {
            Age age = Age.findByCode(arguments[0]);
            userService.setUserAge(user, age);
            sb.append("You selected age: ").append(age.getCode());
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }


}