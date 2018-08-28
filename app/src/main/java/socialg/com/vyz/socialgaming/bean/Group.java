package socialg.com.vyz.socialgaming.bean;

public class Group {

    private String id_game;
    private String name;

    public Group(){

    }

    public Group(String id_game, String name) {
        this.id_game = id_game;
        this.name = name;
    }

    public String getId_game() {
        return id_game;
    }

    public void setId_game(String id_game) {
        this.id_game = id_game;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id_game='" + id_game + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
