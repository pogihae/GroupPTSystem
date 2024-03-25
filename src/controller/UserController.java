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

public class UserController {
    UserView view = new UserView();
    //GroupPTRepository repo = new GroupPTRepository();
    Scanner sc = new Scanner(System.in);

    private final UserService userService = new UserService();

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
//        repo.saveMember(member);
//        repo.saveTrainer(trainer);
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
            view.showListofTrainers(i,trainer.getName(),trainer.getSex(),trainer.getGrade().name());
        }

        view.trainersListMenu();
        String choice = sc.nextLine();
        trainer = trainers.get(Integer.parseInt(sc.nextLine()));
        chooseAvailableTime(trainer);
    }

    public String chooseAvailableTime(Trainer trainer){
        //이름, 번호, 나이, 성별, 아이디, 비밀번호를 받아 User 객체를 생성한다.
        Utils.Day[] lessonDays = trainer.getLessonDays();
        List<LocalDate> availableDays = new ArrayList<>();
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = start.plusDays(6);
        List<LocalDateTime> availableTime = new ArrayList<>();

        for(Utils.Day day: lessonDays){
            LocalDate current = start; // 시작일부터 루프 시작
            while (!current.isAfter(end)) { // end 날짜까지 반복
                if(current.getDayOfWeek().getValue() == day.getDayOfWeek()) {//이 기간의 트레이너 예약 가능 요일
                    for (int hour = 13; hour <= 19; hour++) {
                        LocalDateTime dateTime = LocalDateTime.of(current, LocalTime.of(hour, 0));
                        availableTime.add(dateTime);
                    }
                }
                current = current.plusDays(1); // 다음 날짜로 이동
            }
        }

        List<Reservation> trainerSchedule = userService.findReservationsByTrainer(trainer);
        Reservation schedule;
        for(int i = 0;i<trainerSchedule.size();i++){
            schedule = trainerSchedule.get(i);
            //1시 부터 7시까지 예약가능한 시간만 출력해 보여준다.
            if(availableTime.contains(schedule.getStartDate())){
                availableTime.remove(i);
            }
        }
        view.showAvailableTime(availableTime);

        String choice = sc.nextLine();
//        Reservation newReservation = new Reservation(trainer,);
        return choice;
    }

    public void saveReservation(){
        view.requestName();
        String name = sc.nextLine();
        view.requestPhoneNumber();
        String phoneNumber = sc.nextLine();

        User user = new User(name,phoneNumber);
        //모든 형식이 적절하고, 내용이 중복되지 않으면..
        userService.saveReservation(user);
    }

    public void checkMyReservation(){
        // 이름/전화번호를 입력받는다.
        List<Reservation> list = userService.findAllReservations();
        view.showCheckMyReservation();
        String userInfo = sc.nextLine();
//        for(int i = 0;i<list.size();i++){
//            if(list.get(i).getUsers().contains()){
//
//            }
//        }
    }

    public void changeReservation(){
        List<Reservation> list = userService.findAllReservations();
    }

    public void cancelReservation(){

    }
}

