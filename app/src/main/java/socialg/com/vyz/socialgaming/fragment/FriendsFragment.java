package socialg.com.vyz.socialgaming.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import socialg.com.vyz.socialgaming.AddFriendActivity;
import socialg.com.vyz.socialgaming.FriendActivity;
import socialg.com.vyz.socialgaming.LoginActivity;
import socialg.com.vyz.socialgaming.ProfileWallActivity;
import socialg.com.vyz.socialgaming.R;
import socialg.com.vyz.socialgaming.bean.Friend;
import socialg.com.vyz.socialgaming.connection.CustomRequest;
import socialg.com.vyz.socialgaming.connection.DBConnection;
import socialg.com.vyz.socialgaming.connection.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // url to get info
    private static String url_get_friend_info = "http://"+ LoginActivity.currentIp+"/socialgaming/get_user_details.php";

    private LinearLayout profileContainer;
    private Button addFriendButton;

    private OnFragmentInteractionListener mListener;

    public FriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        List<Friend> friendList = UserInfo.getInstance().getFriendList();

        profileContainer = view.findViewById(R.id.profile_container);
        addFriendButton = view.findViewById(R.id.button_add_friend);

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddFriendActivity.class);
                startActivity(intent);
            }
        });

        Log.i("TestProfiledisplay",String.valueOf(friendList.size()+" amis dans le profil"));
        for (Friend friend : friendList ) {

            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final LinearLayout childView = (LinearLayout) layoutInflater.inflate(R.layout.single_profile, null);
            ((TextView) childView.findViewById(R.id.single_view_title)).setText(friend.getPseudo());

            final String id = friend.getId_user();
            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    profileContainer.removeView(v);

                    CustomRequest requestFriend = new CustomRequest(Request.Method.GET, url_get_friend_info+"?pid="+id, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray jarrayFriends = response.getJSONArray("user");
                                        JSONObject json = jarrayFriends.getJSONObject(0);

                                        Gson gson = new GsonBuilder().create();
                                        // Storing each json item in variable
                                        Friend friend = gson.fromJson(json.toString(), Friend.class);
                                        Log.i("TEST_FRIEND", friend.toString());
                                        Toast.makeText(getActivity(), "Data from "+friend.getPseudo()+" are settled",Toast.LENGTH_SHORT).show();


//                                        Intent intent = new Intent(childView.getContext(), FriendActivity.class);
//                                        Bundle bundle = new Bundle();
//                                        bundle.putSerializable("friend", friend);
//                                        intent.putExtras(bundle);
//                                        Log.i("TESTID", "signal : n°" + id);
//                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            // Le code suivant est appelé lorsque Volley n'a pas réussi à récupérer le résultat de la requête
                        }
                    });
                    requestFriend.setTag(this);
                    // On ajoute la Request au RequestQueue pour la lancer
                    DBConnection.getInstance(getActivity()).getVolleyRequestQueue().add(requestFriend);

                }
            });
            profileContainer.addView(childView);
        }
        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        profileContainer.removeAllViews();
        List<Friend> friendList = UserInfo.getInstance().getFriendList();
        Log.i("TestProfiledisplay",String.valueOf(friendList.size()+" amis dans le profil"));
        for (Friend friend : friendList ) {

            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final LinearLayout childView = (LinearLayout) layoutInflater.inflate(R.layout.single_profile, null);
            ((TextView) childView.findViewById(R.id.single_view_title)).setText(friend.getPseudo());

            final String id = friend.getId_user();
            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    profileContainer.removeView(v);

                    Toast.makeText(getActivity(), "REquest Data of "+id+"are settled",Toast.LENGTH_SHORT).show();

                    CustomRequest requestFriend = new CustomRequest(Request.Method.GET, url_get_friend_info+"?pid="+id, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONObject json = response.getJSONObject("user");
//                                        JSONObject json = jarrayFriends.getJSONObject(0);

                                        Gson gson = new GsonBuilder().create();
                                        // Storing each json item in variable
                                        Friend friend = gson.fromJson(json.toString(), Friend.class);
                                        Log.i("TEST_FRIEND", friend.toString());
                                        Toast.makeText(getActivity(), "Data from "+friend.getPseudo()+" are settled",Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(childView.getContext(), FriendActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("friend", friend);

                                        intent.putExtras(bundle);
                                        Log.i("TESTID", "signal : n°" + id);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            // Le code suivant est appelé lorsque Volley n'a pas réussi à récupérer le résultat de la requête
                        }
                    });
                    requestFriend.setTag(this);
                    // On ajoute la Request au RequestQueue pour la lancer
                    DBConnection.getInstance(getActivity()).getVolleyRequestQueue().add(requestFriend);
                }
            });
            profileContainer.addView(childView);
        }
    }
}
