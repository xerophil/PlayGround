package models;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created on
 * Date: 23.12.13
 * Time: 08:11
 *
 * @author Simon Beckstein <simon.beckstein@gmail.com
 */
@javax.persistence.Entity
public class Comment extends Entity {

    @ManyToOne
    public Comment parent;

    @OneToMany(mappedBy = "parent")
    public List<Comment> comments;

    @ManyToOne
    public Note comment;

    public String text;

    public Comment(String text) {
        this.text = text;
    }

    public Comment() {
    }
}
