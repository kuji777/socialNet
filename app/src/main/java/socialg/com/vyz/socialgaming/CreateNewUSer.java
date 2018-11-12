package socialg.com.vyz.socialgaming;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import socialg.com.vyz.socialgaming.connection.CustomRequest;
import socialg.com.vyz.socialgaming.connection.DBConnection;
import socialg.com.vyz.socialgaming.connection.JSONParser;

public class CreateNewUSer extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    // url to create new product
    private static String url_create_product = "http://"+LoginActivity.currentIp+"/api-sg/v1/users";
    private RequestQueue mRequestQueue;
//    private GithubListAdapter mAdapter;

    // JSON Node names
    private static final String TAG_SUCCESS = "status";

    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputPseudo;
    private EditText inputAge;
    private EditText inputPassword;
    private EditText inputPasswordCheck;
    private EditText inputEmail;
    private EditText inputEmailCheck;
    private EditText inputNation;
    private Button buttonSubmit;

    private TextView fname_warning;
    private TextView lname_warning;
    private TextView email_warning;
    private TextView email_check_warning;
    private TextView pwd_warning;
    private TextView pwd_check_warning;
    private TextView email_exist_warning;

    private Animation shake;

    private static boolean emailResponse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);

        inputFirstName = findViewById(R.id.eddittext_first_name);
        inputLastName = findViewById(R.id.eddittext_last_name);
        inputPseudo = findViewById(R.id.eddittext_pseudo);
        inputAge = findViewById(R.id.eddittext_age);
        inputPassword = findViewById(R.id.eddittext_password);
        inputPasswordCheck = findViewById(R.id.eddittext_password_check);
        inputEmail = findViewById(R.id.eddittext_email);
        inputEmailCheck = findViewById(R.id.eddittext_email_check);
        inputNation = findViewById(R.id.eddittext_nation);
        buttonSubmit = findViewById(R.id.button_create_user);

        //Warnings
        fname_warning = findViewById(R.id.fname_warning);
        lname_warning = findViewById(R.id.lname_warning);
        email_warning = findViewById(R.id.email_warning);
        email_check_warning = findViewById(R.id.email_check_warning);
        pwd_warning = findViewById(R.id.pwd_warning);
        pwd_check_warning = findViewById(R.id.pwd_check_warning);
        email_exist_warning = findViewById(R.id.email_exist_warning);

        shake = AnimationUtils.loadAnimation(this,R.anim.shake);

        inputFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    fnameTest();
                }
            }
        });
        inputLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    lnameTest();
                }
            }
        });
        inputEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    emailTest();
                }
            }
        });
        inputEmailCheck.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    emailCheckTest();
                }
            }
        });
        inputPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    passwordTest();
                }
            }
        });
        inputPasswordCheck.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    passwordTestCheck();
                }
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonSubmit(v);
            }
        });

    }

    private boolean passwordTestCheck() {
        String pwdCheckText = inputPasswordCheck.getText().toString();
        String pwdTxt = inputPassword.getText().toString();
        if(!pwdCheckText.equals(pwdTxt)){
            pwd_check_warning.setVisibility(View.VISIBLE);
            inputPasswordCheck.startAnimation(shake);
            return false;
        }else{
            pwd_check_warning.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean passwordTest() {
        String pwdText = inputPassword.getText().toString();
        if(pwdText.length() < 5 || !(pwdText.matches("[A-Za-z0-9]+")) ){
            pwd_warning.setVisibility(View.VISIBLE);
            inputPassword.startAnimation(shake);
            return false;
        }else{
            pwd_warning.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean emailCheckTest() {
        String emailCheckText = inputEmailCheck.getText().toString();
        String emailTxt = inputEmail.getText().toString();
        if(!emailCheckText.equals(emailTxt)){
            email_check_warning.setVisibility(View.VISIBLE);
            inputEmailCheck.startAnimation(shake);
            return false;
        }else{
            email_check_warning.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean emailTest() { //TODO Controler le retour de test email
        String emailText = inputEmail.getText().toString();
        Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(emailText);
        if(!matcher.find()){
            email_warning.setVisibility(View.VISIBLE);
            inputEmail.startAnimation(shake);
            return false;
        }else{

            CustomRequest request = new CustomRequest(Request.Method.GET, url_create_product+"/"+emailText, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Ce code est appelé quand la requête réussi. Étant ici dans le thread principal, on va pouvoir mettre à jour notre Adapter
//                        mAdapter.updateMembers(jsonArray);
                            try {
                                int success = response.getInt(TAG_SUCCESS);

                                if (success == 1) {
                                    emailResponse = true;
                                    email_exist_warning.setVisibility(View.GONE);
                                } else {
                                    emailResponse = false;
                                    email_exist_warning.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    // Le code suivant est appelé lorsque Volley n'a pas réussi à récupérer le résultat de la requête
                    Toast.makeText(CreateNewUSer.this, "Error while getting JSON: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            request.setTag(this);

            // On ajoute la Request au RequestQueue pour la lancer
            DBConnection.getInstance(CreateNewUSer.this).getVolleyRequestQueue().add(request);
            return true;

        }
    }

    private boolean lnameTest() {
        String text = inputLastName.getText().toString();
        if(text.length() < 2 || text.length() > 26){
            lname_warning.setVisibility(View.VISIBLE);
            inputLastName.startAnimation(shake);
            return false;
        }else{
            lname_warning.setVisibility(View.GONE);
            pwd_check_warning.startAnimation(shake);
            return true;
        }
    }

    private boolean fnameTest() {
        String text = inputFirstName.getText().toString();
        if(text.length() < 2 || text.length() > 26){
            fname_warning.setVisibility(View.VISIBLE);
            inputFirstName.startAnimation(shake);
            return false;
        }else{
            fname_warning.setVisibility(View.GONE);
            return true;
        }
    }

    public void onButtonSubmit(View v) {
        final String firstName,lastName,pseudo,age,password,email,nation,buttonSumbmit;

        if(fnameTest() && lnameTest() && passwordTest() && passwordTestCheck() && emailTest() && emailCheckTest()) {

            firstName = inputFirstName.getText().toString();
            lastName = inputLastName.getText().toString();
            pseudo = inputPseudo.getText().toString();
            age = inputAge.getText().toString();
            password = inputPassword.getText().toString();
            email = inputEmail.getText().toString();
            nation = inputNation.getText().toString();
            buttonSumbmit = buttonSubmit.getText().toString();

            Map<String, String> params = new HashMap<String, String>();
            params.put("first_name", firstName.substring(0, 1).toUpperCase() + firstName.substring(1));
            params.put("last_name", lastName.substring(0, 1).toUpperCase() + lastName.substring(1));
            params.put("username", firstName.toLowerCase() + "_" + lastName.toLowerCase());
            params.put("password", password);
            params.put("email", email);
            params.put("profile_pic", "none for now");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String strDAte = dateFormat.format(date);
            params.put("signup_date", strDAte);

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
                                    // failed to
                                    // create product
                                    Toast.makeText(CreateNewUSer.this, "Not Created !!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
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
    }

    @Override
    protected void onStop() {
//        mRequestQueue.cancelAll(this);
        super.onStop();
    }


}
