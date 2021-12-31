package tw.edu.pu.ApiModel;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public class VolleyApi {

    private final String TAG = "Volley Debug: ";
    private Activity activity;
    private String url;

    public VolleyApi() {

    }

    public VolleyApi(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    public void getApi() {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
        response -> {
            //function response
            try {

            }catch (Exception e) {

            }
        }, error -> {
            //function error
        });

        requestQueue.add(stringRequest);
    }

    public void postApi() {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
        response -> {

        }, error -> {

        });
    }

    public interface VolleyGetBack {
        void onSuccess(String result);
        void onError(VolleyError error);
    }

    public interface VolleyPostBack {
        Map<String, String> getParams();
    }
}
