package balittanah.mainpage;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    @POST("/api/Inference/ProcessData")
    @FormUrlEncoded
    Call<Post> savePost(@Field("Reflectance") String title);
}