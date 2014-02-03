package controllers;

import controllers.securtity.UserSecured;
import models.user.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.mainHome;


public class Application extends Controller {


    public static Result index() {
        return ok(mainHome.render());
    }


    public static Result showLogin() {
        return ok(views.html.user.userLogin.render());
    }

    public static Result checkLogin() {
        return play.mvc.Results.TODO;
    }

    public static Result logout() {
        User user = User.currentUser(ctx());
        user.session.delete();
        session().remove(UserSecured.SESSION_NAME);
        flash("success", "Du wurdest erfolgreich ausgecheckt.");

        return redirect(routes.Application.index());
    }
}
