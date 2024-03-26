//package controller;
//
//import model.Member;
//import model.Reservation;
//import model.User;
//import repository.GroupPTRepository;
//import service.AdminService;
//import service.TrainerService;
//import service.UserService;
//import view.AdminView;
//
//import java.util.List;
//import java.util.Scanner;
//
//public class MainController {
//    public void run() {
//        final UserController userController = new UserController();
//        final TrainerController trainerController = new TrainerController();
//
//        userController.signUp();
//        userController.login();
//
//        TrainerController trainerController = new TrainerController();
//
//        final AdminView adminView = new AdminView();
//        final GroupPTRepository groupPTRepository = GroupPTRepository.getInstance();
//        final TrainerService trainerService = new TrainerService();
//        final AdminService adminService = new AdminService(trainerService);
//        final AdminController adminController = new AdminController(adminService);
//
//        adminController.runAdmin();
//        if (UserService.getLoginedUserRole().equals(User.Role.TRAINER)) {
//            trainerController.handleTrainerMenu();
//        }
//
//
//        User user1 = UserService.loginedUser;
//        Member member1 = new Member(user1);
//        userController.consult();
//        userController.checkMyReservation();
//        member1.payForClass();
//        member1.reserveClass();
//    }
//}
