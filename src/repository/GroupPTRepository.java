package repository;

import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupPTRepository {

    /*-----------파일 저장 위치-----------*/
    private static final String DIRECTORY = "./data/";
    private static final String MEMBER_FILE = "members.dat";
    private static final String TRAINER_FILE = "trainers.dat";
    private static final String RESERVATION_FILE = "reservations.dat";
    private static final String PAYMENT_FILE = "payments.dat";

    private GroupPTRepository() {}
    private static GroupPTRepository instance = null;
    public static GroupPTRepository getInstance() {
        if (instance == null) {
            instance = new GroupPTRepository();
        }
        return instance;
    }

    /*-----------유저 기능-----------*/

    /**
     * 회원의 아이디로 회원 객체를 불러올 수 있다.
     *
     * @param id 유저의 아이디
     * @return 해당 아이디를 가지는 유저가 존재 시 User, 없을 시 null
     * */
    public User findUserById(String id) {
        // 멤버 파일에서 검색 (비회원, 회원, 관리자)
        User user = findAllMembers().stream()
                .filter(mUser -> mUser.getId().equals(id))
                .findFirst().orElse(null);

        // 트레이너 파일에서 검색 (트레이너)
        if (user == null) {
            user = findAllTrainers().stream()
                    .filter(mUser -> mUser.getId().equals(id))
                    .findFirst().orElse(null);
        }
        return user;
    }

    public User findUserByPhone(String phone) {
        return findAllUsers().stream()
                .filter(mUser -> mUser.getPhoneNumber().equals(phone))
                .findFirst().orElse(null);
    }

    /**
     * 모든 회원/비회원/트레이너/관리자 목록을 불러올 수 있다,
     * @return 모든 유저 목록
     * */
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        users.addAll(findAllMembers());
        users.addAll(findAllTrainers());
        return users;
    }

    /*-----------멤버 기능-----------*/

    /**
     * 멤버를 저장할 수 있다.
     * @param member 저장할 멤버
     * */
    public void saveMember(Member member) {
        addObjectToFile(MEMBER_FILE, member);
    }

    /**
     * 모든 멤버 목록을 불러올 수 있다.
     * @return 모든 멤버 목록
     * */
    @SuppressWarnings("unchecked")
    public List<Member> findAllMembers() {
        return (List<Member>) readFile(MEMBER_FILE);
    }

    /*-----------트레이너 기능-----------*/

    /**
     * 트레이너를 저장할 수 있다.
     * @param trainer 저장할 트레이너
     * */
    public void saveTrainer(Trainer trainer) {
        addObjectToFile(TRAINER_FILE, trainer);
    }

    /**
     * 트레이너를 수정할 수 있다.
     * @param trainer 수정할 트레이너
     * */
    public void updateTrainer(Trainer trainer) {
        findAllTrainers().stream()
                .filter(t -> t.getPhoneNumber().equals(trainer.getPhoneNumber()))
                .findFirst()
                .ifPresent(org -> org.update(trainer));
    }

    /**
     * 모든 트레이너 목록을 불러올 수 있다.
     * @return 모든 트레이너 목록
     * */
    @SuppressWarnings("unchecked")
    public List<Trainer> findAllTrainers() {
        return (List<Trainer>) readFile(TRAINER_FILE);
    }

    /*-----------예약 기능-----------*/

    /**
     * 모든 예약 목록을 불러올 수 있다.
     * 예약 정보 속 유저 정보들은 최신 정보로 업데이트 된다. (join)
     * @return 모든 예약 목록
     * */
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

    /**
     * 예약을 저장할 수 있다.
     * @param reservation 저장할 예약 정보
     * */
    public void saveReservation(Reservation reservation) {
        addObjectToFile(RESERVATION_FILE, reservation);
    }

    /**
     * 예약을 수정할 수 있다.
     * @param reservation 수정할 예약 정보
     * */
    public void updateReservation(Reservation reservation) {
        findAllReservations().stream()
                .filter(r -> r.getId().equals(reservation.getId()))
                .findFirst()
                .ifPresent(r -> r.update(reservation));
    }

    /**
     * 예약을 삭제할 수 있다.
     * @param reservation 삭제할 예약 정보
     * */
    public void deleteReservation(Reservation reservation) {
        List<Reservation> reservations = findAllReservations();
        reservations.remove(reservation);
        writeFile(RESERVATION_FILE, reservation);
    }

    /**
     * 해당하는 트레이너의 예약 목록읇 불러올 수 있다,
     * @param trainer 예약 목록을 불러올 트레이너
     * @return 트레이너의 예약 목록
     * */
    public List<Reservation> findReservationsByTrainer(Trainer trainer) {
        return findReservationsByPhone(trainer.getPhoneNumber());
    }

    /**
     * 핸드폰 번호를 기반으로 해당하는 유저의 예약 목록을 불러올 수 있다,
     * @param phone 찾을 유저의 핸드폰 번호
     * @return 유저의 예약 목록
     * */
    public List<Reservation> findReservationsByPhone(String phone) {
        return findAllReservations().stream()
                .filter(reservation ->
                    reservation.getManager().getPhoneNumber().equals(phone)
                )
                .collect(Collectors.toList());
    }

    /**
     * 유저의 노쇼 횟수를 알 수 있다.
     * @param user 확인할 유저
     * @return 해당하는 유저의 노쇼 힛수
     * */
    public int countNoShow(User user) {
        List<Reservation> reservations = findReservationsByPhone(user.getPhoneNumber());
        return (int) reservations.stream()
                .filter(r -> r.isNoShowUser(user))
                .count();
    }

    /*-----------결제 기능-----------*/

    public void savePayment(Payment payment) {
        addObjectToFile(PAYMENT_FILE, payment);
    }

    @SuppressWarnings("unchecked")
    public List<Payment> findAllPayments() {
        return (List<Payment>) readFile(PAYMENT_FILE);
    }

    @SuppressWarnings("unchecked")
    private void addObjectToFile(String fileName, Object object) {
        List<Object> objects = (List<Object>) readFile(fileName);
        objects.add(object);
        writeFile(fileName, objects);
    }

    private Object readFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DIRECTORY + fileName))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeFile(String fileName, Object object) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DIRECTORY + fileName))) {
            oos.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
