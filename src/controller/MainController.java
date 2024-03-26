package controller;

import model.Member;
import model.Reservation;
import model.User;
import repository.GroupPTRepository;
import service.AdminService;
import service.TrainerService;
import service.UserService;

import java.util.List;
import java.util.Scanner;

public class MainController {
    public void run() {
        final UserController userController = new UserController();
        final TrainerController trainerController = new TrainerController();

//        userController.signUp();
        userController.login();

        //final AdminView adminView = new AdminView();
        final GroupPTRepository groupPTRepository = GroupPTRepository.getInstance();
        final TrainerService trainerService = new TrainerService();
        final AdminService adminService = new AdminService(trainerService);
        final AdminController adminController = new AdminController(adminService);

        adminController.runAdmin();
        if (UserService.getLoginedUserRole().equals(User.Role.TRAINER)) {
            trainerController.handleTrainerMenu();
        }
//        TrainerController trainerController = new TrainerController();
//
//        if (UserService.getLoginedUserRole().equals(User.Role.TRAINER)) {
//            trainerController.handleTrainerMenu();
//        }

        User user1 = UserService.loginedUser;
        Member member1 = (Member)user1;
//        userController.consult();
//        userController.checkMyReservation();
//        member1.payForClass();
//        member1.printMyPayment();
//        System.out.println(member1.getRemainSessionCount());
//        member1.reserveClass();
//        member1.reserveClass();
        List<Reservation> allreservations = member1.findAllReservations();
        for (Reservation reservation : allreservations) {
            System.out.println("전체예약내역 출력중: "+reservation.toString());
        }
        Reservation reservationToUpdate = member1.displayReservationInfo();
        System.out.println("이게 선택한 예약임: "+reservationToUpdate.toString());
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
