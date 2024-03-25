package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import util.Utils;

import java.io.Serializable;
import java.util.*;

@Getter
public class Reservation implements Serializable {
    private static final int MAX_CLASS_NUM = 4;
    private static final int MAX_CONSULT_NUM = 1;

    private final Long id;
    private Type type;
    private User manager;
    private List<User> users;
    private List<User> attendants;
    private Date startDate;
    private int durationMinute;

    @AllArgsConstructor
    public enum Type {
        CLASS(4), CONSULT(1);

        final int maxNum;
    }

    public Reservation(Type type, User manager, Date startDate, int durationMinute) {
        this.id = new Random().nextLong(Long.MAX_VALUE);
        this.type = type;
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
        this.type = reservation.type;
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
     * 유저가 예약한 수업(상담)인지 여부를 알 수 있다.
     * */
    public boolean isReservedUser(User user) {
        return users.contains(user);
    }

    /**
     * 유저가 해당 예약을 취소할 수 있다.
     * @param user 취소할 유저
     * @return 취소 성공: true | 예약하지 않은 유저 : false
     * */
    public boolean cancelReservation(User user) {
        if (!isReservedUser(user)) return false;
        return users.remove(user);
    }

    /**
     * 가득찬 예약인지를 확인할 수 있다.
     * */
    public boolean isFull() {
        return users.size() >= type.maxNum;
    }

    public boolean isClass() {
        return this.type.equals(Type.CLASS);
    }
}
