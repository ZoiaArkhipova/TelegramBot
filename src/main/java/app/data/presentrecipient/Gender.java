package app.data.presentrecipient;

import lombok.Getter;

/**
 * @author Evgeny Borisov
 */
@Getter
public enum Gender {
    MALE(1,"MALE"), FEMALE(2,"FEMALE");

    private final int dbCode;
    private final String code;


    Gender(int dbCode, String code) {
        this.dbCode = dbCode;
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code;
    }

    public static Gender findByDbCode(int dbCode) throws IllegalStateException {
        for (Gender gender : values()) {
            if (gender.dbCode == dbCode) {
                return gender;
            }
        }
        throw new GenderNotExistsException(dbCode + " not supported yet");
    }

    public static Gender findByCode(String code) {
        for (Gender gender : values()) {
            if (gender.code.equals(code.toUpperCase())) {
                return gender;
            }
        }
        throw new GenderNotExistsException(code + " not supported yet");
    }

}
