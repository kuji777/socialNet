package socialg.com.vyz.socialgaming.connection;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import socialg.com.vyz.socialgaming.CreateNewUSer;
import socialg.com.vyz.socialgaming.LoginActivity;

public class DBConnection{

    private RequestQueue requestQueue;

    private static DBConnection instance = null;
    private static Context currentContext;

    private DBConnection(Context context){
        currentContext = context;
        requestQueue = getVolleyRequestQueue();
    }

    public static synchronized DBConnection getInstance(Context activity){
        if(instance == null ){
            instance = new DBConnection(activity);
        }
        return instance;
    }

    public RequestQueue getVolleyRequestQueue() {
        if(requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(currentContext.getApplicationContext());
        }
        return requestQueue;
    }

    public <T>void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }

}
