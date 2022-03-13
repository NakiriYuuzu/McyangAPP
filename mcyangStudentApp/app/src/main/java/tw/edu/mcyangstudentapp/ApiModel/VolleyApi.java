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
    RequestQueue requestQueue;

    public VolleyApi(Activity activity) {
        requestQueue = Volley.newRequestQueue(activity);
    }

    public void getApi(String url, VolleyGet get) {
        //function response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                get::onSuccess, get::onFailed);

        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    public void getDomJudgeAPI(String url, VolleyGet get) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, get::onSuccess, get::onFailed) {
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Basic czEwODI5Nzg6czEwODI5Nzg=");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    public void postApi(String url, VolleyGet get, VolleyPost post) {
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

    public void api(int request, String url, VolleyGet get) {
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(request, url,
                get::onSuccess, get::onFailed) {
        };

        requestQueue.add(stringRequest);
    }

    public void api(int request, String url, VolleyGet get, VolleyPost post) {
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(request, url,
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
