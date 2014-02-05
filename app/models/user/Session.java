package models.user;

import models.Entity;

import javax.persistence.OneToOne;
import java.util.Date;
import java.util.UUID;

/**
 * Session object belonging to the user
 */
@javax.persistence.Entity
public class Session extends Entity {
    public static Finder<UUID, Session> find = new Finder<UUID, Session>(UUID.class, Session.class);


    /**
     * timestamp of the creation of this session
     */
    public Date creationTime;

    /**
     * timestamp of the last access of the user to the page
     */
    public Date lastAccess;

    /**
     * last IP address of the user
     */
    public String address;

    /**
     * wheter this session should expire after a while or not
     */
    public Boolean persistent = false;

    /**
     * User the session belongs to; mappe to the user (owner)
     */
    @OneToOne(mappedBy = "session", orphanRemoval = true)
    public User user;

    public Session() {
        creationTime = new Date();
        lastAccess = new Date();
    }


    /**
     * Overwritten to set the session null on the user-side before deleting it
     */
    @Override
    public void delete() {
        user.session = null;
        user.update();
        super.delete();
    }
}
