package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.securtity.UserSecured;
import models.user.Session;
import models.user.Token;
import models.user.User;
import play.Logger;
import play.api.mvc.Call;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.F;
import play.libs.Json;
import play.libs.OpenID;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.mainHome;
import views.html.userLogin;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Application extends Controller {

    public static final Map<String, String> openIdProviders = new HashMap<String, String>() {
        {
            put("google", "https://www.google.com/accounts/o8/id");
            put("steam", "http://steamcommunity.com/openid");
        }
    };


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

            if (boundForm.get().remember) {
                user.session.persistent = true;
            }

            user.update();
            session().put(UserSecured.SESSION_NAME, user.session.id.toString());
            Logger.info("Benuter {} hat sich erfolgreich angemeldet.", user.name);
            flash("success", "Willkommen, " + user.name);
            Call index = controllers.routes.Application.index();
            if (!boundForm.get().redirect.isEmpty()) {
                index = new Call("GET", boundForm.get().redirect);
            }
            return redirect(index);
        } else {
            Logger.info("User {} hat sich {}-mal falsch eingeloggt.", user.name, user.loginAttempts + 1);
            if (user.loginAttempts++ >= 5) {
                user.locked = true;
                user.lockTime = new Date();
                user.token = new Token();
                user.update();
                Logger.info("Benutzer {} wurde aufgrund zu vieler Fehlerhafter Logins gesperrt", user.name);
                flash("error", "Dein Account wurde aufgrund zu vieler fehlerhafter Login-Versuche temporär gesperrt.");
                return redirect(routes.Application.showLogin(null));
            }
            user.update();
            flash("warn", "Fehlerhaftes Passwort. Dies ist Versuch " + user.loginAttempts);
            return forbidden(userLogin.render(boundForm));

        }
    }

    public static Result logout() {
        User user = User.currentUser(ctx());
        if (user != null && user.session != null) {
            user.session.delete();
            session().remove(UserSecured.SESSION_NAME);
            flash("success", "Du wurdest erfolgreich ausgecheckt.");
        } else {
            flash("warn", "Du warst nicht eingeloggt");
        }
        return redirect(routes.Application.index());
    }


    public static Result auth() {
        Logger.debug("authenticate");
        String providerId = "google";
        String providerUrl = openIdProviders.get(providerId);
        String returnToUrl = "http://localhost:9000/login/verify";

        if (providerUrl == null) {
            return badRequest("Could not find provider " + providerId);
        }

        Map<String, String> attributes = new HashMap<>();
        attributes.put("Email", "http://schema.openid.net/contact/email");
//        attributes.put("FirstName", "http://schema.openid.net/namePerson/first");
//        attributes.put("LastName", "http://schema.openid.net/namePerson/last");
//        attributes.put("ProfileImage","http://openid.net/schema/media/image/480x640");

        F.Promise<String> redirectUrl = OpenID.redirectURL(providerUrl, returnToUrl, attributes, null, "http://localhost:9000/");
        return redirect(redirectUrl.get());
    }

    public static Result verify() {
        Logger.debug("verifyLogin");
        F.Promise<OpenID.UserInfo> userInfoPromise = OpenID.verifiedId();
        OpenID.UserInfo userInfo = userInfoPromise.get();
        JsonNode json = Json.toJson(userInfo);
        return ok(json);
    }
}
