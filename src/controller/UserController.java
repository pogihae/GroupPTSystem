package controller;

import model.Trainer;
import model.User;
import service.UserService;
import view.UserView;

import java.util.List;
import java.util.Scanner;

class MenuException extends Exception{
    MenuException(){

    }

    MenuException(String message){
        super(message);
    }
}

public class UserController {
    UserView view = new UserView();
    //GroupPTRepository repo = new GroupPTRepository();
    Scanner sc = new Scanner(System.in);

    private UserService userService;

    public void signUp(){
        view.showSignupMenu();
        view.requestName();
        String name = sc.nextLine();
        view.requestAge();
        String age = sc.nextLine();
        view.requestSex();
        String sex = sc.nextLine();
        view.requestId();
        String id = sc.nextLine();
        view.requestPw();
        String pw = sc.nextLine();
        view.requestPhoneNumber();
        String phoneNumber = sc.nextLine();
        view.requestRole();
        User.Role role = User.Role.valueOf(sc.nextLine());
        User user = new User(name,age,sex,id,pw,phoneNumber, role);

        userService.signUp(user);
//        repo.saveMember(member);
//        repo.saveTrainer(trainer);
        view.showSigned();
    }

    public void consult(){
        view.showConsultMenu();
        String choice = sc.nextLine();
        switch (choice){
            case "1" -> reserveConsultation();
            case "2" -> checkMyReservation();
        }
    }

    public void reserveConsultation(){
        view.showReserveConsultation();
        List<Trainer> Trainer = userService.reserveConsultation();
        for(Trainer trainer:Trainer){
            trainer.getName();
        }
    }

    public void checkMyReservation(){
        view.showCheckMyReservation();
    }

    public void chooseAvailableTime(){

    }

    public void changeReservation(){

    }

    public void cancelReservation(){

    }
}

