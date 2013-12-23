package models;

import play.data.validation.Constraints;

import java.util.UUID;

/**
 * Created on
 * Date: 13.12.13
 * Time: 19:08
 *
 * @author Simon Beckstein <simon.beckstein@gmail.com
 */
@Entity
public class Note extends models.Entity {
    public static Finder<UUID, Note> find = new Finder<UUID, Note>(UUID.class, Note.class);


    @Constraints.Required
    @Constraints.MaxLength(100)
    public String title;
    public String text;


}
