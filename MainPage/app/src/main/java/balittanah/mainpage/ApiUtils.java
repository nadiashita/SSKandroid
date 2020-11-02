package balittanah.mainpage;
public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "https://sskapi.azurewebsites.net/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}