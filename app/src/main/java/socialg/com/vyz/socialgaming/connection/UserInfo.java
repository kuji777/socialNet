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
