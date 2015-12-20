package infiniteloop.rx.demo.application.networking;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class APIService {

    private static APIService sAPIService = new APIService();
    private OkHttpClient mOkHttpClient;
    private static final String GITHUB_USER_RESPONSE_ENDPOINT = "https://api.github.com/users/";

    private APIService() {
        mOkHttpClient = new OkHttpClient();
    }

    public static APIService getInstance() {
        return sAPIService;
    }


    //*********************************************************************
    // Interface ways
    //*********************************************************************

    public void getGitHubUserResponseThroughInterface(String userName, final ApiResponseListenerContract listener) {
        if (userName == null || TextUtils.isEmpty(userName)) {
            listener.onError("Invalid username input");
            return;
        }

        String endpoint = GITHUB_USER_RESPONSE_ENDPOINT + userName;
        Request request = new Request.Builder().url(endpoint).build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException throwable) {
                throwable.printStackTrace();
                listener.onError(throwable.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    listener.onError("Unexpected code " + response);
                }

                Gson gson = new Gson();
                GitHubUserObject object = gson.fromJson(response.body().string(), GitHubUserObject.class);
                listener.onResponse(object);
            }
        });
    }


    public void getRepoResponseThroughInterface(String repoUrl, final ApiResponseListenerContract listener) {
        if (repoUrl == null || TextUtils.isEmpty(repoUrl)) {
            listener.onError("Invalid repo url");
            return;
        }

        Request request = new Request.Builder().url(repoUrl).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException throwable) {
                throwable.printStackTrace();
                listener.onError(throwable.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    listener.onError("Unexpected code " + response);
                }

                Gson gson = new Gson();
                Type listType = new TypeToken<List<RepoObject>>() {
                }.getType();
                List<RepoObject> repos = gson.fromJson(response.body().string(), listType);
                listener.onResponse(repos);
            }
        });
    }


    //*********************************************************************
    // Rx ways
    //*********************************************************************


    public Observable<GitHubUserObject> getGitHubUserResponseThroughRx(final String userName) {
        return Observable.create(new Observable.OnSubscribe<GitHubUserObject>() {

            @Override
            public void call(Subscriber<? super GitHubUserObject> subscriber) {
                if (userName == null || TextUtils.isEmpty(userName)) {
                    subscriber.onError(new Throwable("Invalid username input"));
                }

                String endpoint = GITHUB_USER_RESPONSE_ENDPOINT + userName;
                Request request = new Request.Builder().url(endpoint).build();
                Response response = null;
                try {
                    response = mOkHttpClient.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        subscriber.onError(new IOException("Unexpected code " + response));
                    } else {
                        Gson gson = new Gson();
                        GitHubUserObject object = gson.fromJson(response.body().string(), GitHubUserObject.class);
                        subscriber.onNext(object);
                        subscriber.onCompleted();
                    }
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    public Observable<List<RepoObject>> getRepoResponseThroughRx(final String repoUrl) {
        return Observable.create(new Observable.OnSubscribe<List<RepoObject>>() {

            @Override
            public void call(Subscriber<? super List<RepoObject>> subscriber) {
                if (repoUrl == null || TextUtils.isEmpty(repoUrl)) {
                    subscriber.onError(new Throwable("Invalid repo url"));
                }

                Request request = new Request.Builder().url(repoUrl).build();
                Response response = null;
                try {
                    response = mOkHttpClient.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        subscriber.onError(new IOException("Unexpected code " + response));
                    } else {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<RepoObject>>() {
                        }.getType();
                        List<RepoObject> repos = gson.fromJson(response.body().string(), listType);
                        subscriber.onNext(repos);
                        subscriber.onCompleted();
                    }
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    //*********************************************************************
    // End of class
    //*********************************************************************

}
