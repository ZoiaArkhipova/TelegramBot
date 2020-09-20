package app.commands;

import app.data.presentrecipient.Gender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class SetGenderCommand extends AnonymizerCommand {

    private final AnonymousService mAnonymouses;

    public SetGenderCommand(AnonymousService anonymouses) {
        super("choose_gender", "choose your gender\n");
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
            sb.append("You haven't selected gender");
        else {
            Gender gender = Gender.findByCode(arguments[0]);
            mAnonymouses.setUserGender(user, gender);
            sb.append("You selected gender: ").append(gender.getCode());
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }


}