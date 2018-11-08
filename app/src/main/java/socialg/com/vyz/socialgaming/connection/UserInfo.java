package socialg.com.vyz.socialgaming.connection;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import socialg.com.vyz.socialgaming.LoginActivity;
import socialg.com.vyz.socialgaming.bean.Friend;
import socialg.com.vyz.socialgaming.bean.Group;
import socialg.com.vyz.socialgaming.bean.Post;
import socialg.com.vyz.socialgaming.bean.Team;

/**
 * Created by Vincent on 14/07/2018.
 */

public class UserInfo {

    private String id;
    private String first_name;
    private String last_name;
    private String username;
    private String age;
    private String password;
    private String email;
    private String nation;
    private String sexe;
    private String signup_date;

    private static UserInfo instance = null;

    private List<Friend> friendList = new ArrayList<Friend>();
    private List<Group> groupList = new ArrayList<Group>();
    private List<Team> teamList = new ArrayList<Team>();
    private List<Friend> friendRequestLit = new ArrayList<Friend>();
    private List<Post> postList = new ArrayList<Post>();

    private static String currentIp = LoginActivity.currentIp;

    private static String url_get_friend = "http://"+currentIp+"/socialgaming/liste_amis.php";
    private static String url_get_group = "http://"+currentIp+"/socialgaming/liste_jeux_abo.php";
    private static String url_get_team = "http://"+currentIp+"/socialgaming/get_user_teams.php";
    private static String url_get_friend_request = "http://"+currentIp+"/socialgaming/liste_demande_ami.php";
    private static String url_get_posts = "http://"+currentIp+"/api-sg/v1/posts";

    private static final String TAG_SUCCESS = "status";

    Context currentContext;

    private UserInfo(){

    }

    public static UserInfo getInstance(){
        if(instance == null){
           instance = new UserInfo();
        }
        return instance;
    }

    public void getInfo(Context context) {
        currentContext = context;
        updatePosts();
//        updateFriends();
//        updateGroup();
//        updateFriendRequest();
    }



    public void updateFriends(){
        friendList.clear();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pid",id);

        Log.i("TEST_FRIEND","get friends of "+id +" "+params.toString());

       /* CustomRequest requestFriend = new CustomRequest( url_get_friend, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt(TAG_SUCCESS);
                            Log.i("TEST_FRIEND","get for "+id +" "+response.toString());
                            if (success == 1) {
                                // successfully created product

                                JSONArray jarrayTeams = response.getJSONArray("teams");
                                Gson gson = new GsonBuilder().create();

                                for (int i = 0; i < jarrayTeams.length(); i++) {
                                    JSONObject json = jarrayTeams.getJSONObject(i);

                                    // Storing each json item in variable
                                    Friend friend = gson.fromJson(json.getJSONObject("json").toString(), Friend.class);
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
        });
        requestFriend.setTag(this);*/

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
        DBConnection.getInstance(currentContext).getVolleyRequestQueue().add(requestFriend);

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
                            Log.i("TEST_POSTS","get for "+id +" "+response.toString());
                            if (success == 1) {
                                // successfully created product

                                JSONArray jarrayGroups = response.getJSONArray("result");
                                Gson gson = new GsonBuilder().create();

//                                Log.i("TEST_GROUP","ARRAY GROUTP "+jarrayGroups.toString()+" taille :"+jarrayGroups.length());
                                for (int i = 0; i < jarrayGroups.length(); i++) {
                                    JSONObject json = jarrayGroups.getJSONObject(i);
//                                    Log.i("TEST_GROUP","json "+i+" "+json.toString());
                                    // Storing each json item in variable
                                    Post post = gson.fromJson(json.toString(), Post.class);
                                    Log.i("TEST_GROUP","get "+post.toString());
                                    // adding each child node to HashMap key => value

                                    // adding HashList to ArrayList
                                    postList.add(post);
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
        DBConnection.getInstance(currentContext).getVolleyRequestQueue().add(requestGroup);

    }

    public void updateGroup(){
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
        DBConnection.getInstance(currentContext).getVolleyRequestQueue().add(requestGroup);

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
        DBConnection.getInstance(currentContext).getVolleyRequestQueue().add(requestTeams);

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
        DBConnection.getInstance(currentContext).getVolleyRequestQueue().add(requestFriend);

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

    public String getPseudo() {
        return username;
    }

    public void setUsername(String pseudo) {
        this.username = pseudo;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

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

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public void setInstance(UserInfo instance) {
        this.instance = instance;
    }

    public List<Friend> getFriendList() {
        return friendList;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
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

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setSignup_date(String dateCreation) {
        this.signup_date = dateCreation;
    }

    public String getSexe() {
        return sexe;
    }

    public String getSignup_date() {
        return signup_date;
    }

    public List<Friend> getFriendRequestLit() {
        return friendRequestLit;
    }

}
