package app.service.telegram.commands;

import app.service.telegram.TelegramUserService;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Getter
public final class TelegramGetRecipientInfoCommand extends TelegramRandomPresentBotCommand {

    private final TelegramUserService userService;

    public TelegramGetRecipientInfoCommand(TelegramUserService userService) {
        super("get_recipient_info", "gets recipient info such as gender and age.");
        this.userService = userService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (!userService.hasUser(user)) {
            sb.append("You are not in bot users' list! Send /start command!");

        } else {
            sb.append("Recipient: ").append(userService.getUser(user).get().getGiftRecipient().toString());
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }
}
