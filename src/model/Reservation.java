package model;

import lombok.Getter;

import java.util.*;

@Getter
public class Reservation {
    private final Long id;
    private User manager;
    private List<User> users;
    private List<User> attendants;
    private Date startDate;
    private int durationMinute;

    public Reservation(User manager, Date startDate, int durationMinute) {
        this.id = new Random().nextLong(Long.MAX_VALUE);
        this.manager = manager;
        this.users = new ArrayList<>();
        this.attendants = new ArrayList<>();
        this.startDate = startDate;
        this.durationMinute = durationMinute;
    }

    public void update(Reservation reservation) {
        if (!reservation.id.equals(this.id)) {
            throw new IllegalArgumentException("NO MATCHED RESERVATION");
        }
        this.manager = reservation.manager;
        this.users = reservation.users;
        this.attendants = reservation.attendants;
        this.startDate = reservation.startDate;
        this.durationMinute = reservation.durationMinute;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void addAttendants(User... users) {
        attendants.addAll(Arrays.asList(users));
    }
}
