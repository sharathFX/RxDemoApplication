package infiniteloop.rx.demo.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infiniteloop.rx.demo.application.networking.APIService;
import infiniteloop.rx.demo.application.networking.GitHubUserObject;
import infiniteloop.rx.demo.application.networking.ApiResponseListenerContract;
import infiniteloop.rx.demo.application.networking.RepoObject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.id_username_input)
    EditText mUsernameEditText;

    @Bind(R.id.id_github_response_text)
    TextView mResponseTextView;

    @OnClick(R.id.id_fetch_button)
    void fetchUserDetails() {
        mResponseTextView.setText("Loading ....");
        //fetchUserDetailsThroughInterface(mUsernameEditText.getText().toString());
        //fetchUserDetailsThroughTx(mUsernameEditText.getText().toString());
        //fetchReposThroughInterface("https://api.github.com/users/infinitec123/repos");
        fetchReposThroughRx("https://api.github.com/users/infinitec123/repos");
    }

    //*********************************************************************
    // Life cycles
    //*********************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //*********************************************************************
    // Interface implementation
    //*********************************************************************


    //*********************************************************************
    // Utility methods
    //*********************************************************************

    private void fetchUserDetailsThroughInterface(String user) {
        APIService.getInstance().getGitHubUserResponseThroughInterface(user, new ApiResponseListenerContract<GitHubUserObject>() {
            @Override
            public void onResponse(GitHubUserObject response) {
                runOnUiThread(() -> mResponseTextView.setText(response.toString()));
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> mResponseTextView.setText(errorMessage));
            }
        });
    }


    private void fetchUserDetailsThroughTx(String user) {
        APIService.getInstance().getGitHubUserResponseThroughRx(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                        //.subscribe(object -> mResponseTextView.setText(object.toString()));
                        //.doOnNext(object -> Log.v(TAG, object.toString()))
                .subscribe(new Subscriber<GitHubUserObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mResponseTextView.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(GitHubUserObject gitHubUserObject) {
                        mResponseTextView.setText(gitHubUserObject.toString());
                    }
                });
    }


    private void fetchReposThroughInterface(String repoUrl) {
        APIService.getInstance().getRepoResponseThroughInterface(repoUrl, new ApiResponseListenerContract<List<RepoObject>>() {


            @Override
            public void onResponse(List<RepoObject> response) {
                runOnUiThread(() -> mResponseTextView.setText(response.toString()));
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> mResponseTextView.setText(errorMessage));
            }
        });
    }


    private void fetchReposThroughRx(String repoUrl) {
        APIService.getInstance().getRepoResponseThroughRx(repoUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Subscriber<List<RepoObject>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mResponseTextView.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(List<RepoObject> repoObjects) {
                        mResponseTextView.setText(repoObjects.toString());
                    }
                });
    }

    //*********************************************************************
    // End of class
    //*********************************************************************
}
