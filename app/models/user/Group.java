package models.user;

import models.Entity;
import play.data.validation.Constraints;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

/**
 * This class represents a Group in the database.
 */
@javax.persistence.Entity()
@Table(name = "groups")
public class Group extends Entity {

    /**
     * reference to object finder
     */
    public static Finder<UUID, Group> find = new Finder<UUID, Group>(UUID.class, Group.class);

    /**
     * The name of the groupList, its a required field with min length of 3 and max length of 10
     * Only alphanumeric characters are allowed and the name must be unique
     */
    @Constraints.Required
    @Constraints.MaxLength(10)
    @Constraints.MinLength(3)
    @Column(unique = true)
    @Constraints.Pattern(value = "^[A-Za-z_][A-Za-z\\d_]*$", message = "Bitte nur Buchstaben und Unterstriche als Gruppennamen eingeben")
    public String name;

    /**
     * A built in groupList cannot be deleted by its owner. This flag must not be set in the application
     */
    public Boolean builtin;


    /**
     * The list of groupList members
     */
    @ManyToMany(cascade = CascadeType.ALL)
    public List<User> users;


    /**
     * Default constructor for Group, you shouldn't use this
     */
    public Group() {
        super();
        builtin = false;
    }

    /**
     * Creates a new Group with the given name and owner.
     * The admin and builtin flag are set to false, this shouldn't be changed
     *
     * @param name
     * @param owner
     */
    public Group(String name, User owner) {
        super();
        this.name = name;
        builtin = false;
    }

    public String validate() {
        return find.where().eq("name", name).findUnique() != null ? "not unique" : null;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", users=" + users.size() +
                '}';
    }
}
