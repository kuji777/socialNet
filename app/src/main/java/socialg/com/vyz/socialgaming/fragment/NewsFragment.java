package socialg.com.vyz.socialgaming.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import socialg.com.vyz.socialgaming.HomeActivity;
import socialg.com.vyz.socialgaming.NewsDisplayActivity;
import socialg.com.vyz.socialgaming.R;
import socialg.com.vyz.socialgaming.bean.Comment;
import socialg.com.vyz.socialgaming.bean.CommentList;
import socialg.com.vyz.socialgaming.bean.News;
import socialg.com.vyz.socialgaming.bean.Post;
import socialg.com.vyz.socialgaming.connection.UserInfo;
import socialg.com.vyz.socialgaming.event.CommentsListener;
import socialg.com.vyz.socialgaming.event.NewsListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements NewsListener , CommentsListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout newsContainer;
    private RelativeLayout loading_panel;
    private HashMap<Integer,LinearLayout> postsViews = new HashMap<Integer,LinearLayout>();
    private Integer openedView = null;

    private OnFragmentInteractionListener mListener;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        ((HomeActivity)getActivity()).addNewsListener(this);
        ((HomeActivity)getActivity()).addCommentsListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        newsContainer = view.findViewById(R.id.news_container);
        loading_panel = view.findViewById(R.id.loading_panel);
//        fillPostsViews();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private TextView generateCommentTextView( Comment comment) {
        TextView childView = new TextView(getActivity());
        SpannableStringBuilder text = new SpannableStringBuilder(comment.getPosted_by() + " " + comment.getPost_body());
        text.setSpan(new StyleSpan(Typeface.BOLD), 0, comment.getPosted_by().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        childView.setText(text);
        return childView;
    }

    private void fillCommentsView(CommentList comments, LinearLayout commentsContainerLayout) {
        TextView childView;
        for (Comment comment : comments.getComments()) {
            childView = generateCommentTextView(comment);
            commentsContainerLayout.addView(childView);
        }
    }

    private void fillPostsViews(){

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Map.Entry<Integer,Post> postEntry : ((HomeActivity)getActivity()).getPostList().entrySet() ) {

            final Post post = postEntry.getValue();

            final LinearLayout childView = (LinearLayout) layoutInflater.inflate(R.layout.single_news, null);
            ((TextView) childView.findViewById(R.id.news_title)).setText("created by " + post.getAdded_by());
            ((TextView) childView.findViewById(R.id.news_content)).setText(post.getBody());
            ((TextView) childView.findViewById(R.id.text_comments_count)).setText("Comments");
            final LinearLayout comments_layout = ((LinearLayout) childView.findViewById(R.id.comments_layout));
            ((TextView) childView.findViewById(R.id.text_comments_count)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(openedView == null){
                        comments_layout.setVisibility(View.VISIBLE);
                        openedView = post.getId();
                        if(post.getComments() == null) {
                            Log.i("COM_Ask", "Request sent for post " + post.getId());
                            ((HomeActivity) getActivity()).getComments(post.getId());
                        }
//                        else
//                            fillCommentsView(post.getComments(),comments_layout);
                    }else{
                        if(openedView == post.getId()) {
                            comments_layout.setVisibility(View.GONE);
                            openedView = null;
                        }else{
                            postsViews.get(openedView).findViewById(R.id.comments_layout).setVisibility(View.GONE);
                            comments_layout.setVisibility(View.VISIBLE);
                            openedView = post.getId();
                            if(post.getComments() == null) {
                                Log.i("COM_Ask", "Request sent for post " + post.getId());
                                ((HomeActivity) getActivity()).getComments(post.getId());
                            }
                        }
                    }
                }
            });
            ((TextView) childView.findViewById(R.id.text_likes_count)).setText("Likes("+post.getLikes()+")");

            final int id = post.getId();
            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    newsContainer.removeView(v);
//                    Intent intent = new Intent(childView.getContext(), NewsDisplayActivity.class);
//                    intent.putExtra("id", id);
//                    Log.i("TESTID", "post n°" + id);
//                    startActivity(intent);
                    NewsDisplayFragment newsDisplayFragment = new NewsDisplayFragment();
                    Bundle args = new Bundle();
                    args.putInt("id",id);
                    newsDisplayFragment.setArguments(args);
                    ((HomeActivity)getActivity()).viewDisplayed();

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_fragment,newsDisplayFragment,null).addToBackStack(null).commit();
                }
            });
            newsContainer.addView(childView);
            postsViews.put(post.getId(),childView);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    @Override
    public void onNewsResult() {
        loading_panel.setVisibility(View.GONE);
        fillPostsViews();
    }

    private Handler handler = new Handler(Looper.getMainLooper());//TODO Not necessary with runOnUIThread

    @Override
    public void onCommentsResult(int postId, final CommentList comments) {
        final LinearLayout postView = postsViews.get(postId);

        postView.findViewById(R.id.loading_comments_panel).setVisibility(View.GONE);
        final LinearLayout commentsContainerLayout = postView.findViewById(R.id.comments_layout);
        Log.i("COM_RES", "Received comments for post " + postId);
//        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*getActivity().runOnUiThread */handler.post(new Runnable() {
            @Override
            public void run() {
                TextView childView;
                if(comments.getComments().size() == 0 ){
                    childView = new TextView(getActivity());
                    childView.setText("No comments");
                    childView.setGravity(Gravity.CENTER);
                    childView.setTextColor(getActivity().getResources().getColor(R.color.greyColor));
                    commentsContainerLayout.addView(childView);
                }else {
                    fillCommentsView(comments, commentsContainerLayout);
                }
            }
        });

    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
