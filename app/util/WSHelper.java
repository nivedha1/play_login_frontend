package util;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;

import java.util.concurrent.CompletionStage;
public class WSHelper {


    public CompletionStage<WSResponse> checkAuthorized(String username,String password) {

        WSClient ws = play.test.WSTestClient.newClient(9011);
        //add username password
        WSRequest request = ws.url("http://localhost:9011/login");
        ObjectNode res = Json.newObject();
        res.put("username",username);
        res.put("password",password);
        return request.addHeader("Content-Type", "application/json")
                .post(res)
                .thenApply((WSResponse r) -> {
                    return r;
                });
    }



    public  CompletionStage<WSResponse> registerUser(String username,String password) {

        WSClient ws = play.test.WSTestClient.newClient(9011);
        // send this. user
        ObjectNode res = Json.newObject();
        res.put("username", username);
        res.put("password",password);

        System.out.println(username);
        System.out.println(password);

        WSRequest request = ws.url("http://localhost:9011/register");
        return request.addHeader("Content-Type", "application/json")
                .post(res)
                .thenApply((WSResponse r) -> {
                    return r;
                });
    }

}
