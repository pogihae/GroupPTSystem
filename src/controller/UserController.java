package controller;

import model.Member;
import model.Trainer;
import model.User;
import repository.MemerRepository;
import view.UserView;

import java.util.Scanner;
enum Role{
    TRAINER, MEMBER, USER, ADMIN
}
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
    User user;
    Trainer trainer;
    Member member;
    void signUp(){
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
        Role role = Role.valueOf(sc.nextLine());
        user = new User(name,age,sex,id,pw,phoneNumber);
        if(role == Role.TRAINER){
//            trainer = new Trainer(user);
        }
        else if (role == Role.MEMBER){
//            member = new Member(user);
        }
//        repo.saveMember(member);
//        repo.saveTrainer(trainer);
        view.showSigned();
    }

    void consult(){

    }

    void reserveConsultation(){

    }

    void chooseAvailableTime(){

    }

    void checkMyReservation(){

    }

    void changeReservation(){

    }

    void cancelReservation(){

    }
}

