package tw.edu.mcyangstudentapp.ApiModel;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class VolleyApi {

    private final String TAG = "Volley Debug: ";
    private final Activity activity;

    public VolleyApi(Activity activity) {
        this.activity = activity;
    }

    public void getApi(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    //function response
                    Log.e(TAG, "Success!");
                }, error -> {
                    Log.e(TAG, "Failed!");
            //function error
        });

        requestQueue.add(stringRequest);
    }

    public void getApi(String url, VolleyGet get) {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        //function response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                get::onSuccess, get::onFailed);

        requestQueue.add(stringRequest);
    }

    public void postApi(String url, VolleyGet get) {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.e(TAG, response);
                    get.onSuccess(response);

                }, error -> {
                    Log.e(TAG, error.toString());
                    get.onFailed(error);
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void postApi(String url, String id, String pass, VolleyGet get) {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.e(TAG, response);
                    get.onSuccess(response);

                }, error -> {
                    Log.e(TAG, error.toString());
                    get.onFailed(error);
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sidimei", id);
                params.put("password", pass);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void postApi(String url, VolleyGet get, VolleyPost post) {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                get::onSuccess, get::onFailed) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                return post.getParams();
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                // headers.put("Authorization", "Bearer" + " " + getToken());
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    public interface VolleyGet {
        void onSuccess(String result);
        void onFailed(VolleyError error);
    }

    public interface VolleyPost {
        Map<String, String> getParams();
    }
}