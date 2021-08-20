package app.service.telegram.commands;
import app.service.telegram.TelegramUserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class TelegramStopCommand extends TelegramRandomPresentBotCommand {

    private final TelegramUserService userService;

    public TelegramStopCommand(TelegramUserService userService) {
        super("stop", "remove yourself from bot users' list\n");
        this.userService = userService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (userService.removeUser(user)) {
            sb.append("You've been removed from bot's users list! Bye!");
        } else {
            sb.append("You were not in bot users' list. Bye!");
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }
}