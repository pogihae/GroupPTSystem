package service;

import model.Reservation;
import model.Trainer;
import model.User;
import repository.GroupPTRepository;
import util.Utils;

import java.time.LocalDateTime;
import java.util.List;

public class TrainerService {
    private final GroupPTRepository groupPTRepository = GroupPTRepository.getInstance();

    public int calculateIncome(Trainer trainer, int month, int year) {
        int totalClassNumber = (int) groupPTRepository.findReservationsByTrainer(trainer).stream()
                .filter(r -> Utils.getMonth(r.getStartDate()) == month)
                .filter(r -> r.getStartDate().getYear() == year)
                .filter(Reservation::isClass)
                .count();

        return trainer.calculateIncome(totalClassNumber);
    }

    public List<Reservation> findReservationsByTrainer(Trainer trainer) {
        return groupPTRepository.findReservationsByTrainer(trainer);
    }

    public Reservation findCurrentReservation(Trainer trainer) {
        LocalDateTime now = Utils.getCurrentTime();

        return groupPTRepository.findReservationsByTrainer(trainer).stream()
                .filter(r -> Utils.getYear(r.getStartDate()) == now.getYear())
                .filter(r -> Utils.getMonth(r.getStartDate()) == now.getMonth().getValue())
                .filter(r -> Utils.getDate(r.getStartDate()) == now.getDayOfMonth())
                .findFirst().orElse(null);
    }

    public boolean setLessonDays(Trainer trainer, Utils.Day... days) {
        if (trainer.setLessonDays(days)) {
            groupPTRepository.updateTrainer(trainer);
            return true;
        }
        return false;
    }

    public void addAttendants(Reservation reservation, User[] users) {
        reservation.addAttendants(users);
    }

    public List<User> getMinors(Reservation reservation) {
        return reservation.getUsers().stream()
                .filter(r -> r.getAge() < 20)
                .toList();
    }

}
