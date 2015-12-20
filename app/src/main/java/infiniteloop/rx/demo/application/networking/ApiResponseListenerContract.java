package infiniteloop.rx.demo.application.networking;


public interface ApiResponseListenerContract<T> {
    void onResponse(T response);
    void onError(String errorMessage);
}
