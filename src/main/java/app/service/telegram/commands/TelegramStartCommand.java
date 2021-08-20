package app.service.telegram.commands;

import app.data.TelegramUser;
import app.service.telegram.TelegramUserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class TelegramStartCommand extends TelegramRandomPresentBotCommand {

    private final TelegramUserService userService;

    public TelegramStartCommand(TelegramUserService userService) {
        super("start", "start using bot\n");
        this.userService = userService;
    }

    /**
     * реализованный метод класса BotCommand, в котором обрабатывается команда, введенная пользователем
     * @param absSender - отправляет ответ пользователю
     * @param user - пользователь, который выполнил команду
     * @param chat - чат бота и пользователя
     * @param strings - аргументы, переданные с командой
     */
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (userService.addUser(new TelegramUser(user, chat))) {
             sb.append("Hi! You can choose the gender \n '/choose_gender MALE' or \n'/choose_gender FEMALE' and age \n '/choose_age BABY', \n '/choose_age CHILD', \n '/choose_age TEENAGER', \n '/choose_age ADULT' or \n '/choose_age OLDSTER' of the recipient and \n  '/advice'. I will suggest you an idea for a present");
        } else {
            sb.append("You've already started bot!");
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }
}