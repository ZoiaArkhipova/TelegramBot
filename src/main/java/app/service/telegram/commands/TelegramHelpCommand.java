package app.service.telegram.commands;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class TelegramHelpCommand extends TelegramRandomPresentBotCommand {

    private final ICommandRegistry commandRegistry;

    public TelegramHelpCommand(ICommandRegistry commandRegistry) {
        super("help", "list all known commands\n");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder helpMessageBuilder = new StringBuilder("<b>Available commands:</b>");

        commandRegistry.getRegisteredCommands().forEach(cmd -> helpMessageBuilder.append(cmd.toString()).append("\n"));

        SendMessage helpMessage = new SendMessage();
        helpMessage.setChatId(chat.getId().toString());
        helpMessage.enableHtml(true);
        helpMessage.setText(helpMessageBuilder.toString());

        execute(absSender, helpMessage, user);
    }
}