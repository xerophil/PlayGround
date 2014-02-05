package controllers;

import controllers.securtity.UserSecured;
import models.user.Session;
import models.user.Token;
import models.user.User;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.mainHome;
import views.html.userLogin;

import java.util.Date;


public class Application extends Controller {

    public static class LoginData {
        public static Form<LoginData> loginForm = new Form<>(LoginData.class);

        @Constraints.Required
        public String email;
        @Constraints.Required
        public String password;
        public Boolean remember;
        public String redirect;
    }

    public static Result index() {
        return ok(mainHome.render());
    }

    public static Result showLogin(String redirect) {

        LoginData loginData = new LoginData();
        if (redirect != null && redirect.startsWith("/")) {
            loginData.redirect = redirect;
        }

        return ok(userLogin.render(LoginData.loginForm.fill(loginData)));
    }

    public static Result checkLogin() {

        Form<LoginData> boundForm = LoginData.loginForm.bindFromRequest();

        if (boundForm.hasErrors()) {
            flash("error", "Bitte Login Formular vollständig ausfüllen");
            return badRequest(userLogin.render(boundForm));
        }
        User user = User.find.where().like("mail", boundForm.get().email).findUnique();

        if (user == null) {
            flash("error", "User nicht vorhanden");
            return notFound(userLogin.render(boundForm));
        }

        if (user.isLocked()) {
            flash("error", "User gesperrt");
            return forbidden(userLogin.render(boundForm));
        }
        if (user.checkPassword(boundForm.get().password)) {

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
            return forbidden(userLogin.render(boundForm));

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
