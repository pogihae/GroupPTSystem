package controller;

import model.Member;
import model.Trainer;
import model.User;
import repository.GroupPTRepository;
import util.Utils;

public class MainController {
    public void run() {
        final UserController userController = new UserController();
        userController.signUp();
        userController.login();

        Trainer trainer = new Trainer("name", "phone", 1, "gender", "id", "pw", User.Role.TRAINER);
        trainer.setLessonDays(new Utils.Day[] { Utils.Day.MON, Utils.Day.FRI, Utils.Day.SAT });

        GroupPTRepository.getInstance().saveTrainer(trainer);

        User user1 = new User();
        Member member1 = new Member(user1);
        member1.payForClass();
        member1.reserveClass();
    }
}
