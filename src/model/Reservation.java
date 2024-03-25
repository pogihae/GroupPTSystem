package model;

import lombok.Getter;
import util.Utils;

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

    /**
     * 예약 업데이트
     * 보통 join을 위해 사용된다.
     * @param reservation 최신 예약 정보
     *
     * */
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

    /**
     * 예약한 유저 추가
     * @param user 예약한 유저
     * */
    public void addUser(User user) {
        this.users.add(user);
    }

    /**
     * 출석체크
     * @param users 출석한 유저들 (ex. addAttendants(user1, user2, user3))
     * */
    public void addAttendants(User... users) {
        attendants.addAll(Arrays.asList(users));
    }

    /**
     * 유저가 해당 예약을 노쇼했는지 여부
     * @param user 확인할 유저
     * @return 노쇼: true | 예약안함 or 시작 시간 전 or 출석: false
     * */
    public boolean isNoShowUser(User user) {
        if (!isReservedUser(user)) return false;
        if (Utils.isOverDate(startDate)) return false;
        return !attendants.contains(user);
    }

    /**
     * 유저가 예약한 수업(상담) 여부를 알 수 있다.
     * */
    public boolean isReservedUser(User user) {
        return users.contains(user);
    }
}
