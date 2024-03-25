package controller;

import model.Reservation;
import model.Trainer;
import model.User;
import service.TrainerService;
import util.Utils;
import view.TrainerView;

import java.util.List;

public class TrainerController {
    private final TrainerService service = new TrainerService();
    private final TrainerView view = new TrainerView();

    public void printTrainerTimeTable(Trainer trainer) {
        view.printTimeTable(service.findReservationsByTrainer(trainer));
    }

    public void setTrainerLessonDays(Trainer trainer) {
        Utils.Day[] lessonDays = view.requestLessonDays();
        service.setLessonDays(trainer, lessonDays);
    }

    public void checkTrainerIncome(Trainer trainer) {
        int[] yearMonth = view.requestIncomeYearAndMonth();
        int income = service.calculateIncome(trainer, yearMonth[0], yearMonth[1]);
        view.printIncome(trainer, yearMonth[0], yearMonth[1], income);
    }

    public void checkAttendances(Trainer trainer) {
        Reservation reservation = service.findCurrentReservation(trainer);
        List<User> attendances = view.requestAttendance(reservation);
        service.addAttendants(reservation, attendances.toArray(new User[0]));

        List<User> minors = service.getMinors(reservation);
        for (User minor : minors) {
            view.printMinorUserAttendance(attendances.contains(minor), minor);
        }
    }
}
