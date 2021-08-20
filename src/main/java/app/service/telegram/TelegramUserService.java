package app.service.telegram;

import app.data.TelegramUser;
import app.data.presentrecipient.Age;
import app.data.presentrecipient.Gender;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
public final class TelegramUserService {

    private final Set<TelegramUser> users;

    public TelegramUserService() {
        users = new HashSet<>();
    }

    public void setUserGender(User user, Gender gender) {
        users.stream().filter(a -> a.getUser().equals(user)).forEach(a -> a.getGiftRecipient().setGender(gender));
    }

    public void setUserAge(User user, Age age) {
        users.stream().filter(a -> a.getUser().equals(user)).forEach(a -> a.getGiftRecipient().setAge(age));
    }

    public boolean removeUser(User user) {
        return users.removeIf(a -> a.getUser().equals(user));
    }

    public boolean addUser(TelegramUser telegramUser) {
        return users.add(telegramUser);
    }

    public boolean hasUser(User user) {
        return users.stream().anyMatch(a -> a.getUser().equals(user));
    }

    public Optional<TelegramUser> getUser(User user) {
        return users.stream().filter(a -> a.getUser().equals(user)).findFirst();
    }

    public Stream<TelegramUser> users() {
        return users.stream();
    }

}