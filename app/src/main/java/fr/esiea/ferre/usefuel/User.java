package fr.esiea.ferre.usefuel;

/**
 * Created by rob on 5/29/2017.
 */

public class User {

    private String username;
    private String mail;

    public User(){}

    public User(String p_name, String p_mail){
        username = p_name;
        mail = p_mail;
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
}

