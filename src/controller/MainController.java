package controller;

import model.Member;
import model.Reservation;
import model.Trainer;
import model.User;
import repository.GroupPTRepository;
import service.UserService;
import util.Utils;

import java.util.List;
import java.util.Scanner;

public class MainController {
    public void run() {
        final UserController userController = new UserController();
        final MemberController memberController = new MemberController();
//        userController.signUp();
         userController.login();

        TrainerController trainerController = new TrainerController();

        if (UserService.getLoginedUserRole().equals(User.Role.TRAINER)) {
            trainerController.handleTrainerMenu();
        }
        if (UserService.getLoginedUserRole().equals(User.Role.MEMBER)) {
            memberController.handleMemberMenu();
        }



    }
}
