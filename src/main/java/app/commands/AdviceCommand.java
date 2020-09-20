package app.commands;

import app.data.MarketModel;
import app.data.presentrecipient.Age;
import app.data.presentrecipient.Gender;
import app.data.presentrecipient.PresentRecipient;
import app.service.PresentAdviserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class AdviceCommand extends AnonymizerCommand {

    private final AnonymousService mAnonymouses;
    private final PresentAdviserService presentAdviserService;

    public AdviceCommand(AnonymousService anonymouses, PresentAdviserService presentAdviserService) {
        super("advice", "suggests ideas for present based on gender and age.");
        this.mAnonymouses = anonymouses;
        this.presentAdviserService = presentAdviserService;
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
            PresentRecipient presentRecipient = mAnonymouses.getAnonymous(user).map(Anonymous::getGiftRecipient).orElse(null);
            Gender gender = presentRecipient.getGender();
            Age age = presentRecipient.getAge();
            MarketModel model = presentAdviserService.getAdvice(gender, age);
            sb.append(model != null ? model.getLink() : "There are no ideas for you :(");
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }

}