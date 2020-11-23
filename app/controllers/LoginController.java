package controllers;

import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.libs.ws.WSResponse;


import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import util.WSHelper;
import models.Person;
public class LoginController extends Controller {

    private final FormFactory formFactory;
    private final HttpExecutionContext ec;
    private final WSHelper util=new WSHelper();
    @Inject
    public LoginController(FormFactory formFactory, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.ec = ec;
    }

    public Result index(final Http.Request request) {
        return ok(views.html.index.render(request));
    }
    public Result login(final Http.Request request) {
        return ok(views.html.login.render(request));
    }
    public CompletionStage<Result> registerUser(final Http.Request request) {
        Person person = formFactory.form(Person.class).bindFromRequest(request).get();

        return util.registerUser(person.getName(),person.getPassword())
                .thenApplyAsync((WSResponse r) -> {
                    if (r.getStatus() == 200 && r.asJson() != null) {
                        System.out.println("success");
                        System.out.println(r.asJson());
                        return ok("User registered" + person.getName()).as("text/html");
                    } else {
                        return badRequest("Unsuccessful register").as("text/html");
                    }
                }, ec.current());
    }

    public CompletionStage<Result> loginUser(final Http.Request request) {
        Person person = formFactory.form(Person.class).bindFromRequest(request).get();
        return util.checkAuthorized(person.getName(),person.getPassword())
                .thenApplyAsync((WSResponse r) -> {
                    if (r.getStatus() == 200 && r.asJson() != null && r.asJson().asBoolean()) {
                        System.out.println(r.asJson());
                       return ok("Welcome!!! " + person.getName()).as("text/html");
                    } else {
                        System.out.println("response null");
                        return badRequest("credentials invalid").as("text/html");
                    }
                }, ec.current());
    }

}





