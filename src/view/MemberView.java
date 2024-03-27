package view;

import model.Member;
import model.Reservation;
import model.Trainer;
import repository.GroupPTRepository;
import util.Color;
import util.Utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MemberView extends BaseView{
    public void displayPaymentOptions() {
        println(formatTitle("결제 단위를 선택해주세요"));
        println(SEPARATOR);
        println("1. 10회 - 90일 - 총가격 ₩700,000");
        println("2. 20회 - 120일 - 총가격 ₩1,200,000");
        println("3. 30회 - 180일 - 회당 ₩1,500,000");
        println(SEPARATOR);
    }

    public int getPaymentOptionsChoice() {
        print(".   /\\_/\\\n" +
                "  /  • - • \\\n" +
                "/ づ \uD83C\uDF38づ  [ 번호 입력 ] =>  ");
        String input = readLine();
        int choice = 0;
        //예외처리
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            println("숫자를 입력해주세요.");
            // 여기서 잘못된 입력에 대한 처리를 할 수 있습니다.
        }
        println(SEPARATOR);
        return choice;
    }

    public void displayPaymentResult(int choice, int priceOfSelectedOption) {
        println(Color.GREEN + "  　  /)⋈/)\n" +
                "    (｡•ㅅ•｡)♡\n" +
                " ┏--∪-∪━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓\n" +
                " ♡ " +choice+"번 옵션 , "+priceOfSelectedOption+"원 결제가 완료되었습니다 .。♡\n" +
                " ┗-━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n"+ Color.ANSI_RESET);
    }
    public void displayInvalidOption() {
        printlnError("잘못된 선택입니다.");
    }

    public void displayTrainers(List<Trainer> allTrainersList){
        println(formatTitle("수업 예약"));
        println(formatTitle("예약을 원하는 트레이너의 번호를 입력해주세요."));
        for (int i = 0; i < allTrainersList.size(); i++) {
            Trainer trainer = allTrainersList.get(i);
            println("[%d] %s 트레이너/ 등급 %s/ %s".formatted(i + 1, trainer.getName(), trainer.getGrade(), trainer.getSex()));
        }
        printlnError(SEPARATOR);
    }
    public int getTrainerChoice() {
        print(".   /\\_/\\\n" +
                "  /  • - • \\\n" +
                "/ づ \uD83C\uDF38づ  [ 번호 입력 ] => ");
        int choice = Integer.parseInt(readLine());
        return choice;
        //+) 유효한 번호가 아니면 메뉴 다시 보여주기 구현해야함
    }
    // 예약 가능한 시간대 목록 생성
    public Map<Integer, LocalDateTime> displayReservationSlots(LocalDate today, LocalDate endDay, List<Utils.Day> workDays, List<Reservation> filteredReservations) {
        LocalTime startTime = LocalTime.of(13,0);
        LocalTime endTime = LocalTime.of(19,0);
        today = LocalDate.from(today.atStartOfDay());
        //idx(1~), LocalDateTime 예약한시간
        println("filtered: " + filteredReservations);
        Map<Integer, LocalDateTime> slotIndexToDateTimeMap = new HashMap<>();
        int slotIndex = 1;
        for (LocalDate date = today.plusDays(1); !date.isAfter(endDay); date = date.plusDays(1)) {
            if (workDays.contains(Utils.getDay(date.plusDays(1).atStartOfDay()))) {
                println(formatTitle("[%s]의 예약 가능 시간".formatted(date.toString())));
                println(SEPARATOR);
                //해당 시간: tempStartTime
                LocalTime tempStartTime = startTime;
                while (tempStartTime.isBefore(endTime)) {
                    LocalDateTime slotStartDateTime = LocalDateTime.of(date, tempStartTime);
                    LocalDateTime slotEndDateTime = slotStartDateTime.plusHours(1);

                    boolean isReserved = false;
                    for (Reservation reservation : filteredReservations) {
                        int rDate = reservation.getStartDate().getDayOfMonth();
                        int sDate = slotStartDateTime.getDayOfMonth();
                        int rHour = reservation.getStartDate().getHour();
                        int sHour = slotStartDateTime.getHour();

                        if ((rDate == sDate) && (rHour == sHour)) {
                            isReserved = true;
                            break;
                        }
                    }

                    if (!isReserved) {
                        println("[ "+slotIndex + " ]"+ ". " + slotStartDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd, E"))
                                + " - " + slotStartDateTime.format(DateTimeFormatter.ofPattern("HH:mm")));
                        slotIndexToDateTimeMap.put(slotIndex, slotStartDateTime);
                        slotIndex++;
                    }

                    tempStartTime = tempStartTime.plusHours(1);
                }
            }
        }
        return slotIndexToDateTimeMap;
    }
    public int getReservationSlotChoice() {
        print(".   /\\_/\\\n" +
                "  /  • - • \\\n" +
                "/ づ \uD83C\uDF38づ  [ 예약할 시간의 번호를 입력해주세요 ] => ");
        int choice = Integer.parseInt(readLine());
        System.out.println("--------------------------------------------");
        return choice;
    }
    public void displayNoSessionsLeft() {
        System.out.println(Color.RED+"!!!!!!!!!!남은 횟수가 없습니다. 재결제가 필요합니다!!!!!!!!!!" + Color.ANSI_RESET);
        //printlnError("!!!!!!!!!!남은 횟수가 없습니다. 재결제가 필요합니다!!!!!!!!!!");
    }
    public void displayReservationConfirmation(LocalDateTime selectedDateTime) {
        println(Color.GREEN + "  　  /)⋈/)\n" +
                "    (｡•ㅅ•｡)♡\n" +
                " ┏--∪-∪━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓\n" +
                " ♡  \" 예약이 완료되었습니다. \"\n\t[ 예약일자: "+ selectedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " ]\n"
                +" ┗-━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n"+ Color.ANSI_RESET);
        //System.out.println("[ 예약일자: " + selectedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " ]\n");
    }
    public void displayInvalidChoice() {
        printlnError("!!!!!!!!!!잘못된 번호입니다. 다시 입력해주세요!!!!!!!!!!");
    }
    // 예약된 수업 목록 출력
    public void displayReservationsOfUser(List<Reservation> reservationsByPhone){
        for (int i = 0; i < reservationsByPhone.size(); i++) {
            //예약된 수업 목록 출력 양식: idx. 강사/ 시간/ 정원
            //System.out.println((i+1) + ": " + reservationsByPhone.get(i).toString());
            Reservation reservation = reservationsByPhone.get(i);
            println("[ %d ] %s 트레이너/ %s/ 예약인원 %d명".formatted(
                    i + 1, reservation.getManager().getName(),
                    reservation.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    reservation.getUsers().size()
            ));
        }
    }

    //잔여 수업횟수/ 잔여 수업 사용 가능 일수 출력
    public void printRemainSessionsOfUser(Member member, int calculateDaysRemaining){
        printlnError("고객님의 잔여 횟수는 %d회, 잔여 수업 사용가능일수는 %d일 입니다".formatted(
                member.getRemainSessionCount(), calculateDaysRemaining
        ));

    }
    // 사용자로부터 변경할 예약의 인덱스를 입력받음 (1부터 시작하는 인덱스)
    // 인덱스가 아닌 다른 숫자를 누르면 메인화면으로 돌아간다.
    public int getReservationChoiceToUpdate(){
        System.out.println("예약한 수업을 변경 혹은 취소하고 싶다면 해당 예약의 인덱스를 입력하세요 ==>");
        System.out.print(".   /\\_/\\\n" +
                "  /  • - • \\\n" +
                "/ づ \uD83C\uDF38づ  [ 번호 입력 ] => ");
        return Integer.parseInt(readLine())-1;
    }

    //변경을 원한 예약 객체 리턴
    public int getUpdateOrCancel(Reservation reservationToUpdate){
        System.out.printf("선택한 예약: [ %s 트레이너/ %s ]\n해당 예약을 변경하고 싶다면 1번, 취소하고싶다면 2번을 입력하세요: ", reservationToUpdate.getManager().getName(),
                reservationToUpdate.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        print("\n    /\\_/\\\n" +
                "  /  • - • \\\n" +
                "/ づ \uD83C\uDF38づ  [ 번호 입력 ] => ");
        int choice = Integer.parseInt(readLine());// 사용자는 1부터 인덱싱을 시작하지만, 내부적으로는 0부터 시작하는 인덱스에 맞춰 조정
        System.out.println("--------------------------------------------");
        return choice;
    }
    public void displayUpdateRestrictionMessage() {
        printlnError("!!!!!!!!!!당일은 예약 변경이 불가합니다!!!!!!!!!!");
    }
    public void displayCancellationConfirmation(Reservation reservationToUpdate, Member member) {
        printSpecial("*********%s $s 트레이너 수업 예약이 취소되었습니다*********\n".formatted(
                reservationToUpdate.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                reservationToUpdate.getManager().getName()
        ));
        println(formatTitle("고객님의 남은 수업 횟수는 [ %d회 ] 입니다".formatted(member.getRemainSessionCount())));
    }

    public String requestMemberMenu() throws IllegalAccessException {
        print("\n\n\n");
        print("                MEMBER MENU                   \n");
        return requestMenuSelect(
                "수강권 결제",
                "수업 예약",
                "수업 변경 및 취소"
                //수업정보확인
        );
    }

}
