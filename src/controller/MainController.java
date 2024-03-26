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
            trainerController.handleTrainerMenu();
        }
    }
}
