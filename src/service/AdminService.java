package service;
import model.*;
import repository.GroupPTRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class AdminService {
    private final GroupPTRepository groupPTRepository;
    private final TrainerService trainerService;


    public AdminService(GroupPTRepository groupPTRepository, TrainerService trainerService) {
        this.groupPTRepository = groupPTRepository;
        this.trainerService = trainerService;
    }

    // 1. 회원가입 신청 목록 O
    public List<User> getRegistrationRequests() {
        List<User> allUsers = groupPTRepository.findAllUsers();
        // NONMEMBER인 회원 필터링
        List<User> registrationRequests = allUsers.stream()
                .filter(user -> user.getRole() == User.Role.NONMEMBER)
                .collect(Collectors.toList());
        return registrationRequests;
    }

    //2. 회원 목록 보기 O
    public void getMemberList(){
        List<Member> members = groupPTRepository.findAllMembers();
        for (Member member : members) {
            System.out.print("이름: " + member.getName() + "\t");
            System.out.print("성별: " + member.getSex() + "\t");
            System.out.print("나이: " + member.getAge()+ "\t");
            System.out.print("아이디: " + member.getId()+ "\t");
            System.out.println("휴대폰 번호: " + member.getPhoneNumber());
            System.out.println("--------------------------------");
        }

    }

    // 회원 별 수업 스케줄 확인
//    [홍길동] 남자 30세 hong123 010-1111-2222
//      1회차 2024.02.01 출석 트레이너 김민지
//      2회차 2024.02.02 노쇼
//      3회차 2024.02.03 출석 트레이너 홍현석
//      4회차 2024.02.04 출석 트레이너 김민지
//      5회차 2024.02.05 예약 트레이너 최수민
//     총 결제 횟수 : 20회 / 남은 회수 : 16회 / 예약된 횟수 : 1회 / 노쇼 : 1회
    //user에서 reservation type이 class인 사람들의 수업 스케줄을 다음과 같이 출력
    public void viewMemberClassSchedule(String memberId) {
        User member = groupPTRepository.findUserById(memberId); //예약정보를 가져오기 위해 user에서 reservation이 class인 거 가져와야함
//        Member memberforpaymentsession = (Member) groupPTRepository.findAllMembers(); // 멤버의 총 수업 횟수를 가져오기 위해 필요
        Payment payment = groupPTRepository.findPaymentByPhoneNumber(member.getPhoneNumber());
        List<Reservation> reservations = groupPTRepository.findReservationsByPhone(member.getPhoneNumber());

//        List<Member> membersWithRemainingCountLessThanThree = new ArrayList<>();

        System.out.println("[" + member.getName() + "] " + member.getSex() + " " + member.getAge() + "세 " + member.getId() + " " + member.getPhoneNumber());

        int totalAttendanceCount = 0; //총 결제 횟수
        int noShowCount = groupPTRepository.countNoShow(member); // 노쇼 횟수 확인

        System.out.println("수업 스케줄:");
        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            System.out.print("  " + (i + 1) + "회차 " + reservation.getStartDate() + " ");
            if (reservation.isNoShowUser(member)) {
                System.out.println("노쇼");
            } else {
                System.out.println("출석 트레이너 " + reservation.getManager().getName());
                totalAttendanceCount++;
            }
        }
        int totalPaymentCount = payment.getPaymentOption().getSessions();
        int remainingCount = totalPaymentCount - totalAttendanceCount;
        int reservationCount = reservations.size();
//        if (remainingCount <= 3) {
//            membersWithRemainingCountLessThanThree.add((Member) member);
//        }
        System.out.println(" 총 결제 횟수 : " + totalPaymentCount + "회 / 남은 회수 : " + remainingCount + "회 / 예약된 횟수 : " + reservationCount + "회 / 노쇼 : " + noShowCount + "회");
    }

    //노쇼회원 확인 이름과 노쇼 횟수 출력
    public void viewNoShowMembers(){
        List<Reservation> allReservations = groupPTRepository.findAllReservations();
        List<String> noShowMembers = new ArrayList<>();

        // 노쇼인 경우 해당 회원의 이름을 리스트에 추가
        for (Reservation reservation : allReservations) {
            for (User user : reservation.getUsers()) {
                if (reservation.isNoShowUser(user)) {
                    noShowMembers.add(user.getName());
                }
            }
        }

        // 노쇼 회원 목록 출력
        if (!noShowMembers.isEmpty()) {
            System.out.println("노쇼 회원 목록:");
            for (String name : noShowMembers) {
                System.out.println(name);
            }
        } else {
            System.out.println("노쇼 회원이 없습니다.");
        }

    }

    // 수업 연장 마케팅 전송
    public void sendMarketingMessage() {
        List<Member> membersWithFewSessionsLeft = groupPTRepository.findAllMembers().stream()
                .filter(member -> {
                    Payment payment = groupPTRepository.findPaymentByPhoneNumber(member.getPhoneNumber());
                    int totalAttendanceCount = groupPTRepository.findReservationsByPhone(member.getPhoneNumber()).size();
                    int remainingCount = payment.getPaymentOption().getSessions() - totalAttendanceCount;
                    return remainingCount <= 3;
                })
                .collect(Collectors.toList());

        System.out.println("다음 회원들에게 수업 연장 마케팅 메세지를 전송합니다:");
        for (Member member : membersWithFewSessionsLeft) {
            System.out.println("이름: " + member.getName());
            System.out.println("성별: " + member.getSex());
            System.out.println("나이: " + member.getAge());
            System.out.println("아이디: " + member.getId());
            System.out.println("휴대폰 번호: " + member.getPhoneNumber());
            System.out.println("--------------------------------");
        }
    }


    //3. 비회원 목록 보기 O __ User에서 Reservation 의 Type이 Consult인 사람들을 불러와야함
    public void getNonMemberList(){
        List<User> allUsers = groupPTRepository.findAllUsers();

        // Consult 타입의 예약을 가진 사용자 필터링
        List<User> nonMembersWithConsultReservation = allUsers.stream()
                .filter(user -> groupPTRepository.findReservationsByPhone(user.getPhoneNumber()).stream()
                        .anyMatch(reservation -> reservation.getType() == Reservation.Type.CONSULT))
                        .collect(Collectors.toList());
        for (User user : allUsers) {
            System.out.print("이름: " + user.getName() + "\t");
            System.out.print("성별: " + user.getSex() + "\t");
            System.out.print("나이: " + user.getAge()+ "\t");
            System.out.print("아이디: " + user.getId()+ "\t");
            System.out.println("휴대폰 번호: " + user.getPhoneNumber());
            System.out.println("--------------------------------");
        }

//
//        return nonMembersWithConsultReservation;

    }

    //비회원 상담 스케쥴 확인
    public void viewNonMemberConsultSchedule(String memberId) {
        User nonMember = groupPTRepository.findUserById(memberId);
        List<Reservation> consultReservations = groupPTRepository.findReservationsByPhone(nonMember.getPhoneNumber())
                .stream()
                .filter(reservation -> reservation.getType() == Reservation.Type.CONSULT)
                .collect(Collectors.toList());

        System.out.println("[" + nonMember.getName() + "] " + nonMember.getPhoneNumber());
        System.out.println("상담 예약일:");

        for (Reservation reservation : consultReservations) {
            System.out.println("- 시작일: " + reservation.getStartDate());
        }
    }


    //4. 트레이너 목록
    public void getTrainerList(){
        List<Trainer> trainers = groupPTRepository.findAllTrainers();
        for (Trainer trainer : trainers) {
            System.out.print("트레이너 이름: " + trainer.getName() + "\t");
            System.out.print("트레이너 나이: " + trainer.getAge()+ "\t");
            System.out.print("트레이너 성별: " + trainer.getSex()+ "\t");
            System.out.print("트레이너 등급: " + trainer.getGrade()+ "\t");
            System.out.println("트레이너 휴대폰 번호: " + trainer.getPhoneNumber());

            System.out.println("-------------------------------------------------");
        }

    }

    //트레이너 수입 확인


    //5. 수업 스케줄 확인 __ 현재 시간 이후의 날짜를 가지고 있는 예약만 출력되고 날짜, 시간별로 로 오름차순으로 정렬

    public void getSchedule() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        List<Reservation> reservations = groupPTRepository.findAllReservations().stream()
                .filter(reservation -> reservation.getStartDate().isAfter(currentDateTime))
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());

        for (Reservation reservation : reservations) {
            System.out.print("날짜 / 시간 : " + reservation.getStartDate() + "\t");
            System.out.print("트레이너: " + reservation.getManager() + "\t");
            System.out.println("예약 인원 수: " + reservation.getUsers().size());
            System.out.println("-------------------------------------------------");
        }
    }


    //6. 매출 및 총 인건비 확인
    public void getRevenueAndLaborCost(int month) {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        // 해당 달에 결제한 모든 금액 계산
        int monthlyRevenue = calculateMonthlyRevenue(currentMonth, currentYear);

        // 총 인건비 계산
        int totalLaborCost = calculateTotalLaborCost(currentMonth, currentYear);

        // 현재까지 총 매출 계산
        int totalRevenue = calculateTotalRevenue();

        // 관리자의 순이익 계산
        int adminProfit = monthlyRevenue - totalLaborCost;

        // 결과 출력
        System.out.println("[한달 총 매출]: " + monthlyRevenue);
        System.out.println("[관리자의 이익]: " + adminProfit);
        System.out.println("[현재까지 총 매출]: " + totalRevenue);
    }

    private int calculateMonthlyRevenue(int month, int year) {
        List<Payment> payments = groupPTRepository.findAllPayments();
        int monthlyRevenue = 0;
        for (Payment payment : payments) {
            if (payment.getPaymentTime().getMonthValue() == month && payment.getPaymentTime().getYear() == year) {
                monthlyRevenue += payment.getPaymentOption().getPrice();
            }
        }
        return monthlyRevenue;
    }

    private int calculateTotalLaborCost(int month, int year) {
        // 트레이너별 인건비 총합
        Map<Trainer, Integer> incomeByTrainer = trainerService.calculateIncomeByTrainer(month);

        // 모든 트레이너의 수입을 더함
        int totalRevenue = incomeByTrainer.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        return totalRevenue;
    }

    private int calculateTotalRevenue() {
        List<Payment> payments = groupPTRepository.findAllPayments();
        int totalRevenue = 0;
        for (Payment payment : payments) {
            totalRevenue += payment.getPaymentOption().getPrice();
        }
        return totalRevenue;
    }

}
