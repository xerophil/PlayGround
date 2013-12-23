package models;

import javax.persistence.ManyToOne;
import java.util.UUID;

/**
 * Created on
 * Date: 23.12.13
 * Time: 08:11
 *
 * @author Simon Beckstein <simon.beckstein@gmail.com
 */
@javax.persistence.Entity
public class Comment extends Entity {

    @ManyToOne()
    UUID parent;

    String text;
}
