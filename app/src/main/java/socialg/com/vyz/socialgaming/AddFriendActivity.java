package socialg.com.vyz.socialgaming;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import socialg.com.vyz.socialgaming.bean.Friend;
import socialg.com.vyz.socialgaming.connection.CustomRequest;
import socialg.com.vyz.socialgaming.connection.DBConnection;
import socialg.com.vyz.socialgaming.connection.UserInfo;

public class AddFriendActivity extends AppCompatActivity {

    private Button searchFriendButton;
    private EditText friendPseudoText;
    private LinearLayout friendViewContainer;

    private static String url_response_friend = "http://"+LoginActivity.currentIp+"/socialgaming/response_friend.php";
    private static String url_search_user_by_pseudo = "http://"+LoginActivity.currentIp+"/socialgaming/search_user_pseudo.php";
    private static String url_send_request = "http://"+LoginActivity.currentIp+"/socialgaming/friend_request.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);


        searchFriendButton = findViewById(R.id.button_search_friend);
        friendPseudoText = findViewById(R.id.search_friend_name);
        friendViewContainer = findViewById(R.id.friend_view_container);

        if (UserInfo.getInstance().getFriendRequestLit().size() != 0){
            for(final Friend friend : UserInfo.getInstance().getFriendRequestLit()){
                LayoutInflater layoutInflater = (LayoutInflater) this.getApplicationContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final LinearLayout childView = (LinearLayout) layoutInflater.inflate(R.layout.add_friend_view, null);
                ((TextView) childView.findViewById(R.id.single_view_title)).setText(friend.getPseudo());

                final String id = friend.getId_user();
                ((Button) childView.findViewById(R.id.refuse_friend_button)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friendViewContainer.removeView(childView);
                        //Send the request
                        CustomRequest requestFriend = new CustomRequest(Request.Method.GET, url_response_friend+"?response=false&id_user_request="+friend.getId_user()+"&id_user_accept="+UserInfo.getInstance().getId(), null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(AddFriendActivity.this,"Request successfully refused ~",Toast.LENGTH_SHORT).show();
                                        UserInfo.getInstance().updateFriendRequest();
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
                        DBConnection.getInstance(AddFriendActivity.this).getVolleyRequestQueue().add(requestFriend);
                    }
                });

                ((Button) childView.findViewById(R.id.accept_friend_button)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friendViewContainer.removeView(childView);
                        CustomRequest requestFriend = new CustomRequest(Request.Method.GET, url_response_friend+"?response=true&id_user_request="+friend.getId_user()+"&id_user_accept="+UserInfo.getInstance().getId(), null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(AddFriendActivity.this,friend.getPseudo()+" and you are now friends ~",Toast.LENGTH_SHORT).show();
                                        UserInfo.getInstance().updateFriendRequest();
                                        UserInfo.getInstance().updateFriends();
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
                        DBConnection.getInstance(AddFriendActivity.this).getVolleyRequestQueue().add(requestFriend);
                    }
                });
                friendViewContainer.addView(childView);
            }
        }


        searchFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = friendPseudoText.getText().toString();
                if(tag != "") {
                    if(tag.contains("#")){
                        String friendId = tag.substring(tag.lastIndexOf("#") + 1);
                        String friendPseudo = tag.substring(0, tag.lastIndexOf("#"));
                        Log.i("TEST_FRIEND", "BACK TAG is " + friendId);
                        Log.i("TEST_FRIEND", "FRONT TAG is " + friendPseudo);

                        //Send the request
                        CustomRequest requestFriend = new CustomRequest(Request.Method.GET, url_search_user_by_pseudo+"?pseudo="+friendPseudo, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(AddFriendActivity.this,"Request successfully sent ~",Toast.LENGTH_SHORT).show();

                                        try {
                                            int success = response.getInt("success");
                                            Log.i("TEST_FRIEND",response.toString());
                                            if (success == 1) {
                                                // successfully created product

                                                JSONArray jarrayFriends = response.getJSONArray("user");
//                                                Log.i("TEST_FRIEND","get for "+id +" friends : "+jarrayFriends.toString());
                                                Gson gson = new GsonBuilder().create();

                                                for (int i = 0; i < jarrayFriends.length(); i++) {
                                                    JSONObject json = jarrayFriends.getJSONObject(i);

                                                    // Storing each json item in variable
                                                    final Friend friend = gson.fromJson(json.toString(), Friend.class);

                                                    LayoutInflater layoutInflater = (LayoutInflater) AddFriendActivity.this.getApplicationContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                                    final LinearLayout childView = (LinearLayout) layoutInflater.inflate(R.layout.add_friend_view, null);
                                                    ((TextView) childView.findViewById(R.id.single_view_title)).setText(friend.getPseudo());
                                                    ((Button) childView.findViewById(R.id.refuse_friend_button)).setVisibility(View.GONE);
                                                    ((Button) childView.findViewById(R.id.accept_friend_button)).setText("Add");
                                                    ((Button) childView.findViewById(R.id.accept_friend_button)).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            friendViewContainer.removeView(v);
                                                            UserInfo.getInstance().updateFriends();
                                                            CustomRequest requestFriend = new CustomRequest(Request.Method.GET, url_send_request+"?id_user="+UserInfo.getInstance().getId()+"&id_user_invite="+friend.getId_user(), null,
                                                                    new Response.Listener<JSONObject>() {
                                                                        @Override
                                                                        public void onResponse(JSONObject response) {
                                                                            Toast.makeText(AddFriendActivity.this,"Request successfully sent to "+friend.getPseudo()+" ~",Toast.LENGTH_SHORT).show();
                                                                            UserInfo.getInstance().updateFriendRequest();
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
                                                            DBConnection.getInstance(AddFriendActivity.this).getVolleyRequestQueue().add(requestFriend);
                                                        }
                                                    });
                                                    friendViewContainer.addView(childView);
                                                }
                                                // closing this screen
                                            }
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
                        DBConnection.getInstance(AddFriendActivity.this).getVolleyRequestQueue().add(requestFriend);

                    }else{
                        Toast.makeText(AddFriendActivity.this,"Syntax not correct, put the pseudo # and id",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserInfo.getInstance().updateFriendRequest();
    }
}
