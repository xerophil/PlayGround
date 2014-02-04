package controllers.securtity;


import models.user.Session;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Date;
import java.util.UUID;

/**
 * Created on
 * Date: 03.02.14
 * Time: 14:01
 *
 * @author Simon Beckstein <simon.beckstein@gmail.com
 */
public class UserSecured extends Security.Authenticator {

    /**
     * Session timeout, 30 min
     */
    public static final Long SESSION_TIMEOUT = new Long(30 * 60 * 1000);

    public static final Integer LOGIN_ATTEMPTS = 5;

    /**
     * The name of the session cookie
     */
    public static final String SESSION_NAME = "session_id";

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return redirect(controllers.routes.Application.showLogin());
    }

    @Override
    public String getUsername(Http.Context ctx) {
        ctx.flash().put("error","Keine Berechtigung diesen Bereich zu betreten. Bitte einloggen");
        String cookieID = ctx.session().get(SESSION_NAME);

        //no cookie present
        if (cookieID == null) {
            return null;
        }

        Session session = Session.find.byId(UUID.fromString(cookieID));

        //session isnt in database
        if (session == null) {
            return null;
        }

        //session isnt valid anymore
        if (session.lastAccess.getTime() + SESSION_TIMEOUT < System.currentTimeMillis() || session.user.locked) {
            session.delete();
            ctx.flash().put("error", "Session abgelaufen. Bitte erneut einloggen");
            return null;
        }

        //timestamp updaten
        if (!ctx.request().method().equals("POST")) {
            session.lastAccess = new Date();
            session.update();
        }
        ctx.flash().remove("error");
        return session.user.id.toString();


    }
}
