package models.user;

import ch.qos.logback.core.net.SyslogOutputStream;
import controllers.securtity.UserSecured;
import helper.HashHelper;
import models.Entity;
import play.Logger;
import play.data.validation.Constraints;
import play.mvc.Http;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * This class represents a User in the database
 */
@javax.persistence.Entity
@Table(name = "users")
public class User extends Entity {

    /**
     * reference to object finder
     */

    public static Finder<UUID, User> find = new Finder<UUID, User>(UUID.class, User.class);


    /**
     * Version of the user in the database

     @Version public Long version;*/


    /**
     * the name of the user
     */
    @Constraints.Required
    @Constraints.MinLength(3)
    @Constraints.MaxLength(10)
    @Constraints.Pattern(value = "^[A-Za-z_][A-Za-z\\d_]*$", message = "Bitte nur Buchstaben und Unterstriche als Nutzernamen eingeben")
    public String name;


    /**
     * The email adress of the user
     */
    @Constraints.Email
    @Constraints.Required
    @Constraints.MaxLength(254)
    public String mail;


    /**
     * salted sha512 hash of password
     */
    String password;


    /**
     * Flag for users that are built in from the beginning
     */
    public Boolean builtin = false;

    /**
     * whether the user is locked or not
     */
    public Boolean locked = false;

    /**
     * The time when the user was locked
     */
    public Date lockTime;

    /**
     * Timestamp of the last login
     */
    public Date lastLogin;

    public Date creationTime = new Date();

    /**
     * The count of false login attempts
     */
    public Integer loginAttempts = 0;


    /**
     * IF not null, the session of the user
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    public Session session;

    /**
     * If not null, the token is used to reset or unlock the user
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    public Token token;

    /**
     * The groups the user is a member of
     */
    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
    public List<Group> groups;


    /**
     * Default constructor for User, you shouldnt use this
     */
    public User() {
        super();
    }

    /**
     * @param name
     * @param mail
     * @param password
     */
    public User(String name, String mail) {
        super();
        this.name = name;
        this.mail = mail;
    }

    /**
     * Changes the password of the user. This method performs a hashing algorithm on the password
     *
     * @param newPassword the password to change
     */
    public void changePassword(String newPassword) {
        try {
            password = HashHelper.getHash(newPassword, id.toString());
        } catch (Exception e) {
            Logger.error("Couldn't change password", e);
        }
    }

    /**
     * Performs a check if the given parameter matches the password in the database.
     *
     * @param password
     * @return
     */
    public boolean checkPassword(String password) {
        try {
            return this.password.equals(HashHelper.getHash(password, id.toString()));
        } catch (Exception e) {
            Logger.error("Couldn't check users authenticity");
            return false;
        }
    }

    /**
     * True if the user is not locked
     *
     * @return
     */
    public boolean isLocked() {
        return locked;// || System.currentTimeMillis() - lockTime.getTime() < 15L * 60L * 1000L;
    }

    public static User currentUser(Http.Context ctx) {
        String cookieValue = ctx.session().get(UserSecured.SESSION_NAME);
        if (cookieValue == null) {
            Logger.debug("Unable to lookup User: No cookie value stored");
            return null;
        }

        Session session = Session.find.byId(UUID.fromString(cookieValue));

        if (session == null) {
            Logger.debug("Unable to lookup User: Session is not present in Database");
            return null;
        }


        return session.user;
    }


    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }


}
