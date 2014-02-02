import models.Comment;
import models.Note;
import play.Application;
import play.GlobalSettings;

/**
 * Created on
 * Date: 13.12.13
 * Time: 19:27
 *
 * @author Simon Beckstein <simon.beckstein@gmail.com
 */
public class Global extends GlobalSettings {


    @Override
    public void onStart(Application app) {

        if (Note.find.findRowCount() != 0) return;

        Note test = new Note("kommentiert", "trallala");

        test.save();


        test.comments.add(new Comment("Blubb"));
        test.comments.add(new Comment("tralla"));

        Comment thread = new Comment("ich bin ein troll");
        thread.comments.add(new Comment("stimmt"));
        thread.comments.add(new Comment("fjeden"));

        test.comments.add(thread);

        test.update();

        super.onStart(app);
    }
}
