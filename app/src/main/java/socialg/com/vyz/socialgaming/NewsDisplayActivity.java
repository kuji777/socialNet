package socialg.com.vyz.socialgaming;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import socialg.com.vyz.socialgaming.bean.Post;

public class NewsDisplayActivity extends AppCompatActivity {

    private TextView newsTitle;
    private TextView newsContent;
    private TextView commentsCount;
    private TextView likesCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_display_actvitity);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
//        Post post = HomeActivity.getPost(id);

        newsTitle = findViewById(R.id.news_title);
        newsContent = findViewById(R.id.news_content);
        commentsCount = findViewById(R.id.text_comments_count);
        likesCount = findViewById(R.id.text_likes_count);

//        newsTitle.setText("Created by "+post.getAdded_by());
//        newsContent.setText(post.getBody());
//        likesCount.setText("Comments("+post.getLikes()+")");
    }
}
