package infiniteloop.rx.demo.application.networking;


import com.google.gson.annotations.SerializedName;

public class GitHubUserObject {


    @SerializedName("login")
    private String mLoginId;

    @SerializedName("avatar_url")
    private String mAvatarUrl;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("name")
    private String mName;

    @SerializedName("repos_url")
    private String mRepoUrl;


    public String getLoginId() {
        return mLoginId;
    }

    public void setLoginId(String mLoginId) {
        this.mLoginId = mLoginId;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String mAvatarUrl) {
        this.mAvatarUrl = mAvatarUrl;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getRepoUrl() {
        return mRepoUrl;
    }

    @Override
    public String toString() {
        return "Name:" + mName +
                "\n LoginId:" + mLoginId +
                "\n AvatarUrl:'" + mAvatarUrl +
                "\n ReposUrl:'" + mRepoUrl +
                "\n Email:" + mEmail;
    }
}
