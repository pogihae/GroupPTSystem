package service;

import model.Member;
import model.Reservation;
import model.Trainer;
import model.User;
import repository.GroupPTRepository;

import java.util.List;
//view를 불러서는 안된다. repository에 직접적으로 접근하는 녀석
public class UserService {

    public static User loginedUser = null;

    public static User.Role getLoginedUserRole() {
        if (loginedUser == null) {
            throw new IllegalStateException("로그인한 유저가 존재하지 않습니다.");
        }
        return loginedUser.getRole();
    }

    private GroupPTRepository repository = GroupPTRepository.getInstance();

    public void login(User user) {
        if (loginedUser != null) {
            throw new IllegalStateException("이미 로그인한 유저가 존재합니다.");
        }
        loginedUser = user;
    }

    public void signUp(User user) {
        if (user.getRole().equals(User.Role.TRAINER)) {
            repository.saveTrainer(new Trainer(user));
        } else {
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

    public void chooseAvailableTime(){

    }
    public void saveReservation(User user){
        //addUser(user);
    }
    public List<Reservation> findAllReservations(){
        List<Reservation> list = repository.findAllReservations();
        return list;
    }

    public void changeReservation(){

    }

    public void cancelReservation(){

    }
}
