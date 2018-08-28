package socialg.com.vyz.socialgaming.bean;

/**
 * Created by Vincent on 14/07/2018.
 */

public class Team {

    private int id;
    private int groupId;
    private int chatId;
    private String creationDate;

    public Team(int id, int groupId, int chatId, String creationDate) {
        this.id = id;
        this.groupId = groupId;
        this.chatId = chatId;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
