package app.data.presentrecipient;

import lombok.Getter;

@Getter
public enum Age {
    CHILD_IN_ARMS(1,"BABY"),
    CHILD(2,"CHILD"),
    TEENAGER(3,"TEENAGER"),
    ADULT(4,"ADULT"),
    OLDSTER(5,"OLDSTER");

    private final int dbCode;
    private final String code;

    Age(int dbCode, String code) {
        this.code = code;
        System.out.println(code);
        this.dbCode = dbCode;
    }

    @Override
    public String toString() {
        return this.code;
    }

    public static Age findByDbCode(int dbCode){
        for (Age age : values()) {
            if (age.dbCode == dbCode) {
                return age;
            }
        }
        throw  new AgeNotExistsException(dbCode + " not supported yet");
    }


    public static Age findByCode(String code){
        for (Age age : values()) {
            if (age.code.equals(code.toUpperCase())) {
                return age;
            }
        }
        throw  new AgeNotExistsException(code + " not supported yet");
    }


}
