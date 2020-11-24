package controllers;

import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSResponse;
import views.html.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.concurrent.CompletionStage;

/**
 * Software Service Market Place
 */
public class HomeController extends Controller {

    @Inject
    HttpExecutionContext ec;

    private FormFactory formFactory;

    @Inject
    public HomeController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /**
     * Index page
     */
    public Result index() {
        return ok(views.html.login.render(""));
    }

    /**
     * Index page
     */
    public Result signup() {
        return ok(views.html.register.render(null));
    }

    public CompletionStage<Result> loginHandler(final Http.Request request) {

        Form<User> loginForm = formFactory.form(User.class).bindFromRequest(request);

        return loginForm.get().checkAuthorized()
                .thenApplyAsync((WSResponse r) -> {
                    if (r.getStatus() == 200 && r.asJson() != null && r.asJson().asBoolean()) {
                        System.out.println(r.asJson());

                        // redirect to index page, to display all categories
                        return ok(views.html.index.render("Welcome!!! " + loginForm.get().getUsername()));
                    } else {
                        System.out.println("response null");
                        String authorizeMessage = "Incorrect Username or Password ";
                        return badRequest(views.html.login.render(authorizeMessage));
                    }
                }, ec.current());
    }

    public CompletionStage<Result> signupHandler(final Http.Request request) {

        Form<User> registrationForm = formFactory.form(User.class).bindFromRequest(request);

        return registrationForm.get().registerUser()
                .thenApplyAsync((WSResponse r) -> {
                    if (r.getStatus() == 200 && r.asJson() != null) {
                        System.out.println("success");
                        System.out.println(r.asJson());
                        return ok(views.html.register.render("Successfuly signed up"));
                    } else {
                        System.out.println("response null");
                        return badRequest(views.html.register.render("Username already exists"));
                    }
                }, ec.current());

    }
    public CompletionStage<Result> addStudentHandler(final Http.Request request) {

        Form<Student> registrationForm = formFactory.form(Student.class).bindFromRequest(request);

        return registrationForm.get().addStudent()
                .thenApplyAsync((WSResponse r) -> {
                    if (r.getStatus() == 200 && r.asJson() != null ) {
                        System.out.println("success");
                        System.out.println(r.asJson());
                        return ok("Successfuly Added").as("text/html");

                    } else {
                        System.out.println("response null");
                        return badRequest("Studentname already exists").as("text/html");
                    }
                }, ec.current());

    }
    public CompletionStage<Result> searchStudent(final Http.Request request) {

        Form<Student> searchForm = formFactory.form(Student.class).bindFromRequest(request);

        return searchForm.get().searchStudent()
                .thenApplyAsync((WSResponse r) -> {
                    System.out.println(r.asJson().asBoolean()==true);
                    if (r.getStatus() == 200 && r.asJson().asBoolean()==true) {
                        System.out.println("success");
                        System.out.println(r.asJson());
                        return ok("Successfuly Found").as("text/html");
                    } else {
                        return badRequest("Student not found").as("text/html");
                    }
                }, ec.current());

    }

    /**
     * Add student page
     */
    public Result add() {
        return ok(views.html.addstudent.render(""));
    }

    /**
     * Search student page
     */
    public Result search() {
        return ok(views.html.search.render(null));
    }

}