package controller;

import model.Member;
import model.Trainer;
import model.User;
import repository.GroupPTRepository;
import service.AdminService;
import service.TrainerService;
import service.UserService;
import util.Utils;
import view.AdminView;

public class MainController {
    public void run() {
        final UserController userController = new UserController();
//        userController.signUp();
//        userController.login();
//
//        TrainerController trainerController = new TrainerController();
//
//        if (UserService.getLoginedUserRole().equals(User.Role.TRAINER)) {
//            trainerController.handleTrainerMenu();
//        }
//

        //final AdminView adminView = new AdminView();
        final GroupPTRepository groupPTRepository = GroupPTRepository.getInstance();
        final TrainerService trainerService = new TrainerService();
        final AdminService adminService = new AdminService(groupPTRepository, trainerService);
        final AdminController adminController = new AdminController(adminService);



//        userController.login();
//
//        TrainerController trainerController = new TrainerController();
//
//        if (UserService.getLoginedUserRole().equals(User.Role.TRAINER)) {
//            Trainer trainer = (Trainer) UserService.loginedUser;
//            trainerController.setTrainerLessonDays(trainer);
//            trainerController.checkTrainerIncome(trainer);
//            trainerController.printTrainerTimeTable(trainer);
//            trainerController.checkAttendances(trainer);
//        }
//
//        User user1 = UserService.loginedUser;
//        Member member1 = new Member(user1);
//        //userController.consult();
//
//        member1.payForClass();
//        member1.reserveClass();
//        member1.reserveClass();

        adminController.runAdmin();
    }
}
