package socialg.com.vyz.socialgaming.event;

import socialg.com.vyz.socialgaming.bean.CommentList;

/**
 * Created by Vincent on 09/11/2018.
 */

public interface CommentsListener{

    void onCommentsResult(int postId, CommentList comments);
}
