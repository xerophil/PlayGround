package controllers;

import controllers.securtity.UserSecured;
import models.user.Session;
import models.user.Token;
import models.user.User;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.mainHome;
import views.html.userLogin;

import java.util.Date;
import java.util.Map;


public class Application extends Controller {

    public Form<LoginData> loginForm = new Form<>(LoginData.class);
    class LoginData {
        public String email;
        public String password;
        public Boolean remember;
        public String redirect;
    }

    public static Result index() {
        return ok(mainHome.render());
    }

    public static Result showLogin(String redirect) {
        if (redirect != null && !redirect.startsWith("/")) {
            redirect = "/";
        }

        return ok(userLogin.render(redirect));
    }

    public static Result checkLogin() {
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        String email = formData.get("email")[0];
        String password = formData.get("password")[0];
        if (email == null || password == null) {
            flash("error", "Bitte Loginformular ausfüllen");
            return redirect(routes.Application.showLogin(null));
        }
        User user = User.find.where().like("mail", email).findUnique();

        if (user == null) {
            flash("error", "User nicht vorhanden");
            return redirect(routes.Application.showLogin(null));
        }

        if (user.isLocked()) {
            flash("error", "User gesperrt");
            return redirect(routes.Application.showLogin(null));
        }
        if (user.checkPassword(password)) {

            if (user.token != null)
                user.token.delete();
            if (user.session != null)
                user.session.delete();


            user.loginAttempts = 0;
            user.lastLogin = new Date();
            user.session = new Session();


            user.update();
            session().put(UserSecured.SESSION_NAME, user.session.id.toString());
            Logger.info("Benuter {} hat sich erfolgreich angemeldet.", user.name);
            flash("success", "Willkommen, " + user.name);
            return redirect(routes.Application.index());
        } else {
            Logger.info("User {} hat sich {}-mal falsch eingeloggt.", user.name, user.loginAttempts + 1);
            if (user.loginAttempts++ >= 5) {
                user.locked = true;
                user.lockTime = new Date();
                user.token = new Token();
                user.update();
                Logger.info("Benutzer {} wurde aufgrund zu vieler Fehlerhafter Logins gesperrt", user.name);
                flash("error", "Dein Account wurde aufgrund zu vieler fehlerhafter Login-Versuche gesperrt.");
                return redirect(routes.Application.showLogin(null));
            }
            user.update();
            flash("warn", "Fehlerhaftes Passwort. Dies ist Versuch " + user.loginAttempts);
            return redirect(routes.Application.showLogin(null));

        }
    }

    public static Result logout() {
        User user = User.currentUser(ctx());
        user.session.delete();
        session().remove(UserSecured.SESSION_NAME);
        flash("success", "Du wurdest erfolgreich ausgecheckt.");

        return redirect(routes.Application.index());
    }
}
