package socialg.com.vyz.socialgaming.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Map;

import socialg.com.vyz.socialgaming.HomeActivity;
import socialg.com.vyz.socialgaming.NewsDisplayActivity;
import socialg.com.vyz.socialgaming.R;
import socialg.com.vyz.socialgaming.bean.News;
import socialg.com.vyz.socialgaming.bean.Post;
import socialg.com.vyz.socialgaming.connection.UserInfo;
import socialg.com.vyz.socialgaming.event.NewsListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements NewsListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout newsContainer;
    private RelativeLayout loading_panel;

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

    private void fillPostsViews(){

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Map.Entry<Integer,Post> postEntry : ((HomeActivity)getActivity()).getPostList().entrySet() ) {

            Post post = postEntry.getValue();

            final LinearLayout childView = (LinearLayout) layoutInflater.inflate(R.layout.single_news, null);
            ((TextView) childView.findViewById(R.id.news_title)).setText("created by " + post.getAdded_by());
            ((TextView) childView.findViewById(R.id.news_content)).setText(post.getBody());
            ((TextView) childView.findViewById(R.id.text_comments_count)).setText(post.getBody());
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
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_fragment,newsDisplayFragment,null).addToBackStack(null).commit();
                }
            });
            newsContainer.addView(childView);
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
