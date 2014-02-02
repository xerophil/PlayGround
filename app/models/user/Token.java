package models.user;

import models.Entity;

import javax.persistence.OneToOne;
import java.util.Date;
import java.util.UUID;

/**
 * Unique token used for (initial) password reset and unlocking account
 */
@javax.persistence.Entity
public class Token extends Entity {
    public static Finder<UUID, Token> find = new Finder<UUID, Token>(UUID.class, Token.class);

    /**
     * Timestamp of creation
     */
    public Date timestamp;

    /**
     * The user the token belongs to; owned by the user
     */
    @OneToOne(mappedBy = "token", orphanRemoval = true)
    public User user;

    public Token() {
        timestamp = new Date();
    }

    /**
     * Overwritten to set the token null on the user-side before deleting it
     */
    @Override
    public void delete() {
        user.token = null;
        user.update();
        super.delete();
    }
}
