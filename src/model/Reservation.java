package model;

import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class Reservation {
    private Long id;
    private User manager;
    private List<User> users;
    private Date startDate;
    private int durationMinute;

    public void update(Reservation reservation) {
        //set
    }
}
