package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;

import java.util.concurrent.CompletionStage;

public class Student {

    private String studentname;

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public CompletionStage<WSResponse> searchStudent() {

        WSClient ws = play.test.WSTestClient.newClient(9012);
        //add username password
        WSRequest request = ws.url("http://localhost:9012/search");
        ObjectNode res = Json.newObject();
        res.put("studentname", this.studentname);

        return request.addHeader("Content-Type", "application/json")
                .post(res)
                .thenApply((WSResponse r) -> {
                    return r;
                });
    }

    public CompletionStage<WSResponse> addStudent() {

        WSClient ws = play.test.WSTestClient.newClient(9012);
        // send this. user
        ObjectNode res = Json.newObject();
        res.put("studentname", this.studentname);

        WSRequest request = ws.url("http://localhost:9012/addStudent");
        return request.addHeader("Content-Type", "application/json")
                .post(res)
                .thenApply((WSResponse r) -> {
                    return r;
                });
    }
}
