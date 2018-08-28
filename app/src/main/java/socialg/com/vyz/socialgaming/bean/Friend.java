package socialg.com.vyz.socialgaming.bean;

/**
 * Created by Vincent on 11/07/2018.
 */

public class Friend {

    private String id_user;
    private String first_name;
    private String last_name;
    private String pseudo;
    private int id;

    public Friend(){

    }

    public Friend(String id_user, String first_name, String last_name, String pseudo) {
        this.id_user = id_user;
        this.first_name = first_name;
        this.last_name = last_name;
        this.pseudo = pseudo;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    @Override
    public String toString() {
        return "Friend{" +
                "id_user='" + id_user + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", pseudo='" + pseudo + '\'' +
                '}';
    }
}
