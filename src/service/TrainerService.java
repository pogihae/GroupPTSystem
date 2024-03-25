package service;

import model.Reservation;
import model.Trainer;
import model.User;
import repository.GroupPTRepository;
import util.Utils;

import java.util.List;

public class TrainerService {
    private final GroupPTRepository groupPTRepository = GroupPTRepository.getInstance();

    public int calculateIncome(Trainer trainer, int month) {
        int totalClassNumber = (int) groupPTRepository.findReservationsByTrainer(trainer).stream()
                .filter(r -> Utils.getMonth(r.getStartDate()) == month)
                .filter(Reservation::isClass)
                .count();

        return trainer.calculateIncome(totalClassNumber);
    }

    public List<Reservation> findReservationsByTrainer(Trainer trainer) {
        return groupPTRepository.findReservationsByTrainer(trainer);
    }

    public boolean setLessonDays(Trainer trainer, Utils.Day... days) {
        return trainer.setLessonDays(days);
    }

    public void addAttendants(Reservation reservation, User... users) {
        reservation.addAttendants(users);
    }

    public List<User> getMinors(Reservation reservation) {
        return reservation.getUsers().stream()
                .filter(r -> r.getAge() < 20)
                .toList();
    }

}