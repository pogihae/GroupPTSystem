package controller;

import model.Reservation;
import model.Trainer;
import model.User;
import service.UserService;
import util.Utils;
import view.UserView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.time.LocalDateTime;

public class UserController {
    private final UserView view = new UserView();
    private final UserService userService = new UserService();

    public void login() {
        String id = view.requestId();
        String pw = view.requestPw();

        if (!userService.login(id, pw)) {
            view.printLoginFailed();
            return;
        }
        view.printLoginSuccess(UserService.loginedUser);
    }

    public void signUp(){
        view.showSignupMenu();
        String name = view.requestName();
        int age = view.requestAge();
        String sex = view.requestSex();
        String id = view.requestId();
        //정규표현식 확인
        if(!userService.isDuplicateId(id)){
            view.printRegisterFailed(1);
            return;
        }
        if(!userService.isValidId(id)) {
            view.printRegisterFailed(3);
            return;
        }
        String pw = view.requestPw();
        if(!userService.isValidPw(pw)){
            view.printRegisterFailed(4);
            return;
        }
        String phoneNumber = view.requestPhoneNumber();
        if(!userService.isDuplicatePhone(phoneNumber)){
            view.printRegisterFailed(2);
            return;
        }
        if(!userService.isValidPhone(phoneNumber)){
            view.printRegisterFailed(5);
            return;
        }

        int roleChoice = view.requestRole();
        User.Role role = (roleChoice==1)? User.Role.TRAINER: User.Role.MEMBER;
        userService.signUp(name,phoneNumber, age, sex, id, pw, role);
        view.showSigned();
    }

    public void consult(){
        String choice = view.showConsultMenu();
        switch (choice){
            case "1" -> reserveConsultation();
            case "2" -> checkMyReservation();
        }
    }

    public Trainer requestTrainers(){
        view.showReserveConsultation();
        List<Trainer> trainers = userService.findAllTrainers();
        String choice = view.showTrainers(trainers);
        return trainers.get(Integer.parseInt(choice) - 1);
    }

    public void reserveConsultation(){
        Trainer trainer = requestTrainers();
        LocalDateTime start = chooseAvailableTime(trainer);
        makeConsultReservation(trainer, start);
    }

    private LocalDateTime chooseAvailableTime(Trainer trainer){
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

        //1시 부터 7시까지 예약가능한 시간만 출력해 보여준다.
        for (Reservation reservation : trainerSchedule) {
            schedule = reservation;
            availableTime.remove(schedule.getStartDate());
        }
        String choice = view.showAvailableTime(availableTime);
//        if(choice.equals("0")){
//            requestTrainers();
//            return;
//        }
        //이름, 번호를 받아 User 객체를 생성한다.

        return availableTime.get(Integer.parseInt(choice)-1);
    }

    private void makeConsultReservation(Trainer trainer, LocalDateTime startTime) {
        String name =  view.requestName();
        String phoneNumber = view.requestPhoneNumber();
        User user = new User(name,phoneNumber);

        //모든 형식이 적절하고, 내용이 중복되지 않으면..
        userService.saveReservation(user,trainer,startTime);
        view.showResult("예약이");
    }

    public void checkMyReservation(){
        // 전화번호를 입력받는다.
        String phoneNumber = view.showCheckMyReservation();
        Reservation reservation = userService.checkMyReservation(phoneNumber);
        view.printReservation(reservation);
        String choice = view.myReservationMenu();
        switch (choice){
            case "1" -> changeReservation(reservation);
            case "2" -> cancelReservation(reservation);
        }
    }

    public void changeReservation(Reservation reservation){
        userService.cancelReservation(reservation);

        Trainer trainer = requestTrainers();
        LocalDateTime start = chooseAvailableTime(trainer);
        User user = reservation.getUsers().get(0);
        userService.saveReservation(user,trainer,start);

        view.showResult("예약 변경");
    }

    public void cancelReservation(Reservation reservation){
        if(view.confirmAction("정말로 취소하시겠습니까?")){
            userService.cancelReservation(reservation);
            view.showResult("예약 취소");
            return;
        }
        checkMyReservation();
    }
}