package controller;

import model.Member;
import model.Reservation;
import model.Trainer;
import model.User;
import repository.GroupPTRepository;
import service.AdminService;
import service.TrainerService;
import service.UserService;
import util.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TestController {
    public void run() {
        loadSampleData();

        // 1. 관리자
        TrainerService trainerService = new TrainerService();
        AdminController adminController = new AdminController(new AdminService(trainerService));
        adminController.runAdmin();
    }

    // 0. test data set
    // 관리자 * 1명
    // 이름: 관리자 아이디: a1 비밀번호: a1
    // 멤버 * 10명
    // 멤버 1번 / 이름: 멤버1 아이디: m1 비밀번호: m1 번호: 1
    // 이하동일규칙
    // 트레이너 * 5명
    // 트레이너 요일
    // 1번 트레이너: 월화수
    // 오늘 15시 상담 예약 / 이름: 1번트레이너-15시상담 번호: 010-1234-5678
    // 2번 트레이너: 화수목
    // 오늘 13시 상담 예약 / 이름: 2번트레이너-13시상담 번호: 010-5678-1234
    // 3번 트레이너: 일수목
    // 4번 트레이너: 목금토
    // 5번 트레이너: 금토일
    public void loadSampleData() {
        final GroupPTRepository repository = GroupPTRepository.getInstance();

        // 유저 저장
//        Member admin = new Member("관리자", "010-0000-0000", 21,
//                "male", "a1", "a1", User.Role.ADMIN);
//        repository.saveMember(admin);
//
//        List<Member> members = new ArrayList<>(10);
//        for (int i=1; i<=10; i++) {
//            Member member = new Member("멤버"+i, Integer.toString(i), i, "male", "m"+i, "m"+i);
//            members.add(member);
//            repository.saveMember(member);
//        }

        List<Trainer> trainers = new ArrayList<>(5);
        Utils.Day[][] trainerWorkDays = {
                {Utils.Day.MON, Utils.Day.TUE, Utils.Day.WED},
                {Utils.Day.TUE, Utils.Day.WED, Utils.Day.THU},
                {Utils.Day.SUN, Utils.Day.WED, Utils.Day.THU},
                {Utils.Day.THU, Utils.Day.FRI, Utils.Day.SAT},
                {Utils.Day.FRI, Utils.Day.SAT, Utils.Day.SUN}
        };
        for (int i=1; i<=5; i++) {
            Trainer trainer = new Trainer("트레이너"+i, Integer.toString(i), i, "male", "m"+i, "m"+i);
            trainer.setLessonDays(trainerWorkDays[i-1]);
            trainers.add(trainer);
            repository.saveTrainer(trainer);
        }

        // 상담 예약 저장
        final UserService userService = new UserService();
        userService.saveReservation(
                new User("1번트레이너-15시상담", "010-1234-5678"),
                trainers.get(0),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0)));
        userService.saveReservation(
                new User("2번트레이너-13시상담", "010-5678-1234"),
                trainers.get(0),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0)));
    }
}
