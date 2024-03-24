package service;

import model.Member;
import model.Trainer;
import model.User;
import repository.GroupPTRepository;

import java.util.List;

public class UserService {

    private GroupPTRepository repository;

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
