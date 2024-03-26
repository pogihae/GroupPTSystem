package controller;

import model.Reservation;
import model.Trainer;
import model.User;
import service.UserService;
import util.Utils;
import view.UserView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class UserController {
    UserView view = new UserView();
    //GroupPTRepository repo = new GroupPTRepository();
    Scanner sc = new Scanner(System.in);

    private final UserService userService = new UserService();

    public void login() {
        view.requestId();
        String id = sc.nextLine();
        view.requestPw();
        String pw = sc.nextLine();

        if (!userService.login(id, pw)) {
            view.printLoginFailed();
            return;
        }

        view.printLoginSuccess(UserService.loginedUser);
    }

    public void signUp(){
        view.showSignupMenu();
        view.requestName();
        String name = sc.nextLine();
        view.requestAge();
        String age = sc.nextLine();
        view.requestSex();
        String sex = sc.nextLine();
        view.requestId();
        String id = sc.nextLine();
        view.requestPw();
        String pw = sc.nextLine();
        view.requestPhoneNumber();
        String phoneNumber = sc.nextLine();
        view.requestRole();
        User.Role role = User.Role.valueOf(sc.nextLine());
        User user = new User(name,phoneNumber, Integer.parseInt(age), sex, id, pw, role);

        userService.signUp(user);
        view.showSigned();
    }

    public void consult(){
        view.showConsultMenu();
        String choice = sc.nextLine();
        switch (choice){
            case "1" -> reserveConsultation();
            case "2" -> checkMyReservation();
        }
    }

    public void reserveConsultation(){
        view.showReserveConsultation();
        List<Trainer> trainers = userService.findAllTrainers();
        Trainer trainer;
        for(int i = 0;i<trainers.size();i++){
            trainer = trainers.get(i);
            view.showListofTrainers(i,trainer.getName(),trainer.getSex(),"A");
        }

        view.trainersListMenu();
        String choice = sc.nextLine();
        trainer = trainers.get(Integer.parseInt(choice) - 1);
        System.out.println("test: "+trainer.getLessonDays());
        chooseAvailableTime(trainer);
    }

    private void chooseAvailableTime(Trainer trainer){
        List<Utils.Day> lessonDays = trainer.getLessonDays();
        List<LocalDateTime> availableTime = new ArrayList<>();
        LocalDateTime end = LocalDateTime.now().plusDays(8);

        // 1. lesson day -> closest day
        for (Utils.Day day : lessonDays) {
            LocalDateTime start = LocalDateTime.now().plusDays(1);
            while (!start.isAfter(end)) {
                if (day.equals(Utils.getDay(start.plusDays(1)))) {
                    // 2. 13 ~ 18
                    for (int hour = 13; hour < 19; hour++) {
                        LocalDateTime dateTime = LocalDateTime.of(LocalDate.from(start), LocalTime.of(hour, 0));
                        availableTime.add(dateTime);
                    }
                }
                start = start.plusDays(1);
            }
        }

        List<Reservation> trainerSchedule = userService.findReservationsByTrainer(trainer);
        System.out.println("TEST:"+trainerSchedule);
        Reservation schedule;
        for(int i = 0;i<trainerSchedule.size();i++){
            schedule = trainerSchedule.get(i);
            //1시 부터 7시까지 예약가능한 시간만 출력해 보여준다.
            availableTime.remove(schedule.getStartDate());
        }
        view.showAvailableTime(availableTime);

        String choice = sc.nextLine();
        //이름, 번호를 받아 User 객체를 생성한다.

        Reservation newReservation = new Reservation(trainer,availableTime.get(Integer.parseInt(choice)-1));
        view.requestName();
        String name = sc.nextLine();
        view.requestPhoneNumber();
        String phoneNumber = sc.nextLine();

        User user = new User(name,phoneNumber);
        //모든 형식이 적절하고, 내용이 중복되지 않으면..
        userService.saveReservation(user,newReservation);
        view.showResult("예약이");

    }

    public void checkMyReservation(){
        // 전화번호를 입력받는다.
        view.showCheckMyReservation();
        String phoneNumber = sc.nextLine();
        Reservation reservation = userService.checkMyReservation(phoneNumber);
        view.printReservation(reservation);
        view.myReservationMenu();
        String choice = sc.nextLine();
        switch (choice){
            case "1" -> changeReservation(reservation);
            case "2" -> cancelReservation(reservation);
        }
    }

    public void changeReservation(Reservation reservation){
        reserveConsultation();
        userService.cancelReservation(reservation);
        view.showResult("예약 변경");
    }

    public void cancelReservation(Reservation reservation){
        userService.cancelReservation(reservation);
        view.showResult("예약 취소");
    }
}