package controllers;

import models.Note;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.listNotes;

import java.util.List;


public class Application extends Controller {

    private static Form<Note> noteForm = Form.form(Note.class);

    public static Result index() {
        return redirect(routes.Application.listNotes());
    }

    public static Result listNotes() {
        List<Note> notes = Note.find.all();

        return ok(listNotes.render(notes, noteForm));
    }


    public static Result newNote() {
        Form<Note> boundForm = noteForm.bindFromRequest();

        if (boundForm.hasErrors()) {
            return badRequest(listNotes.render(Note.find.all(), boundForm));
        }
        Note note = boundForm.get();
        note.save();


        return redirect(routes.Application.listNotes());
    }

    public static Result showNote(java.util.UUID id) {
        return play.mvc.Results.TODO;
    }

    public static Result deleteNote(java.util.UUID id) {
        return play.mvc.Results.TODO;
    }
}
