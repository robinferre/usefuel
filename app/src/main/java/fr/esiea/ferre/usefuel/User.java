package fr.esiea.ferre.usefuel;

/**
 * Created by rob on 5/29/2017.
 */

public class User {

    private String username;
    private String mail;
    private String type;

    public User(){}

    public User(String p_name, String p_mail, String p_type){
        username = p_name;
        mail = p_mail;
        type = p_type;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String p_name) {
        username = p_name;
    }

    public String getMail() {
        return mail;
    }
    public void setMail(String p_mail) {
        mail = p_mail;
    }

    public String getType() {
        return type;
    }
    public void setType(String p_type) {
        type = p_type;
    }
}

