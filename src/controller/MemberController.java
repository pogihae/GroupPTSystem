package controller;

import model.*;
import repository.GroupPTRepository;
import service.MemberService;
import service.TrainerService;
import service.UserService;
import util.Utils;
import view.MemberView;
import view.TrainerView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MemberController {
    private final MemberService service = new MemberService();
    private final MemberView view = new MemberView();

    public void payForClass(Member member){
        view.displayPaymentOptions();
        int choice = view.getPaymentOptionsChoice(); //가격옵션번호
        Payment.PaymentOption selectedOption = null;
        switch (choice) {
            case 1:
                selectedOption = Payment.PaymentOption.OPTION_1;
                break;
            case 2:
                selectedOption = Payment.PaymentOption.OPTION_2;
                break;
            case 3:
                selectedOption = Payment.PaymentOption.OPTION_3;
                break;
            default:
                view.displayInvalidOption();
                break; // 메소드 종료
        }

        service.processPayment(member, selectedOption);
        //노쇼 1회 시 재등록(결제) 불가능하다
        assert selectedOption != null;
        view.displayPaymentResult(choice, selectedOption.getPrice());
        System.out.println(member.getPayment());
    }

    //수업예약
    public void reserveClass(Member member){
        //남은횟수 있는지 확인 후 => 있으면
        //1. 트레이너 선택메뉴 => 선택
        //2. 해당 트레이너의 수업 예약 가능한 시간 목록 출력 => 선택
        //3. 예약 확정 => 예약확정 메세지 출력
        if(member.getRemainSessionCount()==0){
            view.displayNoSessionsLeft();
            return;
        }
        //1. 트레이너 선택 메뉴(이름, 등급, 성별)
        List<Trainer> allTrainersList = service.findAllTrainers();
        view.displayTrainers(allTrainersList);
        int choice = view.getTrainerChoice();
        Trainer selectedTrainer = allTrainersList.get(choice-1);

        //2. 선택한 트레이너의 예약 목록 출력
        List<Reservation> reservationOfSelectedTrainer = service.findreservationOfSelectedTrainer(selectedTrainer);
        // users 리스트의 크기가 4개인 예약들만 필터링하여 새로운 리스트를 생성 filteredReservations
        List<Reservation> filteredReservations = service.findfilteredReservationsOfSelectedTrainer(reservationOfSelectedTrainer);

        // 트레이너의 근무 요일 배열을 DayOfWeek 타입의 리스트로 변환
        List<Utils.Day> workDays = selectedTrainer.getLessonDays();

        // 예약 가능한 시간대 목록 생성
        LocalDate today = LocalDate.now();
        LocalDate endDay = today.plusWeeks(1); //일주일 후
        Map<Integer, LocalDateTime> slotIndexToDateTimeMap= view.displayReservationSlots(today, endDay,
                                workDays, filteredReservations);

        //3. 사용자로부터 예약할 시간대 선택 받기
        int selectedSlotIndex = view.getReservationSlotChoice();
        // 선택한 시간대에 예약 객체 생성
        if (slotIndexToDateTimeMap.containsKey(selectedSlotIndex)) {
            LocalDateTime selectedDateTime = slotIndexToDateTimeMap.get(selectedSlotIndex);

            // 예약 객체 생성
            //reservationOfSelectedTrainer 의 reservation 중 startDate가 selectedDateTime과 같으면 거기에 추가, 아니면 객체생성
            Reservation existingReservation = null;
            // 선택한 시간대에 해당하는 예약이 있는지 검사
            for (Reservation reservation : reservationOfSelectedTrainer) {
                if (reservation.getStartDate().equals(selectedDateTime)) {
                    existingReservation = reservation;
                    break; // 일치하는 예약을 찾았으므로 루프 탈출
                }
            }
            service.makeReservation(member, existingReservation, selectedTrainer, selectedDateTime);
            view.displayReservationConfirmation(selectedDateTime);
        } else {
            view.displayInvalidChoice();
        }
    }//수업예약함수 END
        /*
        1. 원하는 시간이 없으면 ‘0’번을 눌러 다시 트레이너 목록이 보여진다.
        2. ‘*’을 누르면 메인화면으로 돌아간다.
        3. 선택지에 없는 메뉴를 선택할 경우 메뉴를 다시 출력한다.
         */

    //수업정보확인
    //변경할 예약객체를 리턴받는다.
    // 강사/ 시간/ 출석여부
    // 강사/ 시간/ 예약인원
    //잔여 수업횟수/ 잔여 수업 사용 가능 일수
    // 예약한 수업을 변경 혹은 취소하고 싶다면 해당 번호를 입력하세요
    // 강사/ 시간 >> 해당 예약 변경은 1번, 해당 예약 취소는 2번을 입력하세요
    public Reservation displayReservationInfo(Member member) {
        List<Reservation> reservationsByPhone = service.getReserationsOfUser(member);
        //예약된수업목록 출력
        view.displayReservationsOfUser(reservationsByPhone);
        //잔여 수업횟수/ 잔여 수업 사용 가능 일수 출력
        int calculateDaysRemaining = service.calculateDaysRemaining(member);
        view.printRemainSessionsOfUser(member, calculateDaysRemaining);
        // 사용자로부터 취소할 예약의 인덱스를 입력받음 (1부터 시작하는 인덱스; 리턴할 때 -1)
        int index = view.getReservationChoiceToUpdate();
        view.printLine();
        Reservation reservationToUpdate = service.getReservationToUpdate(index, member);//변경하고싶은 예약 번호 입력
        //해당 예약 변경할지 삭제할지 결정
        return reservationToUpdate;
    }
    //예약정보확인 -> 변경하고싶은 인덱스를 누른 경우
    public void updateOrCancel(Member member){
        Reservation reservationToUpdate = this.displayReservationInfo(member);
        int choice = view.getUpdateOrCancel(reservationToUpdate);
        switch (choice){
            case 1: this.updateClassReservation(reservationToUpdate, member);
                break;
            case 2: this.cancelClassReservation(reservationToUpdate, member);
                break;
        }
    }

    //displayReservationInfo() == 1 이라면
    //수업예약 변경
    //당일예약변경 불가능하다
    public void updateClassReservation(Reservation reservationToUpdate, Member member){
        LocalDate today = LocalDate.now();
        // 당일은 변경 가능한 목록이 아니라고 출력
        // 남은 세션 수 복구
        if (reservationToUpdate.getStartDate().toLocalDate().equals(today)) {
            view.displayUpdateRestrictionMessage();
            return;
        }
        service.restoreReducedSessionCount(member);
        service.cancelReservation(reservationToUpdate);
        this.reserveClass(member);
    }
    //당일취소할 경우, 취소는 되지만 횟수가 차감된다
    //displayReservationInfo() == 2 라면
    //수업예약취소
    public void cancelClassReservation(Reservation reservationToUpdate, Member member){
        // 유효한 인덱스 범위 검사
        //if (index >= 0 && index < reservationsByPhone.size())
        LocalDate today = LocalDate.now();
        //당일 취소 제외) 남은 세션 수 복구
        if (reservationToUpdate.getStartDate().toLocalDate().isAfter(today)) {
            service.restoreReducedSessionCount(member);
        }
        service.cancelReservation(reservationToUpdate);
        view.displayCancellationConfirmation(reservationToUpdate, member);
    }


    public void handleMemberMenu(){
        if (!UserService.getLoginedUserRole().equals(User.Role.MEMBER)) {
            throw new IllegalStateException("회원으로 로그인되어있지 않습니다,");
        }
        Member member = (Member)UserService.loginedUser;
        String input = view.requestMemberMenu();
        switch (input) {
            case "1" -> payForClass(member);
            case "2" -> reserveClass(member);
            case "3" -> updateOrCancel(member);
        }
    }




}
