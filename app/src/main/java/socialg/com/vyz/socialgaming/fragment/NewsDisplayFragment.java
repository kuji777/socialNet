package socialg.com.vyz.socialgaming.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import socialg.com.vyz.socialgaming.HomeActivity;
import socialg.com.vyz.socialgaming.R;
import socialg.com.vyz.socialgaming.bean.Post;

/**
 * Created by Vincent on 08/11/2018.
 */


public class NewsDisplayFragment  extends android.support.v4.app.Fragment{


    // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout newsContainer;
    private RelativeLayout loading_panel;

    private NewsFragment.OnFragmentInteractionListener mListener;

    private TextView newsTitle;
    private TextView newsContent;
    private TextView commentsCount;
    private TextView likesCount;

    public NewsDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_news_display_actvitity, container, false);

        int id = getArguments().getInt("id");
        Post post = ((HomeActivity)getActivity()).getPost(id);

        newsTitle = view.findViewById(R.id.news_title);
        newsContent = view.findViewById(R.id.news_content);
        commentsCount = view.findViewById(R.id.text_comments_count);
        likesCount = view.findViewById(R.id.text_likes_count);

        newsTitle.setText("Created by "+post.getAdded_by());
        newsContent.setText(post.getBody());
        likesCount.setText("Comments("+post.getLikes()+")");

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewsFragment.OnFragmentInteractionListener) {
            mListener = (NewsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity)getActivity()).updatePosts();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
