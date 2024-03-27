package service;

import model.Member;
import model.Reservation;
import model.Trainer;
import model.User;
import repository.GroupPTRepository;

import java.time.LocalDateTime;
import java.util.*;
//view를 불러서는 안된다. repository에 직접적으로 접근하는 녀석
public class UserService {

    public static User loginedUser = null;

    public static User.Role getLoginedUserRole() {
        if (loginedUser == null) {
            throw new IllegalStateException("로그인한 유저가 존재하지 않습니다.");
        }
        return loginedUser.getRole();
    }

    private final GroupPTRepository repository = GroupPTRepository.getInstance();

    public boolean login(String id, String pw) {
        if (loginedUser != null) {
            throw new IllegalStateException("이미 로그인한 유저가 존재합니다.");
        }

        User user = repository.findUserById(id);
        System.out.println(user);
        if (user == null || !user.getPw().equals(pw)) {
            return false;
        }
        loginedUser = user;
        return true;
    }

    public void signUp(String name,String phoneNumber,int age,String sex,String id,String pw, User.Role role) {
        User user = new User(name,phoneNumber, age, sex, id, pw, role);
        if (user.getRole().equals(User.Role.TRAINER)) {
            user.setState(User.State.APPROVED);
            repository.saveTrainer(new Trainer(user));
        } else {
            user.setState(User.State.APPROVED);
            repository.saveMember(new Member(user));
        }
    }

    public List<Trainer> findAllTrainers(){
        List<Trainer> list = repository.findAllTrainers();
        return list;
    }

    public List<Reservation> findReservationsByTrainer(Trainer trainer){
        List<Reservation> list =  repository.findReservationsByTrainer(trainer);
        return list;
    }

    public void saveReservation(User user, Trainer trainer, LocalDateTime startTime){
        Reservation reservation = new Reservation(trainer, startTime);

        reservation.addUser(user);
        reservation.setType(Reservation.Type.CONSULT);
        repository.saveReservation(reservation);
    }
    public List<Reservation> findAllReservations(){
        List<Reservation> list = repository.findAllReservations();
        return list;
    }

    public Reservation checkMyReservation(String phoneNumber){
        List<Reservation> list = repository.findReservationsByPhone(phoneNumber);
        return (list.isEmpty())? null : list.get(0);
    }

    public void cancelReservation(Reservation reservation){
        repository.deleteReservation(reservation);
    }

    //중복된 아이디인지 체크
    public boolean isDuplicatePhone(String arg) {
        System.out.println(repository.findUserByPhone(arg));
        return repository.findUserByPhone(arg)==null;
    }

    //중복된 번호인지 체크
    public boolean isDuplicateId(String arg) {
        return repository.findUserById(arg)==null;
    }


   //최소한 하나의 영문 소/대문자, 최소한 하나의 숫자, 최소한 하나의 특수문자를 포함해서 8자 이상의 비밀번호
    public boolean isValidPw(String pw){
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return pw.matches(regex);
    }

    //010-1111-1111
    public boolean isValidPhone(String phone){
        String regex = "^010-\\d{4}-\\d{4}$";
        return phone.matches(regex);
    }

    /*
   길이는 최소한 4자에서 16자까지
   영문자, 숫자, 밑줄(_), 마침표(.)만을 허용
   첫 글자는 영문자로 시작
    */
    public boolean isValidId(String id){
        String regex = "^[a-zA-Z][a-zA-Z0-9._]{3,15}$";
        return id.matches(regex);
    }
}
