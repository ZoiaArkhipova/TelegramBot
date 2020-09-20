package app.commands;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Getter
public final class GetRecipientInfoCommand extends AnonymizerCommand {

    private final AnonymousService mAnonymouses;

    public GetRecipientInfoCommand(AnonymousService anonymouses) {
        super("get_recipient_info", "gets recipient info such as gender and age.");
        mAnonymouses = anonymouses;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (!mAnonymouses.hasAnonymous(user)) {

            sb.append("You are not in bot users' list! Send /start command!");

        } else {

            //todo: check if mAnonymouses.getAnonymous(user) is empty
            sb.append("Recipient: ").append(mAnonymouses.getAnonymous(user).get().getGiftRecipient().toString());
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }
}
