package controller;

import model.Reservation;
import model.Trainer;
import model.User;
import service.TrainerService;
import service.UserService;
import util.Utils;
import view.TrainerView;

import java.util.Arrays;
import java.util.List;

public class TrainerController {
    private final TrainerService service = new TrainerService();
    private final TrainerView view = new TrainerView();

    public void handleTrainerMenu() throws IllegalAccessException {
        if (!UserService.getLoginedUserRole().equals(User.Role.TRAINER)) {
            throw new IllegalStateException("트레이너로 로그인되어있지 않습니다,");
        }

        Trainer trainer = (Trainer) UserService.loginedUser;

        String input = view.requestTrainerMenu();
        switch (input) {
            case "1" -> printTrainerTimeTable(trainer);
            case "2" -> setTrainerLessonDays(trainer);
            case "3" -> checkAttendances(trainer);
            case "4" -> checkTrainerIncome(trainer);
        }
    }

    public void printTrainerTimeTable(Trainer trainer) {
        view.printTrainerReservations(service.findReservationsIsNotEnd(trainer));
    }

    public void setTrainerLessonDays(Trainer trainer) {
        Utils.Day[] lessonDays = view.requestLessonDays();
//        System.out.println("test" + Arrays.toString(lessonDays));
        System.out.println(service.setLessonDays(trainer, lessonDays));
    }

    public void checkTrainerIncome(Trainer trainer) {
        int[] yearMonth = view.requestIncomeYearAndMonth();
        int income = service.calculateIncome(trainer, yearMonth[0], yearMonth[1]);
        view.printIncome(trainer, income, yearMonth[0], yearMonth[1]);
    }

    public void checkAttendances(Trainer trainer) {
        Reservation reservation = service.findCurrentReservation(trainer);
        if (reservation == null) {
            System.out.println("수업 없음");
            return;
        }
        List<User> attendances = view.requestAttendance(reservation);
        service.addAttendants(reservation, attendances.toArray(new User[0]));

        List<User> minors = service.getMinors(reservation);
        for (User minor : minors) {
            view.printMinorUserAttendance(attendances.contains(minor), minor);
        }
    }
}
