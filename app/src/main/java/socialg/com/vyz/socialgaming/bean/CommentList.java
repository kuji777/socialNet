package socialg.com.vyz.socialgaming.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 09/11/2018.
 */

public class CommentList {

    private List<Comment> comments = new ArrayList<Comment>();

    public CommentList(){

    }

    public List<Comment> getComments(){
        return comments;
    }

    public void addComment(Comment comment){
        comments.add(comment);
    }

    public void removeComment(Comment comment){
        comments.remove(comment);
    }


}
