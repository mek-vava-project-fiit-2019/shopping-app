package com.example.shoppingapp.model.data;

import com.example.shoppingapp.controller.AccountManager;
import com.example.shoppingapp.model.entity.User;
import com.example.shoppingapp.model.service.HTTPGetRequestTask;
import com.example.shoppingapp.model.service.HTTPResult;

import java.io.IOException;

import static com.example.shoppingapp.controller.Config.CONFIG_AUTH_API_PATH;
import static com.example.shoppingapp.controller.Config.CONFIG_SERVER_PATH;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {



    public Result<User> login(String email, String password) {

        try {
            String apiPath = CONFIG_AUTH_API_PATH.replace("{email}", email);
            apiPath = apiPath.replace("{password}", password);

            HTTPGetRequestTask<User> authenticateTask = new HTTPGetRequestTask(CONFIG_SERVER_PATH  + "/" + apiPath , User.class,null);
            System.out.println("Executing: ");
            System.out.println(CONFIG_SERVER_PATH  + "/" + apiPath);
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
