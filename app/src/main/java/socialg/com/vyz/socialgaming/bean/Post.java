package socialg.com.vyz.socialgaming.bean;

/**
 * Created by Vincent on 04/11/2018.
 */

public class Post {

    private int id;
    private int likes;
    private String body ;
    private String added_by ;
    private String user_to;
    private String date_added;
    private String user_closed;
    private String deleted;

    public Post(){

    }

    public Post(int id, int likes, String body, String added_by, String user_to, String date_added, String user_closed, String deleted) {
        this.id = id;
        this.likes = likes;
        this.body = body;
        this.added_by = added_by;
        this.user_to = user_to;
        this.date_added = date_added;
        this.user_closed = user_closed;
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getUser_to() {
        return user_to;
    }

    public void setUser_to(String user_to) {
        this.user_to = user_to;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getUser_closed() {
        return user_closed;
    }

    public void setUser_closed(String user_closed) {
        this.user_closed = user_closed;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", likes=" + likes +
                ", body='" + body + '\'' +
                ", added_by='" + added_by + '\'' +
                ", user_to='" + user_to + '\'' +
                ", date_added='" + date_added + '\'' +
                ", user_closed='" + user_closed + '\'' +
                ", deleted='" + deleted + '\'' +
                '}';
    }
}
