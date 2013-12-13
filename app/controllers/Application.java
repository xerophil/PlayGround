package controllers;

import models.Note;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.listNotes;

import java.util.List;


public class Application extends Controller {

    public static Result index() {
        return redirect(routes.Application.listNotes());
    }

    public static Result listNotes() {
        List<Note> notes = Note.find.all();


        return ok(listNotes.render(notes));
    }
}
