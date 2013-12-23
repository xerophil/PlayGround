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
}
