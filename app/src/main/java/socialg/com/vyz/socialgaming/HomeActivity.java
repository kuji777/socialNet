package socialg.com.vyz.socialgaming;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import socialg.com.vyz.socialgaming.bean.Comment;
import socialg.com.vyz.socialgaming.bean.CommentList;
import socialg.com.vyz.socialgaming.bean.Friend;
import socialg.com.vyz.socialgaming.bean.Group;
import socialg.com.vyz.socialgaming.bean.Post;
import socialg.com.vyz.socialgaming.bean.Team;
import socialg.com.vyz.socialgaming.connection.CustomRequest;
import socialg.com.vyz.socialgaming.connection.DBConnection;
import socialg.com.vyz.socialgaming.connection.UserInfo;
import socialg.com.vyz.socialgaming.event.CommentsListener;
import socialg.com.vyz.socialgaming.event.NewsListener;
import socialg.com.vyz.socialgaming.fragment.FriendsFragment;
import socialg.com.vyz.socialgaming.fragment.GroupsFragment;
import socialg.com.vyz.socialgaming.fragment.NewsFragment;

public class HomeActivity extends AppCompatActivity implements FriendsFragment.OnFragmentInteractionListener, GroupsFragment.OnFragmentInteractionListener , NewsFragment.OnFragmentInteractionListener{

    //User infos
    private static String id;
    private String first_name;
    private String last_name;
    private String username;
    private String age;
    private String password;
    private String email;
    private String nation;
    private String sexe;
    private String signup_date;

    //private static UserInfo instance = null;
    private static HomeActivity instance = null;

    //Connection Data
    private static String currentIp = LoginActivity.currentIp;

//    private static String url_get_friend = "http://"+currentIp+"/socialgaming/liste_amis.php";
//    private static String url_get_group = "http://"+currentIp+"/socialgaming/liste_jeux_abo.php";
//    private static String url_get_team = "http://"+currentIp+"/socialgaming/get_user_teams.php";
    private static String url_get_friend_request = "http://"+currentIp+"/socialgaming/liste_demande_ami.php";
    private static String url_create_product = "http://"+currentIp+"/api-sg/v1/users/login";
    private static String url_get_posts = "http://"+currentIp+"/api-sg/v1/posts";
    private static String url_get_comments = "http://"+currentIp+"/api-sg/v1/comments";
    private static String url_send_post = "http://"+currentIp+"/api-sg/v1/posts";
    private static String url_send_comment = "http://"+currentIp+"/api-sg/v1/comments";

    private static final String TAG_SUCCESS = "status";

    //Data collections
    public  List<Friend> friendList = new ArrayList<Friend>();
    private List<Group> groupList = new ArrayList<Group>();
    private List<Team> teamList = new ArrayList<Team>();
    private static List<Friend> friendRequestLit = new ArrayList<Friend>();
    private HashMap<Integer,Post> postHashMap = new HashMap<Integer,Post>();

    //Listener collections
    private List<NewsListener> newsListeners = new ArrayList<NewsListener>();
    private List<CommentsListener> commentsListeners = new ArrayList<CommentsListener>();

    //HomeActivity Layout elements
    private FrameLayout mainFrame;
    private BottomNavigationView bottomMenu;

    //Activity variables
    private boolean viewOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mainFrame = findViewById(R.id.main_frame_fragment);
        setInstance(this);

        UserInfo userInfo = UserInfo.getInstance();
        id = userInfo.getId();
        first_name = userInfo.getFirstName();
        last_name = userInfo.getLastName();
        username = userInfo.getPseudo();
        email = userInfo.getEmail();
        signup_date = userInfo.getSignup_date();

