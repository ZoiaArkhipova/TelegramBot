package app.service.telegram.commands;

import app.data.presentrecipient.Gender;
import app.service.telegram.TelegramUserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class TelegramSetGenderCommand extends TelegramRandomPresentBotCommand {

    private final TelegramUserService userService;

    public TelegramSetGenderCommand(TelegramUserService userService) {
        super("choose_gender", "choose recipient's gender(MALE or FEMALE)\n");
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
            sb.append("You haven't selected gender. Therefore any gender will be selected");
        else {
            Gender gender = Gender.findByCode(arguments[0]);
            userService.setUserGender(user, gender);
            sb.append("You selected gender: ").append(gender.getCode());
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }


}