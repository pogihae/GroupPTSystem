package controller;

import model.Member;
import model.Trainer;
import model.User;
import repository.GroupPTRepository;
import service.UserService;
import util.Utils;

public class MainController {
    public void run() {
        final UserController userController = new UserController();
        userController.signUp();
        userController.login();

        TrainerController trainerController = new TrainerController();

        if (UserService.getLoginedUserRole().equals(User.Role.TRAINER)) {
            Trainer trainer = (Trainer) UserService.loginedUser;
            trainerController.setTrainerLessonDays(trainer);
            trainerController.checkTrainerIncome(trainer);
            trainerController.printTrainerTimeTable(trainer);
            trainerController.checkAttendances(trainer);
        }
//
//        User user1 = UserService.loginedUser;
//        Member member1 = new Member(user1);
        userController.consult();
        userController.checkMyReservation();
//        member1.payForClass();
//        member1.reserveClass();
    }
}
