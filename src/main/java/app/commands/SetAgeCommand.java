package app.commands;

import app.data.presentrecipient.Age;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class SetAgeCommand extends AnonymizerCommand {

    private final AnonymousService mAnonymouses;

    public SetAgeCommand(AnonymousService anonymouses) {
        super("choose_age", "choose your age\n");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (!mAnonymouses.hasAnonymous(user)) {
            message.setText("Firstly you should start the bot! Execute '/start' command!");
            execute(absSender, message, user);
            return;
        }

        StringBuilder sb = new StringBuilder();
        if (arguments == null || arguments.length == 0)
            sb.append("You haven't selected age");
        else {
            Age age = Age.findByCode(arguments[0]);
            mAnonymouses.setUserAge(user, age);
            sb.append("You selected age: ").append(age.getCode());
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }


}