package socialg.com.vyz.socialgaming.bean;

/**
 * Created by Vincent on 09/11/2018.
 */

public class Comment {

    private int id;
    private int post_id;
    private String post_body;
    private String posted_by;
    private String posted_to;
    private String date_added;
    private String removed;

    public Comment(){
    }

    public Comment(int id, int post_id, String post_body, String posted_by, String posted_to, String date_added, String removed) {
        this.id = id;
        this.post_id = post_id;
        this.post_body = post_body;
        this.posted_by = posted_by;
        this.posted_to = posted_to;
        this.date_added = date_added;
        this.removed = removed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getPost_body() {
        return post_body;
    }

    public void setPost_body(String post_body) {
        this.post_body = post_body;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public void setPosted_by(String posted_by) {
        this.posted_by = posted_by;
    }

    public String getPosted_to() {
        return posted_to;
    }

    public void setPosted_to(String posted_to) {
        this.posted_to = posted_to;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getRemoved() {
        return removed;
    }

    public void setRemoved(String removed) {
        this.removed = removed;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", post_id=" + post_id +
                ", post_body='" + post_body + '\'' +
                ", posted_by='" + posted_by + '\'' +
                ", posted_to='" + posted_to + '\'' +
                ", date_added='" + date_added + '\'' +
                ", removed='" + removed + '\'' +
                '}';
    }
}
