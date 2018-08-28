package socialg.com.vyz.socialgaming;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import socialg.com.vyz.socialgaming.bean.Friend;
import socialg.com.vyz.socialgaming.connection.CustomRequest;
import socialg.com.vyz.socialgaming.connection.DBConnection;
import socialg.com.vyz.socialgaming.connection.JSONParser;
import socialg.com.vyz.socialgaming.connection.UserInfo;

public class FriendActivity extends AppCompatActivity {


    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

//    ArrayList<HashMap<String, String>> productsList;
    private Friend friend;

    // url to get info
    private static String url_get_friend_info = "http://"+LoginActivity.currentIp+"/socialgaming/get_user_details.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "users";
    private static final String TAG_PID = "id_user";
    private static final String TAG_NAME = "pseudo";

    // products JSONArray
    JSONArray products = null;

    private LinearLayout fragmentFrame;
    private Button switcher;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        title = findViewById(R.id.friend_title);
        switcher = findViewById(R.id.button_switcher);
        fragmentFrame = findViewById(R.id.friend_frame_fragment);

        String pid = getIntent().getStringExtra("id");

        CustomRequest requestFriend = new CustomRequest(Request.Method.GET, url_get_friend_info+"?pid="+pid, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jarrayFriends = response.getJSONArray("user");
                            JSONObject json = jarrayFriends.getJSONObject(0);

                            Gson gson = new GsonBuilder().create();
                            // Storing each json item in variable
                            friend = gson.fromJson(json.toString(), Friend.class);

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
        DBConnection.getInstance(FriendActivity.this).getVolleyRequestQueue().add(requestFriend);

//        productsList = new ArrayList<HashMap<String, String>>();
//        new LoadAllProducts().execute();

        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO switch fragment chat / wall
            }
        });

    }

    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    /*class LoadAllProducts extends AsyncTask<String, String, String> {

        *//**
         * Before starting background thread Show Progress Dialog
         * *//*
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FriendActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        *//**
         * getting All products from url
         * *//*
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String pseudo = c.getString(TAG_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, pseudo);

                        // adding HashList to ArrayList
                        productsList.add(map);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FriendActivity.this,"Product found",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
//                    Intent i = new Intent(getApplicationContext(),
//                            NewProductActivity.class);
//                    // Closing all previous activities
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FriendActivity.this,"No product found",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        *//**
         * After completing background task Dismiss the progress dialog
         * **//*
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    *//**
                     * Updating parsed JSON data into ListView
                     * *//*
//                    ListAdapter adapter = new SimpleAdapter(
//                            FriendActivity.this, productsList,
//                            R.layout.single_profile, new String[] {
//                            TAG_NAME},
//                            new int[] { R.id.single_view_title });
//                    // updating listview
//                    setListAdapter(adapter);
                    for (HashMap<String, String> hash : productsList) {


                        LayoutInflater layoutInflater2 = (LayoutInflater) getApplicationContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        final LinearLayout childView = (LinearLayout) layoutInflater2.inflate(R.layout.single_profile, null);
                        ((TextView) childView.findViewById(R.id.single_view_title)).setText(hash.get(TAG_NAME));
                        fragmentFrame.addView(childView);

                    }
                }

            });


        }

    }*/
}
