package sk.mek.shoppingapp.model.data;

import com.example.shoppingapp.BuildConfig;

import java.io.IOException;

import sk.mek.shoppingapp.controller.AccountManager;
import sk.mek.shoppingapp.model.entity.User;
import sk.mek.shoppingapp.model.service.HTTPGetRequestTask;
import sk.mek.shoppingapp.model.service.HTTPResult;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {



    public Result<User> login(String email, String password) {

        try {
            String apiPath = BuildConfig.CONFIG_AUTH_API_PATH.replace("{email}", email);
            apiPath = apiPath.replace("{password}", password);

            HTTPGetRequestTask<User> authenticateTask = new HTTPGetRequestTask(BuildConfig.CONFIG_SERVER_PATH  + "/" + apiPath , User.class,null);
            System.out.println("Executing: ");
            System.out.println(BuildConfig.CONFIG_SERVER_PATH  + "/" + apiPath);
            HTTPResult<User> authResult = authenticateTask.execute().get();

            if(authResult.getResultCode() == HTTP_OK) {
                return new Result.Success<>(authResult.getResultObject());
            } else {
                return new Result.Error(new Exception("Wrong password or Email"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));
        }

    }

    public void logout() {
        AccountManager.loggedUser = null;
    }
}
