package socialg.com.vyz.socialgaming.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Vincent on 09/11/2018.
 */

public class CommentList {

    private HashMap<Integer,Comment> comments = new HashMap<Integer,Comment> ();

    public CommentList(){

    }

    public HashMap<Integer,Comment> getComments(){
        return comments;
    }

    public Comment getComment(Integer num){
        return comments.get(num);
    }

    public void addComment(int postId, Comment comment){
        comments.put(postId,comment);
    }

    public void removeComment(int postId){
        comments.remove(postId);
    }


}
