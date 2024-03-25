package controller;

import model.Reservation;
import model.Trainer;
import model.User;
import service.UserService;
import view.UserView;
import java.util.*;

public class UserController {
    UserView view = new UserView();
    //GroupPTRepository repo = new GroupPTRepository();
    Scanner sc = new Scanner(System.in);

    private UserService userService;

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
        User user = new User(name,age,sex,id,pw,phoneNumber,role);

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

    public void chooseAvailableTime(Trainer trainer){
        //이름, 번호, 나이, 성별, 아이디, 비밀번호를 받아 User 객체를 생성한다.

        List<Reservation> trainerSchedule = userService.findReservationsByTrainer(trainer);
        Reservation schedule;
        Calendar scheduledDate = Calendar.getInstance();
        List<Integer> availableTime = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7));
        for(int i = 0;i<trainerSchedule.size();i++){
            schedule = trainerSchedule.get(i);
            //1시 부터 7시까지 예약가능한 시간만 출력해 보여준다.
            scheduledDate.setTime(schedule.getStartDate());
            if(availableTime.contains(scheduledDate.get(Calendar.HOUR_OF_DAY))){
                availableTime.remove(i);
            }
        }
        view.showAvailableTime(availableTime);
        String choice = sc.nextLine();

        view.requestName();
        String name = sc.nextLine();
        view.requestPhoneNumber();
        String phoneNumber = sc.nextLine();
        User user = new User(name,phoneNumber);
        //모든 형식이 적절하고, 내용이 중복되지 않으면..
//        chosenTime.addUser(user);
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

