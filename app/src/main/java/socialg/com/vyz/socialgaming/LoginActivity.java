package socialg.com.vyz.socialgaming;

import android.animation.IntArrayEvaluator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.HashMap;
import java.util.Map;

import socialg.com.vyz.socialgaming.connection.CustomRequest;
import socialg.com.vyz.socialgaming.connection.DBConnection;
import socialg.com.vyz.socialgaming.connection.UserInfo;

public class LoginActivity extends AppCompatActivity {
    /**
     * Login textView value
     */
    private EditText loginText;
    /**
     * Password textView value
     */
    private EditText passwordText;
    /**
     * Login button:::::
     */
    private Button buttonLogin;
    /**
     * Account creation button
     */
    private Button buttonCreateAccount;
    private CheckBox rememberMe;

    public static String currentIp = "10.33.1.221";
    private static String url_create_product = "http://"+currentIp+"/socialgaming/login.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Getting layout views
        loginText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.login_button);
        buttonCreateAccount = findViewById(R.id.createAccount);
        rememberMe = findViewById(R.id.rememberMe);

        SharedPreferences prefs = getSharedPreferences("user_details", MODE_PRIVATE);
        loginText.setText(prefs.getString("login", ""));
        rememberMe.setChecked(prefs.getBoolean("isChecked",false));

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonOnClick(v);
            }
        });
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateNewUSer.class);
                startActivity(intent);
            }
        });
    }


    /**
     * On loginButton clicked function, tests if informations are corrects
     * @param view
     */
    void loginButtonOnClick(View view){
        final String login,password;
        login = loginText.getText().toString();
        password = passwordText.getText().toString();

        Map<String, String> params = new HashMap<String, String>();
        params.put("login", login);
        params.put("pwd", password);

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
                                Toast.makeText(LoginActivity.this,"Successfully logged in",Toast.LENGTH_SHORT).show();
                                JSONArray jsonUser = response.getJSONArray("user");
                                JSONObject jsonObject = jsonUser.getJSONObject(0);
                                UserInfo user = UserInfo.getInstance();
                                user.setId(jsonObject.getString("id_user"));
                                user.setFirstName(jsonObject.getString("first_name"));
                                user.setLastName(jsonObject.getString("last_name"));
                                user.setPseudo(jsonObject.getString("pseudo"));
                                user.setAge(jsonObject.getString("age"));
                                user.setEmail(jsonObject.getString("email"));
                                user.setNation(jsonObject.getString("natio"));
                                user.setSexe(jsonObject.getString("sexe"));
                                user.setDateCreation(jsonObject.getString("date_inscription"));
                                UserInfo.getInstance().getInfo(LoginActivity.this);
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);

                                //save connections
                                if (rememberMe.isChecked()) {
                                    final SharedPreferences prefs = getSharedPreferences("user_details", Context.MODE_PRIVATE);
                                    prefs.edit().putString("login", login).apply();
                                    prefs.edit().putString("password", password).apply();//TODO crypter
                                    prefs.edit().putBoolean("isChecked", rememberMe.isChecked()).apply();
                                }
                            } else {
                                // failed to create product
                                Toast.makeText(LoginActivity.this, "Login or password not correct", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginActivity.this, "Error while getting JSON: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(this);


        DBConnection.getInstance(LoginActivity.this).getVolleyRequestQueue().add(request);

    }

}
