package model;

import repository.GroupPTRepository;
import util.Utils;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Member extends User {
    transient Scanner sc = new Scanner(System.in);
    transient GroupPTRepository repository = GroupPTRepository.getInstance();
    int remainSessionCount;//남은수업횟수 //유효날짜가 지나면 member의 remain횟수를 0으로 만들기
    Payment payment;//결제 객체
    LocalDate paymentTime;
    Payment.PaymentOption selectedOption;

    public Member(User user) {//회원가입신청할 때 생성
        super(user.getName(), user.getPhoneNumber(), user.getAge(), user.getSex(), user.getId(), user.getPw(), user.getRole());
        this.remainSessionCount = 0; // 예시로 0으로 설정
        this.payment = null; // 초기값으로 null 설정
        this.paymentTime = null; // 현재 날짜로 초기화, 필요에 따라 다른 값을 사용할 수 있음
        this.selectedOption = null; // 초기값으로 null 설정
    }
    //수업결제
    public void payForClass(){
        System.out.println("결제 단위를 선택해주세요");
        System.out.println("1. 10회 - 90일 - 총가격 ₩700,000");
        System.out.println("2. 20회 - 120일 - 총가격 ₩1,200,000");
        System.out.println("3. 30회 - 180일 - 회당 ₩1,500,000");
        System.out.print("번호 입력: ");
        int choice = sc.nextInt();

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
                System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
                return; // 메소드 종료
        }
        this.payment = new Payment(LocalDate.now(), this.getPhoneNumber(),this.selectedOption);
        System.out.println(choice+"번 옵션, "+selectedOption.getPrice()+"원 결제가 완료되었습니다");
        this.remainSessionCount += selectedOption.getSessions();
        //payment 객체를 "결제" 파일에 저장하는 작업
        repository.savePayment(this.payment);
        //회원 파일도 업데이트
        //updateMember
        //노쇼 1회 시 재등록(결제) 불가능하다
    }
    //수업예약
    public void reserveClass(){
        //남은횟수 있는지 확인 후 => 있으면
        //1. 트레이너 선택메뉴 => 선택
        //2. 해당 트레이너의 수업 예약 가능한 시간 목록 출력 => 선택
        //3. 예약 확정 => 예약확정 메세지 출력
        if(this.remainSessionCount==0){
            System.out.println("남은 횟수가 없습니다. 재결제가 필요합니다");
            return;
        }
        //1. 트레이너 선택 메뉴(이름, 등급, 성별)
        List<Trainer> allTrainersList = repository.findAllTrainers();
        System.out.println("예약을 원하는 트레이너의 번호를 입력해주세요");
        for(int i=0; i<allTrainersList.size();i++) {
            Trainer trainer = allTrainersList.get(i);
            System.out.printf("%d. %s 트레이너/ 등급 %s/ %s\n", i+1, trainer.getName(), trainer.getGrade()
                    , trainer.getSex());
        }
        System.out.print("번호 입력: ");
        int choice = sc.nextInt();

        //+) 유효한 번호가 아니면 메뉴 다시 보여주기 구현해야함
        Trainer selectedTrainer = allTrainersList.get(choice-1);

        //2. 선택한 트레이너의 예약 목록 출력
        List<Reservation> reservationOfSelectedTrainer = repository.findReservationsByTrainer(selectedTrainer);
        // users 리스트의 크기가 4개인 예약들만 필터링하여 새로운 리스트를 생성 filteredReservations
        List<Reservation> filteredReservations = reservationOfSelectedTrainer.stream()
                .filter(reservation -> reservation.getUsers().size() == 4)
                .toList();

        // 트레이너의 근무 요일 배열을 DayOfWeek 타입의 리스트로 변환
        List<Utils.Day> workDays = selectedTrainer.getLessonDays();

        // 예약 가능한 시간대 목록 생성
        LocalDate today = LocalDate.now();
        LocalDate endDay = today.plusWeeks(1); //일주일 후
        LocalTime startTime = LocalTime.of(13,0);
        LocalTime endTime = LocalTime.of(19,0);

        //idx(1~), LocalDateTime 예약한시간
        Map<Integer, LocalDateTime> slotIndexToDateTimeMap = new HashMap<>();
        int slotIndex = 1;

        for (LocalDate date = today.plusDays(1); !date.isAfter(endDay); date = date.plusDays(1)) {

            if (workDays.contains(Utils.getDay(date.atStartOfDay()))) {
                System.out.println(date + "의 예약 가능한 시간:");
                System.out.println("--------------------------------------------");
                //해당 시간: tempStartTime
                LocalTime tempStartTime = startTime;
                while (tempStartTime.isBefore(endTime)) {
                    LocalDateTime slotStartDateTime = LocalDateTime.of(date, tempStartTime);
                    LocalDateTime slotEndDateTime = slotStartDateTime.plusHours(1);

                    boolean isReserved = filteredReservations.stream().anyMatch(reservation ->
                            reservation.getStartDate().equals(slotStartDateTime));

                    if (!isReserved) {
                        System.out.println(slotIndex + ". " + slotStartDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd, E"))
                                + " - " + slotEndDateTime.format(DateTimeFormatter.ofPattern("HH:mm")));
                        slotIndexToDateTimeMap.put(slotIndex, slotStartDateTime);
                        slotIndex++;
                    }

                    tempStartTime = tempStartTime.plusHours(1);
                }
            }
        }

        //3. 사용자로부터 예약할 시간대 선택 받기
        System.out.println("예약할 시간의 번호를 입력해주세요:");
        int selectedSlotIndex = sc.nextInt();
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
            if (existingReservation != null) { //해당시간에 예약한 사람이 0명인 경우
                existingReservation.addUser(this);
                repository.updateReservation(existingReservation);
            } else {
                Reservation newReservation = new Reservation(selectedTrainer, selectedDateTime);
                newReservation.addUser(this);
                repository.saveReservation(newReservation);
            }
            //횟수차감
            remainSessionCount-=1;
            System.out.println("예약이 완료되었습니다. 예약일자: " + selectedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))+"\n");
        } else {
            System.out.println("잘못된 번호입니다. 다시 입력해주세요.");
        }
    }//수업예약함수 END


    //-----



        /*
        1. 원하는 시간이 없으면 ‘0’번을 눌러 다시 트레이너 목록이 보여진다.
        2. ‘*’을 누르면 메인화면으로 돌아간다.
        3. 선택지에 없는 메뉴를 선택할 경우 메뉴를 다시 출력한다.
         */


    public static void main(String[] args) {
        User user1 = new User();
        Member member1 = new Member(user1);
        member1.payForClass();
        member1.reserveClass();
    }
    //수업정보확인
    // 강사/ 시간/ 출석여부
    // 강사/ 시간/ 예약인원
    //잔여 수업횟수/ 잔여 수업 사용 가능 일수
    // 예약한 수업을 변경 혹은 취소하고 싶다면 해당 번호를 입력하세요
    // 강사/ 시간 >> 해당 예약 변경은 1번, 해당 예약 취소는 2번을 입력하세요
    public Reservation displayReservationInfo() {
        List<Reservation> reservationsByPhone = repository.findReservationsByPhone(this.getPhoneNumber());
        // 예약된 수업 목록 출력
        for (int i = 0; i < reservationsByPhone.size(); i++) {
            //예약된 수업 목록 출력 양식: idx. 강사/ 시간/ 정원
            //System.out.println((i+1) + ": " + reservationsByPhone.get(i).toString());
            Reservation reservation = reservationsByPhone.get(i);
            System.out.printf("%d. %s 트레이너/ %s/ 예약인원 %d명\n", i + 1, reservation.getManager().getName(),
                    reservation.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    reservation.getUsers().size());
        }
        //잔여 수업횟수/ 잔여 수업 사용 가능 일수 출력
        System.out.printf("고객님의 잔여 횟수는 %d회, 잔여 수업 사용가능일수는 %d일 입니다\n", this.remainSessionCount, this.calculateDaysRemaining());
        System.out.println("--------------------------------------------------");
        // 사용자로부터 취소할 예약의 인덱스를 입력받음 (1부터 시작하는 인덱스)
        System.out.print("예약한 수업을 변경 혹은 취소하고 싶다면 해당 예약의 인덱스를 입력하세요 : ");
        int index = sc.nextInt() - 1; // 사용자는 1부터 인덱싱을 시작하지만, 내부적으로는 0부터 시작하는 인덱스에 맞춰 조정
        System.out.println("--------------------------------------------------");
        Reservation reservationToUpdate = reservationsByPhone.get(index);
        System.out.printf("선택한 예약: [ %s 트레이너/ %s ]\n해당 예약을 변경하고 싶다면 1번, 취소하고싶다면 2번을 입력하세요: ", reservationToUpdate.getManager().getName(),
                reservationToUpdate.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        //return sc.nextInt();
        //변경을 원한 예약 객체 리턴
        return reservationToUpdate;
    }
    //잔여수업사용 가능 일수 구하기
    public int calculateDaysRemaining(){
        if(this.payment!=null){
            LocalDate paymentdate = this.payment.getPaymentTime();
            int validDays = this.payment.getPaymentOption().getValidDays();
            LocalDate expirationDate = paymentdate.plusDays(validDays);
            LocalDate today = LocalDate.now();

            Period period = Period.between(today, expirationDate);
            return period.getDays() + period.getMonths()*30;
            //+period.getYears()*365
        }
        return 0; //결제 정보가 없는 경우
    }

    //displayReservationInfo() == 1 이라면
    //수업예약 변경
    //당일예약변경 불가능하다
    public void updateClassReservation(Reservation reservationToUpdate){
        LocalDate today = LocalDate.now();
        // 당일은 변경 가능한 목록이 아니라고 출력
        // 남은 세션 수 복구
        if (reservationToUpdate.getStartDate().toLocalDate().equals(today)) {
            System.out.println("당일은 예약 변경이 불가합니다");
            return;
        }
        this.remainSessionCount += 1;
        repository.deleteReservation(reservationToUpdate);
        this.reserveClass();
    }



    //당일취소할 경우, 취소는 되지만 횟수가 차감된다
    //displayReservationInfo() == 2 라면
    //수업예약취소
    public void cancelClassReservation(Reservation reservationToUpdate){
        // 유효한 인덱스 범위 검사
        //if (index >= 0 && index < reservationsByPhone.size())
            LocalDate today = LocalDate.now();
            //당일 취소 제외) 남은 세션 수 복구
            if (reservationToUpdate.getStartDate().toLocalDate().isAfter(today)) {
                this.remainSessionCount += 1;
            }
            repository.deleteReservation(reservationToUpdate);
            System.out.printf("%s $s 트레이너 수업 예약이 취소되었습니다\n",reservationToUpdate.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    , reservationToUpdate.getManager().getName());
            System.out.printf("고객님의 남은 수업 횟수는 %d회 입니다" , this.remainSessionCount);
        }
        //모든예약찾기
        public List<Reservation> findAllReservations(){
         return repository.findAllReservations();
        }


}//Member class END
