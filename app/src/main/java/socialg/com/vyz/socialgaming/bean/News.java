package socialg.com.vyz.socialgaming.bean;

/**
 * Created by Vincent on 24/06/2018.
 */

public class News {

    private String title;
    private String content;
    private int id;

    public News(){

    }

    public News(String title, String content){
        this.content = content;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
