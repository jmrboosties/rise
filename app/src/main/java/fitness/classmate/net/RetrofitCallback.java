package fitness.classmate.net;

import fitness.classmate.util.Print;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RetrofitCallback<T> implements Callback<T> {

	private UiCallback<T> mCallback;

	public RetrofitCallback(UiCallback<T> callback) {
		mCallback = callback;
	}

	@Override
	public void onResponse(Call<T> call, Response<T> response) {
		if(response.isSuccessful()) {
			if(mCallback != null)
				mCallback.onResponse(response.body());
		}
		else {
			Print.exception(new Exception("url: " + call.request().url().toString() + ";\n"
					+ "status code: " + response.code() + ";\n"
					+ "message: " + response.message()));

			if(mCallback != null)
				mCallback.onErrorResponse(call, response);
		}
	}

	@Override
	public void onFailure(Call<T> call, Throwable t) {
		//TODO something
//		if(mCallback != null)
//			mCallback.onFailure(call, t);
	}

	public interface UiCallback<T> {

		void onResponse(T response);

		void onErrorResponse(Call<T> call, Response<T> response);

//		void onFailure(Call<T> call, Throwable throwable);

	}

}
