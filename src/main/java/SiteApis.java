/**
 * Created by Microsoft on 07/07/2017.
 */
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface SiteApis {

    @GET("api/advertisement/list")
    Call<List<Advertisement>> getAdsList(@Query("page") Integer page,@Query("size") Integer pageSize);

}
