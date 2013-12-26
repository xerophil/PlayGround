package models;

import play.db.ebean.Model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * Created on
 * Date: 23.12.13
 * Time: 08:08
 *
 * @author Simon Beckstein <simon.beckstein@gmail.com
 */
@MappedSuperclass
public class Entity extends Model {
    @Id
    public UUID id;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;
        if (!super.equals(o)) return false;

        Entity entity = (Entity) o;

        if (id != null ? !id.equals(entity.id) : entity.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                '}';
    }
}
