package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Created on
 * Date: 13.12.13
 * Time: 19:08
 *
 * @author Simon Beckstein <simon.beckstein@gmail.com
 */
@Entity
public class Note extends Model {
    public static Finder<UUID, Note> find = new Finder<UUID, Note>(UUID.class, Note.class);

    @Id
    public UUID id;

    @Constraints.MaxLength(255)
    public String title;
    public String text;


}
