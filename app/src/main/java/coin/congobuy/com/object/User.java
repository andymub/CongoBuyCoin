package coin.congobuy.com.object;

/**
 * Created by Andymub on 06/07/2018.
 */
import java.util.HashMap;
import java.util.Map;

public class User {

    private String id;
    private String emailUser;
    private String numberUser;

    public User() {
    }

    public User(String id, String emailUser, String numberUser) {
        this.id = id;
        this.emailUser = emailUser;
        this.numberUser = numberUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getNumberUser() {
        return numberUser;
    }

    public void setNumberUser(String numberUser) {
        this.numberUser = numberUser;
    }

    public Map<String, Object> toMapUser () {

        HashMap<String, Object> result = new HashMap<>();
        result.put("emailUser", this.emailUser);
        result.put("numberUser", this.numberUser);

        return result;
    }
}
