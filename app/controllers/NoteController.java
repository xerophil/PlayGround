package controllers;

import controllers.securtity.UserSecured;
import models.Note;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.noteList;

import java.util.List;

/**
 * Created on
 * Date: 03.02.14
 * Time: 14:18
 *
 * @author Simon Beckstein <simon.beckstein@gmail.com
 */
@Security.Authenticated(UserSecured.class)
public class NoteController extends Controller {


    private static Form<Note> noteForm = Form.form(Note.class);

    public static Result listNotes() {
        List<Note> notes = Note.find.all();

        return ok(noteList.render(notes, noteForm));
    }

    public static Result createNote() {
        Form<Note> boundForm = noteForm.bindFromRequest();

        if (boundForm.hasErrors()) {
            return badRequest(noteList.render(null, boundForm));
        }
        Note note = boundForm.get();
        note.save();


        return redirect(routes.NoteController.listNotes());
    }

    public static Result showNote(java.util.UUID id) {
        Note note = Note.find.byId(id);
        if (note == null) {
            return notFound();
        }

        Form<Note> filledForm = noteForm.fill(note);

        return ok(noteList.render(null, filledForm));

    }

    public static Result deleteNote(java.util.UUID id) {
        Note note = Note.find.byId(id);
        if (note == null) {
            return notFound();
        }

        note.delete();
        return redirect(routes.NoteController.listNotes());
    }

    public static Result updateNote(java.util.UUID id) {

        Form<Note> boundForm = noteForm.bindFromRequest();

        if (boundForm.hasErrors()) {
            return badRequest(noteList.render(null, boundForm));
        }
        Note note = boundForm.get();
        note.update(id);

        return redirect(routes.NoteController.listNotes());

    }
}
