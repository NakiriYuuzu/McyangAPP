package tw.edu.mcyangstudentapp.ApiModel;

import android.app.Activity;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class VolleyApi {
    private final Activity activity;

    public VolleyApi(Activity activity) {
        this.activity = activity;
    }

    public void getApi(String url, VolleyGet get) {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        //function response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                get::onSuccess, get::onFailed);

        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
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
