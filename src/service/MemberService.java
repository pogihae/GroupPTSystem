package service;

import model.*;
import repository.GroupPTRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MemberService {
    private final GroupPTRepository groupPTRepository = GroupPTRepository.getInstance();
    public void processPayment(Member member, Payment.PaymentOption selectedOption) {
        Payment payment = new Payment(LocalDate.now(), member.getPhoneNumber(), selectedOption);
        //payment 객체를 "결제" 파일에 저장하는 작업
        groupPTRepository.savePayment(payment);
        updateInfoOfMember(member, payment, member.getRemainSessionCount() + selectedOption.getSessions());
    }
    private void updateInfoOfMember(Member member, Payment payment, int remainSessionCount) {
//        System.out.println(payment);
//        System.out.println(remainSessionCount);
        member.setRemainSessionCount(remainSessionCount);
        member.setPayment(payment);
        //새로운 payment와 reaminsessionscount로 member를 파일에서 업데이트하는 작업
        groupPTRepository.updateMember(member);
    }
    public List<Trainer> findAllTrainers(){
        return groupPTRepository.findAllTrainers();
    }
    public List<Reservation> findreservationOfSelectedTrainer(Trainer selectedTrainer){
        return groupPTRepository.findReservationsByTrainer(selectedTrainer);
    }
    public List<Reservation> findfilteredReservationsOfSelectedTrainer(Member member, List<Reservation> reservationOfSelectedTrainer){
        int[] reservedDate = groupPTRepository.findReservationsByPhone(member.getPhoneNumber()).stream()
                .mapToInt(r -> r.getStartDate().getDayOfMonth())
                .toArray();
        System.out.println(Arrays.toString(reservedDate));
        return reservationOfSelectedTrainer.stream()
                .filter(reservation -> reservation.isReservedUser(member) || reservation.isFull())
                .filter(reservation -> Arrays.stream(reservedDate).anyMatch(date -> date == reservation.getStartDate().getDayOfMonth()))
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .toList();
    }
    public void makeReservation(Member member, Reservation existingReservation, Trainer selectedTrainer, LocalDateTime selectedDateTime){
        if (existingReservation != null) {
            System.out.println("ALREADY RESERVED");
            existingReservation.addUser(member);
            groupPTRepository.updateReservation(existingReservation);
        } else { //해당시간에 예약한 사람이 0명인 경우
            Reservation newReservation = new Reservation(selectedTrainer, selectedDateTime);
            newReservation.setType(Reservation.Type.CLASS);
            newReservation.addUser(member);
            groupPTRepository.saveReservation(newReservation);
        }
        //횟수차감
        member.setRemainSessionCount(member.getRemainSessionCount()-1);
        //바뀐 멤버로 파일 업데이트
        groupPTRepository.updateMember(member);
    }
    public void restoreReducedSessionCount(Member member){
        member.setRemainSessionCount(member.getRemainSessionCount()+1);
        groupPTRepository.updateMember(member);
    }
    public List<Reservation> getReserationsOfUser(Member member){
        return groupPTRepository.findReservationsByPhone(member.getPhoneNumber());
    }
    //잔여수업사용 가능 일수 구하기
    public int calculateDaysRemaining(Member member){
        if(member.getPayment()!=null){
            LocalDate paymentdate = member.getPayment().getPaymentTime();
            int validDays = member.getPayment().getPaymentOption().getValidDays();
            LocalDate expirationDate = paymentdate.plusDays(validDays);
            LocalDate today = LocalDate.now();

            Period period = Period.between(today, expirationDate);
            return period.getDays() + period.getMonths()*30;
            //+period.getYears()*365
        }
        return 0; //결제 정보가 없는 경우
    }
    //예약 취소
    public void cancelReservation(Reservation reservationToUpdate){
        groupPTRepository.deleteReservation(reservationToUpdate);
    }

}
