package app.service.telegram.commands;

import app.data.MarketModel;
import app.data.TelegramUser;
import app.data.presentrecipient.Age;
import app.data.presentrecipient.Gender;
import app.data.presentrecipient.PresentRecipient;
import app.service.PresentAdviserService;
import app.service.telegram.TelegramUserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class TelegramAdviceCommand extends TelegramRandomPresentBotCommand {

    private final TelegramUserService userService;
    private final PresentAdviserService presentAdviserService;

    public TelegramAdviceCommand(TelegramUserService userService, PresentAdviserService presentAdviserService) {
        super("advice", "suggests ideas for present based on gender and age\n");
        this.userService = userService;
        this.presentAdviserService = presentAdviserService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (!userService.hasUser(user)) {

            sb.append("You are not in bot users' list! Send /start command!");

        } else {

            PresentRecipient presentRecipient = userService.getUser(user).map(TelegramUser::getGiftRecipient).orElse(null);
            Gender gender = presentRecipient.getGender();
            Age age = presentRecipient.getAge();
            MarketModel model = presentAdviserService.getAdvice(gender, age);
            sb.append(model != null ? model.getLink() : "There are no ideas for you :(");
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }

}