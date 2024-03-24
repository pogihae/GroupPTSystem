package repository;

import model.Member;
import model.Reservation;
import model.Trainer;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TODO join
public class GroupPTRepository {
    private static final String MEMBER_FILE = "members.dat";
    private static final String TRAINER_FILE = "trainers.dat";
    private static final String RESERVATION_FILE = "reservations.dat";

    public User findUserById(String id) {
        User user = findAllMembers().stream()
                .filter(mUser -> mUser.getId().equals(id))
                .findFirst().orElse(null);
        if (user == null) {
            user = findAllTrainers().stream()
                    .filter(mUser -> mUser.getId().equals(id))
                    .findFirst().orElse(null);
        }
        return user;
    }

    public void saveMember(Member member) {
        addObjectToFile(MEMBER_FILE, member);
    }

    public void saveTrainer(Trainer trainer) {
        addObjectToFile(TRAINER_FILE, trainer);
    }

    public void updateTrainer(Trainer trainer) {
        findAllTrainers().stream()
                .filter(t -> t.getPhoneNumber().equals(trainer.getPhoneNumber()))
                .findFirst()
                .ifPresent(org -> org.update(trainer));
    }

    @SuppressWarnings("unchecked")
    public List<Member> findAllMembers() {
        return (List<Member>) readFile(MEMBER_FILE);
    }

    @SuppressWarnings("unchecked")
    public List<Trainer> findAllTrainers() {
        return (List<Trainer>) readFile(TRAINER_FILE);
    }

    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        users.addAll(findAllMembers());
        users.addAll(findAllTrainers());
        return users;
    }

    @SuppressWarnings("unchecked")
    public List<Reservation> findAllReservations() {
        List<Reservation> reservations = (List<Reservation>) readFile(RESERVATION_FILE);

        reservations.forEach(r -> {
            List<User> mUsers = r.getUsers();
            mUsers.forEach(u -> u.update(findUserById(u.getId())));

            User mManager = r.getManager();
            mManager.update(findUserById(mManager.getId()));
        });

        return reservations;
    }

    public void saveReservation(Reservation reservation) {
        addObjectToFile(RESERVATION_FILE, reservation);
    }

    public void updateReservation(Reservation reservation) {
        findAllReservations().stream()
                .filter(r -> r.getId().equals(reservation.getId()))
                .findFirst()
                .ifPresent(r -> r.update(reservation));
    }

    public void deleteReservation(Reservation reservation) {
        List<Reservation> reservations = findAllReservations();
        reservations.remove(reservation);
        writeFile(RESERVATION_FILE, reservation);
    }

    public List<Reservation> findReservationsByTrainer(Trainer trainer) {
        return findReservationsByPhone(trainer.getPhoneNumber());
    }

    public List<Reservation> findReservationsByPhone(String phone) {
        return findAllReservations().stream()
                .filter(reservation ->
                    reservation.getManager().getPhoneNumber().equals(phone)
                )
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private void addObjectToFile(String fileName, Object object) {
        List<Object> objects = (List<Object>) readFile(fileName);
        objects.add(object);
        writeFile(fileName, objects);
    }

    private Object readFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeFile(String fileName, Object object) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
