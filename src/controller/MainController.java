package controller;

import model.Member;
import model.Reservation;
import model.Trainer;
import model.User;
import repository.GroupPTRepository;
import util.Utils;

import java.util.List;
import java.util.Scanner;

public class MainController {
    public void run() {
        Scanner sc = new Scanner(System.in);
        final UserController userController = new UserController();
        userController.signUp();

        Trainer trainer = new Trainer("name", "phone", 1, "gender", "id", "pw", User.Role.TRAINER);
        trainer.setLessonDays(new Utils.Day[] { Utils.Day.MON, Utils.Day.FRI, Utils.Day.SAT });

        GroupPTRepository.getInstance().saveTrainer(trainer);

        User user1 = new User("김현주", "010-1111-1111");
        Member member1 = new Member(user1);
//        member1.payForClass();
//        member1.reserveClass();
//        member1.reserveClass();
//        member1.reserveClass();
        Reservation selectedReservation = member1.displayReservationInfo();//Reservation 객체 return
        System.out.println(selectedReservation.toString());
        int index = sc.nextInt();
        switch (index){
            case 1: member1.updateClassReservation(selectedReservation);
            break;
            case 2: member1.cancelClassReservation(selectedReservation);
            break;
        }

    }
}
