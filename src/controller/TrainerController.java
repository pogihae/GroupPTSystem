package controller;

import model.Trainer;
import service.TrainerService;
import view.TrainerView;

public class TrainerController {
    private final TrainerService service = new TrainerService();
    private final TrainerView view = new TrainerView();

    public void printTrainerTimeTable(Trainer trainer) {
        view.printTimeTable(service.findReservationsByTrainer(trainer));
    }

    public void setTrainerLessonDays(Trainer trainer) {
        view.requestLessonDays();
    }

}
