package service;

import model.Member;
import model.Trainer;
import model.User;
import repository.GroupPTRepository;

import java.util.List;

public class UserService {

    public static User loginedUser = null;

    public static User.Role getLoginedUserRole() {
        if (loginedUser == null) {
            throw new IllegalStateException("로그인한 유저가 존재하지 않습니다.");
        }
        return loginedUser.getRole();
    }

    private GroupPTRepository repository;

    public void login(User user) {
        if (loginedUser != null) {
            throw new IllegalStateException("이미 로그인한 유저가 존재합니다.");
        }
        loginedUser = user;
    }

    public void signUp(User user) {
        if (user.getRole().equals(User.Role.TRAINER)) {
//            repository.saveTrainer(new Trainer(user));
        } else {
            repository.saveMember(new Member(user));
        }
    }

    public List<Trainer> reserveConsultation(){
        List<Trainer> list = repository.findAllTrainers();
        return list;
    }

    public void chooseAvailableTime(){

    }

    public void checkMyReservation(){

    }

    public void changeReservation(){

    }

    public void cancelReservation(){

    }
}
