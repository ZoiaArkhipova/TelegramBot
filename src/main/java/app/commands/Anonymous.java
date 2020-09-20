package app.commands;

import app.data.presentrecipient.PresentRecipient;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

public final class Anonymous {

    private static final String USER_CHAT_CANNOT_BE_NULL = "User or chat cannot be null!";

    private final User mUser;
    private final Chat mChat;
    private String mDisplayedName;
    private PresentRecipient mPresentRecipient;

    public Anonymous(User user, Chat chat) {
        if (user == null || chat == null) {
            throw new IllegalStateException(USER_CHAT_CANNOT_BE_NULL);
        }
        mUser = user;
        mChat = chat;
        mPresentRecipient = new PresentRecipient();
    }

    @Override
    public int hashCode() {
        return mUser.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Anonymous && ((Anonymous) obj).getUser().equals(mUser);
    }

    public User getUser() {
        return mUser;
    }

    public Chat getChat() {
        return mChat;
    }

    public String getDisplayedName() {
        return mDisplayedName;
    }

    public PresentRecipient getGiftRecipient() {  return mPresentRecipient; }

    public void setDisplayedName(String displayedName) {
        mDisplayedName = displayedName;
    }
}