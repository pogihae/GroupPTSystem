package controller;

import model.*;
import repository.GroupPTRepository;
import service.AdminService;
import service.MemberService;
import service.TrainerService;
import service.UserService;
import view.UserView;
import util.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class MainController {

    public void run(boolean containSampleData) {
        if (containSampleData) {
            loadSampleData();
        }
        //시작
        final UserView userView = new UserView();
        userView.showLogo();
        userView.showStart();
        if (!userView.confirmAction("입력해주세요")) {
            userView.printlnError("종료합니다");
        }
        while (true) {
            try {
                runUtil();
            } catch (IllegalAccessException | NumberFormatException e) {
                userView.printlnError(e.getMessage());
            } catch (Exception e) {
                userView.printlnError("오류 발생! 다시 시도해주세요.");
            }
        }
    }

    private void runUtil() throws IllegalAccessException {

        final UserController userController = new UserController();
        while (!userController.hasLoginedUser()) {
            userController.run();
        }

        while (userController.hasLoginedUser()) {
            switch (UserService.loginedUser.getRole()) {
                case ADMIN -> {
                    final AdminController adminController = new AdminController(new AdminService(new TrainerService()));
                    adminController.runAdmin();
                }
                case TRAINER -> {
                    final TrainerController trainerController = new TrainerController();
                    trainerController.handleTrainerMenu();
                }
                case MEMBER -> {
                    final MemberController memberController = new MemberController();
                    memberController.handleMemberMenu();
                }
            }
        }

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
    // 오늘 17시 수업 예약 / 멤버1
    // 오늘 17시 수업 예약 / 멤버2
    // 4번 트레이너: 목금토
    // 오늘 14시 수업 예약 / 멤버 3
    // 5번 트레이너: 금토일
    // (취소됨) 오늘 15시 수업 예약 / 멤버 4
    public void loadSampleData() {
        System.out.println("-----테스트 데이터 저장중------");
        final GroupPTRepository repository = GroupPTRepository.getInstance();
        //repository.reset();

        // 유저 저장
        System.out.println("유저 저장");
        Member admin = new Member("관리자", "010-0000-0000", 21,
                "male", "a1", "a1", User.Role.ADMIN);
        repository.saveMember(admin);

        List<Member> members = new ArrayList<>(10);
        for (int i=1; i<=10; i++) {
            Member member = new Member("멤버"+i, Integer.toString(i), i, "male", "m"+i, "m"+i);
            members.add(member);
            repository.saveMember(member);
        }

        List<Trainer> trainers = new ArrayList<>(5);
        Utils.Day[][] trainerWorkDays = {
                {Utils.Day.MON, Utils.Day.TUE, Utils.Day.WED, Utils.Day.THU, Utils.Day.FRI, Utils.Day.SAT, Utils.Day.SUN},
                {Utils.Day.MON, Utils.Day.TUE, Utils.Day.WED, Utils.Day.THU, Utils.Day.FRI, Utils.Day.SAT, Utils.Day.SUN},
                {Utils.Day.MON, Utils.Day.TUE, Utils.Day.WED, Utils.Day.THU, Utils.Day.FRI, Utils.Day.SAT, Utils.Day.SUN},
                {Utils.Day.MON, Utils.Day.TUE, Utils.Day.WED, Utils.Day.THU, Utils.Day.FRI, Utils.Day.SAT, Utils.Day.SUN},
                {Utils.Day.MON, Utils.Day.TUE, Utils.Day.WED, Utils.Day.THU, Utils.Day.FRI, Utils.Day.SAT, Utils.Day.SUN}
        };
        for (int i=1; i<=5; i++) {
            Trainer trainer = new Trainer("트레이너"+i, Integer.toString(i), i, "male", "t"+i, "t"+i);
            trainer.setLessonDays(trainerWorkDays[i-1]);
            trainers.add(trainer);
            repository.saveTrainer(trainer);
        }

        // 모든 회원 승인
        System.out.println("회원 승인");
        final TrainerService trainerService = new TrainerService();
        final AdminService adminService = new AdminService(trainerService);

        adminService.approveUsers(trainers.stream().map(t -> (User) t).toList());
        adminService.approveUsers(List.of(members.get(0), members.get(1), members.get(2), members.get(3)));

        // 상담 예약 저장
        System.out.println("상담 저장");
        final UserService userService = new UserService();
        userService.saveReservation(
                new User("1번트레이너-15시상담", "010-1234-5678"),
                trainers.get(0),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0)));
        userService.saveReservation(
                new User("2번트레이너-13시상담", "010-5678-1234"),
                trainers.get(1),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0)));

        // 멤버 결제 및 예약
        System.out.println("결제 및 예약");
        final MemberService memberService = new MemberService();
        memberService.processPayment(members.get(0), Payment.PaymentOption.OPTION_1);
        memberService.makeReservation(members.get(0), null, trainers.get(2), LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(17, 0)));

        memberService.processPayment(members.get(2), Payment.PaymentOption.OPTION_2);
        memberService.makeReservation(members.get(2), null, trainers.get(3), LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(17, 0)));

        memberService.processPayment(members.get(3), Payment.PaymentOption.OPTION_3);
        memberService.makeReservation(members.get(3), null, trainers.get(4), LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(17, 0)));

        // 예약 취소 오늘 15시 수업 예약 / 멤버 4
        System.out.println("예약 취소");
        Reservation toCancel = repository.findReservationsByPhone(members.get(3).getPhoneNumber()).get(0);
//        System.out.println("예약됨: " + toCancel.isReservedUser(members.get(3)));
//        System.out.println("예약안됨: " + toCancel.isReservedUser(members.get(2)));
        memberService.cancelReservation(toCancel);

        // 동일 수업 다른 유저
        System.out.println("동일 수업 다른 유저");
        Reservation toAdd = repository.findReservationsByPhone(members.get(0).getPhoneNumber()).get(0);
        memberService.makeReservation(members.get(1), toAdd, (Trainer) toAdd.getManager(), toAdd.getStartDate());
    }
}
