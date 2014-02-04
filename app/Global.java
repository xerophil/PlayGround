import models.Comment;
import models.Note;
import models.user.Group;
import models.user.User;
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

        User xero = new User("xero", "simon.beckstein@gmail.com");
        xero.save();
        xero.changePassword("xero");
        xero.update();
        User globe = new User("globe", "globefreak@gmail.com");
        globe.save();
        globe.changePassword("globe");

        Group admins = new Group("admins", xero);
        admins.users.add(globe);
        admins.save();

        super.onStart(app);
    }
}
