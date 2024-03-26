package repository;

import model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupPTRepository {

    /*-----------파일 저장 위치-----------*/
    private static final String DIRECTORY = "data/";
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

        // 파일 생성
        File[] files = {
                new File(DIRECTORY + MEMBER_FILE),
                new File(DIRECTORY + TRAINER_FILE),
                new File(DIRECTORY + RESERVATION_FILE),
                new File(DIRECTORY + PAYMENT_FILE)
        };
        try {
            if (!new File(DIRECTORY).exists()) {
                Files.createDirectory(Paths.get(DIRECTORY));
            }
            for (File file : files) {
                if (!file.exists()) {
                    if (!file.createNewFile()) {
                        throw new IOException("파일 생성 실패");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        List<Member> res = (List<Member>) readFile(MEMBER_FILE);
        return (res == null)? new ArrayList<>() : res;
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
        List<Trainer> trainers = findAllTrainers();
        trainers.stream()
                .filter(t -> t.getPhoneNumber().equals(trainer.getPhoneNumber()))
                .findFirst()
                .ifPresent(org -> {
                    org.update(trainer);
                    writeFile(TRAINER_FILE, trainers);
                });
    }

    /**
     * 모든 트레이너 목록을 불러올 수 있다.
     * @return 모든 트레이너 목록
     * */
    @SuppressWarnings("unchecked")
    public List<Trainer> findAllTrainers() {
        List<Trainer> res = (List<Trainer>) readFile(TRAINER_FILE);
        return (res == null)? new ArrayList<>() : res;
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

        if (reservations == null) {
            reservations = new ArrayList<>();
        }

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
        System.out.println(reservation);
        addObjectToFile(RESERVATION_FILE, reservation);
    }

    /**
     * 예약을 수정할 수 있다.
     * @param reservation 수정할 예약 정보
     * */
    public void updateReservation(Reservation reservation) {
        List<Reservation> reservations = findAllReservations();

        reservations.stream()
                .filter(r -> r.getId().equals(reservation.getId()))
                .findFirst()
                .ifPresent(r -> {
                    r.update(reservation);
                    writeFile(RESERVATION_FILE, reservations);
                });
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
        final User user = findUserByPhone(phone);
        System.out.println(user);
        return findAllReservations().stream()
                .filter(r -> r.isReservedUser(user))
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

    public Payment findPaymentByPhoneNumber(String phoneNumber) {
        List<Payment> payments = findAllPayments();
        for (Payment payment : payments) {
            if (payment.getMemberPhoneNumber().equals(phoneNumber)) {
                return payment;
            }
        }
        return null; // 해당하는 결제 정보가 없을 경우 null 반환
    }

    @SuppressWarnings("unchecked")
    public List<Payment> findAllPayments() {
        List<Payment> res = (List<Payment>) readFile(PAYMENT_FILE);
        if (res == null) {
            res = new ArrayList<>();
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    private void addObjectToFile(String fileName, Object object) {
        List<Object> objects = (List<Object>) readFile(fileName);
        if (objects == null) {
            objects = new ArrayList<>();
        }
        objects.add(object);
        writeFile(fileName, objects);
    }

    private Object readFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DIRECTORY + fileName))) {
            return ois.readObject();
        } catch (ClassNotFoundException | IOException e2) {
            return null;
        }
    }

    private void writeFile(String fileName, Object object) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DIRECTORY + fileName))) {
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
