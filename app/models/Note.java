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
@javax.persistence.Entity
public class Note extends Entity {
    public static Finder<UUID, Note> find = new Finder<>(UUID.class, Note.class);


    @Constraints.Required(message = "Darf nicht leer sein")
    @Constraints.MaxLength(value = 10, message = "Max. 10 Zeichen")
    @Constraints.MinLength(value = 3, message = "Min. 3 Zeichen")
    public String title;
    public String text;


    public Note(String text, String title) {
        this.text = text;
        this.title = title;
    }

    public Note(String title) {
        this(title, "");
    }

    public Note() {
    }

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                "} " + super.toString();
    }
}
