package service;

import model.Reservation;
import model.Trainer;
import model.User;
import repository.GroupPTRepository;
import util.Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TrainerService {
    private final GroupPTRepository groupPTRepository = GroupPTRepository.getInstance();

    public int calculateIncome(Trainer trainer, int month, int year) {
        int totalClassNumber = (int) groupPTRepository.findReservationsByTrainer(trainer).stream()
                .filter(r -> r.getStartDate().getMonthValue() == month)
                .filter(r -> r.getStartDate().getYear() == year)
                .count();

        return trainer.calculateIncome(totalClassNumber);
    }

    public List<Reservation> findReservationsByTrainer(Trainer trainer) {
        return groupPTRepository.findReservationsByTrainer(trainer);
    }

    public List<Reservation> findReservationsIsNotEnd(Trainer trainer) {
        return findReservationsByTrainer(trainer).stream()
                .filter(r -> !r.isEnd())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Reservation findCurrentReservation(Trainer trainer) {
        LocalDateTime now = Utils.getCurrentTime();

        return groupPTRepository.findReservationsByTrainer(trainer).stream()
                .filter(r -> Utils.getYear(r.getStartDate()) == now.getYear())
                .filter(r -> Utils.getMonth(r.getStartDate()) == now.getMonth().getValue())
                .filter(r -> Utils.getDate(r.getStartDate()) == now.getDayOfMonth())
                .filter(r -> !r.isEnd())
                .min(Comparator.comparing(Reservation::getStartDate))
                .orElse(null);
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
                .filter(u -> u.getRole() != null && u.getRole().equals(User.Role.MEMBER))
                .filter(r -> r.getAge() < 20)
                .toList();
    }

}
