package application;

import application.controller.Controller;
import application.model.Model;
import application.view.View;

public class Application {
    public static void main(String[] args) {
        View view = new View("Signal processing #1");
        Model model = new Model();

        Controller controller = new Controller(view, model);
    }
}
