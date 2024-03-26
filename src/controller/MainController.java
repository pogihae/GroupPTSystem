package controller;

import model.Member;
import model.Reservation;
import model.Trainer;
import model.User;
import repository.GroupPTRepository;
import service.UserService;
import util.Utils;

import java.util.Scanner;

public class MainController {
    public void run() {
        final UserController userController = new UserController();
        Trainer trainer = new Trainer("sample", "123", 12, "m", "id", "pw", User.Role.TRAINER);
        trainer.setLessonDays(new Utils.Day[]{Utils.Day.FRI, Utils.Day.MON});
        GroupPTRepository.getInstance().saveTrainer(trainer);
        //userController.signUp();
        userController.login();

//        TrainerController trainerController = new TrainerController();
//
//        if (UserService.getLoginedUserRole().equals(User.Role.TRAINER)) {
//            Trainer trainer = (Trainer) UserService.loginedUser;
//            trainerController.setTrainerLessonDays(trainer);
//            trainerController.checkTrainerIncome(trainer);
//            trainerController.printTrainerTimeTable(trainer);
//            trainerController.checkAttendances(trainer);
//        }

        User user1 = new User();
        Member member1 = new Member(user1);
//        userController.consult();
//        userController.checkMyReservation();
        member1.payForClass();
        member1.reserveClass();
        Reservation reservationToUpdate = member1.displayReservationInfo();
        Scanner sc = new Scanner(System.in);
        int index = sc.nextInt();
        switch (index){
            case 1: member1.updateClassReservation(reservationToUpdate);
            break;
            case 2: member1.cancelClassReservation(reservationToUpdate);
            break;
        }

    }
}
