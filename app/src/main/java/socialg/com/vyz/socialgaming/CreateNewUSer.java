package socialg.com.vyz.socialgaming;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import socialg.com.vyz.socialgaming.connection.CustomRequest;
import socialg.com.vyz.socialgaming.connection.DBConnection;
import socialg.com.vyz.socialgaming.connection.JSONParser;

public class CreateNewUSer extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    // url to create new product
    private static String url_create_product = "http://"+LoginActivity.currentIp+"/socialgaming/add_user.php";
    private RequestQueue mRequestQueue;
//    private GithubListAdapter mAdapter;

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputPseudo;
    private EditText inputAge;
    private EditText inputPassword;
    private EditText inputEmail;
    private EditText inputNation;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);

        inputFirstName = findViewById(R.id.eddittext_first_name);
        inputLastName = findViewById(R.id.eddittext_last_name);
        inputPseudo = findViewById(R.id.eddittext_pseudo);
        inputAge = findViewById(R.id.eddittext_age);
        inputPassword = findViewById(R.id.eddittext_password);
        inputEmail = findViewById(R.id.eddittext_email);
        inputNation = findViewById(R.id.eddittext_nation);
        buttonSubmit = findViewById(R.id.button_create_user);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonSubmit(v);
                //new CreateNewUser().execute();
            }
        });

    }

    public void onButtonSubmit(View v) {
        final String firstName,lastName,pseudo,age,password,email,nation,buttonSumbmit;
        firstName = inputFirstName.getText().toString();
        lastName = inputLastName.getText().toString();
        pseudo = inputPseudo.getText().toString();
        age = inputAge.getText().toString();
        password = inputPassword.getText().toString();
        email = inputEmail.getText().toString();
        nation = inputNation.getText().toString();
        buttonSumbmit = buttonSubmit.getText().toString();

        Map<String, String> params = new HashMap<String, String>();
        params.put("first_name", firstName);
        params.put("last_name", lastName);
        params.put("pseudo",pseudo);
        params.put("password",password);

        CustomRequest request = new CustomRequest(Request.Method.POST, url_create_product, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Ce code est appelé quand la requête réussi. Étant ici dans le thread principal, on va pouvoir mettre à jour notre Adapter
//                        mAdapter.updateMembers(jsonArray);
                        try {
                            int success = response.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                // successfully created product
                                Toast.makeText(CreateNewUSer.this, "User Created !!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                                // closing this screen
                                finish();
                            } else {
                                // failed to create product
                                Toast.makeText(CreateNewUSer.this, "Not Created !!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CreateNewUSer.this, "Error while getting JSON: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(this);

        // On ajoute la Request au RequestQueue pour la lancer
        DBConnection.getInstance(CreateNewUSer.this).getVolleyRequestQueue().add(request);

    }

    @Override
    protected void onStop() {
//        mRequestQueue.cancelAll(this);
        super.onStop();
    }

//    /**
//     * Background Async Task to Create new product
//     * */
//    class CreateNewUser extends AsyncTask<String, String, String> {
//
//        /**
//         * Before starting background thread Show Progress Dialog
//         * */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(CreateNewUSer.this);
//            pDialog.setMessage("Creating Product..");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
//        }
//
//        /**
//         * Creating product
//         * */
//        protected String doInBackground(String... args) {
//            String firstName = inputFirstName.getText().toString();
//            String lastName = inputLastName.getText().toString();
//            String pseudo = inputPseudo.getText().toString();
//            String age = inputAge.getText().toString();
//            String password = inputPassword.getText().toString();
//            String email = inputEmail.getText().toString();
//            String nation = inputNation.getText().toString();
//            String buttonSumbmit = buttonSubmit.getText().toString();
//
//            // Building Parameters
//            List<NameValuePair> params = new ArrayList<NameValuePair>();
//            params.add(new BasicNameValuePair("first_name", firstName));
//            params.add(new BasicNameValuePair("last_name", lastName));
//            params.add(new BasicNameValuePair("pseudo", pseudo));
//            params.add(new BasicNameValuePair("password", password));
//
//            // getting JSON Object
//            // Note that create product url accepts POST method
//            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
//                    "POST", params);
//
//            // check log cat fro response
//            Log.d("Create Response", json.toString());
//
//            // check for success tag
//            try {
//                int success = json.getInt(TAG_SUCCESS);
//
//                if (success == 1) {
//                    // successfully created product
//                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//                    startActivity(i);
//
//                    // closing this screen
//                    finish();
//                } else {
//                    // failed to create product
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        /**
//         * After completing background task Dismiss the progress dialog
//         * **/
//        protected void onPostExecute(String file_url) {
//            // dismiss the dialog once done
//            pDialog.dismiss();
//        }
//
//    }
}