        getInfo();

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        NewsFragment newsFragment = new NewsFragment();
        fragmentTransaction.replace(R.id.main_frame_fragment,newsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        bottomMenu = findViewById(R.id.bottom_navigation);
        bottomMenu.setSelectedItemId(R.id.news_action);
        bottomMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected( MenuItem item) {
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        switch (item.getItemId()) {
                            case R.id.news_action:
                                NewsFragment newsFragment = new NewsFragment();
                                fragmentTransaction2.replace(R.id.main_frame_fragment,newsFragment);
                                fragmentTransaction2.addToBackStack(null);
                                fragmentTransaction2.commit();
                                break;
                            case R.id.groups_action:
                                GroupsFragment groupsFragment = new GroupsFragment();
                                fragmentTransaction2.replace(R.id.main_frame_fragment,groupsFragment);
                                fragmentTransaction2.addToBackStack(null);
                                fragmentTransaction2.commit();
                                break;
                            case R.id.friends_action:
                                FriendsFragment friendsFragment = new FriendsFragment();
                                fragmentTransaction2.replace(R.id.main_frame_fragment,friendsFragment);
                                fragmentTransaction2.addToBackStack(null);
                                fragmentTransaction2.commit();
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getInfo() {
        updatePosts();
//        updateFriends();
//        updateGroup();
//        updateFriendRequest();
    }

    public void updatePosts(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("pid",id);

        CustomRequest requestGroup = new CustomRequest(Request.Method.GET, url_get_posts, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt(TAG_SUCCESS);
                            if (success == 1) {

                                JSONArray jarrayGroups = response.getJSONArray("result");
                                Gson gson = new GsonBuilder().create();

                                for (int i = 0; i < jarrayGroups.length(); i++) {
                                    JSONObject json = jarrayGroups.getJSONObject(i);
                                    // Storing each json item in variable
                                    Post post = gson.fromJson(json.toString(), Post.class);
                                    Log.i("TEST_POST","json "+i+" "+post.toString());
                                    // adding HashList to ArrayList
                                    postHashMap.put(post.getId(),post);
                                }
                                for(NewsListener newsListener : newsListeners){
                                    newsListener.onNewsResult();
                                }
                            } else {
                                Log.i("POSTS" , response.getString("result"));
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
        requestGroup.setTag(this);

        // On ajoute la Request au RequestQueue pour la lancer
        DBConnection.getInstance(this).getVolleyRequestQueue().add(requestGroup);

    }

    public void getComments(final int postId){
        CustomRequest requestGroup = new CustomRequest(Request.Method.GET, url_get_comments+"/"+postId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt(TAG_SUCCESS);
                            CommentList commentList = new CommentList();

                            if (success == 1) {

                                JSONArray jarrayGroups = response.getJSONArray("result");
                                Gson gson = new GsonBuilder().create();

                                for (int i = 0; i < jarrayGroups.length(); i++) {
                                    JSONObject json = jarrayGroups.getJSONObject(i);
                                    // Storing each json item in variable
                                    Comment comment = gson.fromJson(json.toString(), Comment.class);
                                    commentList.addComment(comment.getId(),comment);
                                    Log.i("TEST_COMMENT","json "+i+" "+comment.toString());
//                                    postHashMap.put(comment.getId(),comment);
                                }
                                postHashMap.get(postId).setComments(commentList);
                                for(CommentsListener commentListener : commentsListeners){
                                    commentListener.onCommentsResult(postId,commentList);
                                }
                            } else {
                                Log.i("COMMENT" , response.getString("message"));
                                for(CommentsListener commentListener : commentsListeners){
                                    commentListener.onCommentsResult(postId,commentList);
                                }
                            }
                            postHashMap.get(postId).setComments(commentList);
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
        requestGroup.setTag(this);

        // On ajoute la Request au RequestQueue pour la lancer
        DBConnection.getInstance(this).getVolleyRequestQueue().add(requestGroup);

    }

    public void sendPost(String body, String added_by , String user_to , String date_added){

        Map<String, String> params = new HashMap<String, String>();
        params.put("body",body);
        params.put("added_by",added_by);
        params.put("user_to",user_to);
        params.put("date_added",date_added);

        CustomRequest sendpost = new CustomRequest(Request.Method.POST, url_send_post , params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt(TAG_SUCCESS);
                            JSONObject json = response.getJSONObject("result");
                            Log.i("TEST_POST", "res for successful post : " + json.toString());
                            if (success == 1) {
//                                // successfully created product
                                Gson gson = new GsonBuilder().create();

                                Log.i("TEST_POST", "res for successful post : " + json.toString());
                                // Storing each json item in variable
                                Post post = gson.fromJson(json.toString(), Post.class);
                                postHashMap.put(post.getId(),post);
                                //TODO post return event maybe
                            } else {
//                                Log.e("TEST_POST", "res for failed post : " + json.toString());
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
        sendpost.setTag(this);

        // On ajoute la Request au RequestQueue pour la lancer
        DBConnection.getInstance(this).getVolleyRequestQueue().add(sendpost);

    }

    public void sendComment(String body, String comment_by , String user_to , String date_added , String post_id) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("post_body", body);
        params.put("posted_by", comment_by);
        params.put("posted_to", user_to);
        params.put("date_added", date_added);
        params.put("post_id", post_id);

            CustomRequest request = new CustomRequest(Request.Method.POST, url_send_comment, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Ce code est appelé quand la requête réussi. Étant ici dans le thread principal, on va pouvoir mettre à jour notre Adapter
//                        mAdapter.updateMembers(jsonArray);
                            try {
                                int success = response.getInt(TAG_SUCCESS);
                                Gson gson = new GsonBuilder().create();

                                if (success == 1) {
                                    // successfully created product
                                    Toast.makeText(HomeActivity.this, "comment Sent!!", Toast.LENGTH_SHORT).show();
                                    JSONObject json = response.getJSONObject("result");
                                    Comment comment = gson.fromJson(json.toString(), Comment.class);
                                    postHashMap.get(comment.getPost_id()).getComments().addComment(comment.getId(),comment);
                                    //TODO comment return event maybe
                                } else {
                                    Toast.makeText(HomeActivity.this, "comment not sent !!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    // Le code suivant est appelé lorsque Volley n'a pas réussi à récupérer le résultat de la requête
                    Toast.makeText(HomeActivity.this, "Error while getting JSON: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            request.setTag(this);

            // On ajoute la Request au RequestQueue pour la lancer
            DBConnection.getInstance(HomeActivity.this).getVolleyRequestQueue().add(request);
        }

    /*public void updateFriends(){
        friendList.clear();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pid",id);

        Log.i("TEST_FRIEND","get friends of "+id +" "+params.toString());

        CustomRequest requestFriend = new CustomRequest(Request.Method.GET, url_get_friend+"?pid="+params.get("pid") , params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt(TAG_SUCCESS);
                            Log.i("TEST_FRIEND","get for "+id +" "+response.toString());
                            if (success == 1) {
                                // successfully created product

                                JSONArray jarrayFriends = response.getJSONArray("user");
                                Log.i("TEST_FRIEND","get for "+id +" friends : "+jarrayFriends.toString());
                                Gson gson = new GsonBuilder().create();

                                for (int i = 0; i < jarrayFriends.length(); i++) {
                                    JSONObject json = jarrayFriends.getJSONObject(i);

                                    // Storing each json item in variable
                                    Friend friend = gson.fromJson(json.toString(), Friend.class);

                                    Log.i("TEST_FRIEND","get "+friend.toString());
                                    // adding each child node to HashMap key => value

                                    // adding HashList to ArrayList
                                    friendList.add(friend);
                                }
                                // closing this screen
                            } else {
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
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pid",id);
                return params;
            }
        };
        requestFriend.setTag(this);

        // On ajoute la Request au RequestQueue pour la lancer
        DBConnection.getInstance(this).getVolleyRequestQueue().add(requestFriend);

    }
    */
    /*public void updateGroup(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("pid",id);

        CustomRequest requestGroup = new CustomRequest(Request.Method.POST, url_get_group, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt(TAG_SUCCESS);
                            Log.i("TEST_GROUP","get for "+id +" "+response.toString());
                            if (success == 1) {
                                // successfully created product

                                JSONArray jarrayGroups = response.getJSONArray("game");
                                Gson gson = new GsonBuilder().create();

//                                Log.i("TEST_GROUP","ARRAY GROUTP "+jarrayGroups.toString()+" taille :"+jarrayGroups.length());
                                for (int i = 0; i < jarrayGroups.length(); i++) {
                                    JSONObject json = jarrayGroups.getJSONObject(i);
//                                    Log.i("TEST_GROUP","json "+i+" "+json.toString());
                                    // Storing each json item in variable
                                    Group group = gson.fromJson(json.toString(), Group.class);
                                    Log.i("TEST_GROUP","get "+group.toString());
                                    // adding each child node to HashMap key => value

                                    // adding HashList to ArrayList
                                    groupList.add(group);
                                }
                                // closing this screen
                            } else {
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
        requestGroup.setTag(this);

        // On ajoute la Request au RequestQueue pour la lancer
        DBConnection.getInstance(this).getVolleyRequestQueue().add(requestGroup);

    }

    public void updateTeams(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("pid",String.valueOf(id));

        CustomRequest requestTeams = new CustomRequest(Request.Method.GET, url_get_team, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Ce code est appelé quand la requête réussi. Étant ici dans le thread principal, on va pouvoir mettre à jour notre Adapter
//                        mAdapter.updateMembers(jsonArray);
                        try {
                            int success = response.getInt(TAG_SUCCESS);
                            if (success == 1) {
                                // successfully created product

                                JSONArray jarrayTeams = response.getJSONArray("teams");
                            } else {
                                // failed to create product
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
        requestTeams.setTag(this);

        // On ajoute la Request au RequestQueue pour la lancer
        DBConnection.getInstance(this).getVolleyRequestQueue().add(requestTeams);

    }

    public void updateFriendRequest(){
        friendRequestLit.clear();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pid",id);

        CustomRequest requestFriend = new CustomRequest(Request.Method.GET, url_get_friend_request+"?pid="+params.get("pid") , params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt(TAG_SUCCESS);
                            Log.i("TEST_FRIEND","get for "+id +" "+response.toString());
                            if (success == 1) {
                                // successfully created product

                                JSONArray jarrayFriends = response.getJSONArray("users");
                                Gson gson = new GsonBuilder().create();

                                for (int i = 0; i < jarrayFriends.length(); i++) {
                                    JSONObject json = jarrayFriends.getJSONObject(i);

                                    // Storing each json item in variable
                                    Friend friend = gson.fromJson(json.toString(), Friend.class);

                                    Log.i("TEST_FRIEND","get "+friend.toString());
                                    // adding each child node to HashMap key => value

                                    // adding HashList to ArrayList
                                    friendRequestLit.add(friend);
                                }
                                // closing this screen
                            } else {
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
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pid",id);
                return params;
            }
        };
        requestFriend.setTag(this);

        // On ajoute la Request au RequestQueue pour la lancer
        DBConnection.getInstance(this).getVolleyRequestQueue().add(requestFriend);

    }*/

    public void addNewsListener(NewsListener newsListener){
        newsListeners.add(newsListener);
    }

    public void addCommentsListener(CommentsListener commentsListener){commentsListeners.add(commentsListener);}

    public Post getPost(int id){
        return postHashMap.get(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String lastName) {
        this.last_name = lastName;
    }

    public String getUsername() { return username; }

    public void setUsername(String pseudo) { this.username = pseudo; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInstance(HomeActivity instance) { this.instance = instance; }

    public List<Friend> getFriendList() {
        return friendList;
    }

    public HashMap<Integer,Post> getPostList() {
        return postHashMap;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
    }

    public void setSignup_date(String dateCreation) {
        this.signup_date = dateCreation;
    }

    public String getSexe() { return sexe; }

    public void setSexe(String sexe) { this.sexe = sexe;}

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }

    public String getSignup_date() {
        return signup_date;
    }

    public static List<Friend> getFriendRequestLit() {
        return friendRequestLit;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void viewDisplayed(){
        viewOpened = true;
    }

    public void viewClosed(){
        viewOpened = false;
    }

    @Override
    public void onBackPressed() {
        if(viewOpened){
            super.onBackPressed();
            viewClosed();
        }else {
            View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .create();
            Button yes_btn = (Button) view.findViewById(R.id.yes_button);
            yes_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
            Button no_btn = (Button) view.findViewById(R.id.no_button);
            no_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}
