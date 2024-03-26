package service;

import model.*;
import repository.GroupPTRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class AdminService {
    private final GroupPTRepository groupPTRepository;
    private final TrainerService trainerService;
    private List<User> registrationRequests;



    public AdminService(GroupPTRepository groupPTRepository, TrainerService trainerService) {
        this.groupPTRepository = groupPTRepository;
        this.trainerService = trainerService;
        this.registrationRequests = getRegistrationRequests();
    }

    // 1. 회원가입 신청 목록
    public List<User> getRegistrationRequests() {
        List<User> allUsers = groupPTRepository.findAllUsers();
        // NONMEMBER인 회원 필터링
        List<User> registrationRequests = allUsers.stream()
                .filter(user -> user.getRole() == User.Role.NONMEMBER)
                .collect(Collectors.toList());

        return registrationRequests;
    }

    //관리자가 승인한 목록들의 role nonmember에서 member로 바꿔주기
    public void approveMember(int index) {
        // 선택한 인덱스의 회원의 ROLE을 MEMBER로 변경
        if (index >= 1 && index <= registrationRequests.size()) {
            User user = registrationRequests.get(index - 1);
            user.setRole(User.Role.MEMBER);

            if (user.getRole().equals(User.Role.TRAINER)) {
                groupPTRepository.updateTrainer((Trainer) user);
            } else {
                groupPTRepository.updateMember((Member) user);
            }

            System.out.println(user.getName() + "님이 승인되었습니다.");
        } else {
            System.out.println("유효하지 않은 인덱스입니다.");
        }
    }

    //2. 회원 목록 보기 O
    public void getMemberList(){
        List<Member> members = groupPTRepository.findAllMembers();
        System.out.println("");
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            if (member.getRole() == User.Role.MEMBER) { // MEMBER인 경우에만 출력
                int index = i + 1;
                System.out.println("-----------------------------------------------------");
                System.out.print(index + "\t");
                System.out.print("이름: " + member.getName() + "\t");
                System.out.print("성별: " + member.getSex() + "\t");
                System.out.print("나이: " + member.getAge()+ "\t");
                System.out.print("아이디: " + member.getId()+ "\t");
                System.out.println("휴대폰 번호: " + member.getPhoneNumber());
                System.out.println("-----------------------------------------------------");
            }
        }
        System.out.println("");
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
        // 숫자인지 확인하고 숫자로 변환하거나 그대로 사용
        try {
            int memberIndex = Integer.parseInt(memberId);
            List<Member> members = groupPTRepository.findAllMembers();
            if (memberIndex < 1 || memberIndex > members.size()) {
                System.out.println("잘못된 인덱스입니다.");
                return;
            }
            Member selectedMember = members.get(memberIndex - 1);
            // 회원 정보 출력
            printMemberInfo(selectedMember);
            // 회원의 예약 정보
            List<Reservation> reservations = groupPTRepository.findReservationsByPhone(selectedMember.getPhoneNumber());
            // 결제 정보 가져오기
            Payment payment = groupPTRepository.findPaymentByPhoneNumber(selectedMember.getPhoneNumber());
            // 회원의 수업 스케줄 출력
            printMemberClassSchedule(selectedMember, reservations, payment);
        } catch (NumberFormatException e) {
            if (memberId.equals("@")) {
                viewNoShowMembers();
            } else if (memberId.equals("!")) {
                sendMarketingMessage();
            } else {
                System.out.println("잘못된 입력입니다.");
            }
        }
    }

    private void printMemberInfo(Member member) {
        System.out.println("[" + member.getName() + "] " + member.getSex() + " " + member.getAge() + "세 " + member.getId() + " " + member.getPhoneNumber());
    }

    private void printMemberClassSchedule(Member member, List<Reservation> reservations, Payment payment) {
        int totalAttendanceCount = 0;
        int noShowCount = groupPTRepository.countNoShow(member);

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

        System.out.println(" 총 결제 횟수 : " + totalPaymentCount + "회 / 남은 회수 : " + remainingCount + "회 / 예약된 횟수 : " + reservationCount + "회 / 노쇼 : " + noShowCount + "회");
    }

    // 노쇼 회원 확인 이름과 노쇼 횟수 출력
    public void viewNoShowMembers() {
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
            System.out.println("노쇼 회원이 없습니다.\n");
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
    public List<User> getNonMemberList(){
        List<User> allUsers = groupPTRepository.findAllUsers();

        // Consult 타입의 예약을 가진 사용자 필터링
        List<User> nonMembersWithConsultReservation = allUsers.stream()
                .filter(user -> groupPTRepository.findReservationsByPhone(user.getPhoneNumber()).stream()
                        .anyMatch(reservation -> reservation.getType() == Reservation.Type.CONSULT))
                .collect(Collectors.toList());

        return nonMembersWithConsultReservation;
    }

    public void printNonMemberList(List<User> nonMemberList) {
        if (!nonMemberList.isEmpty()) {
            for (int i = 0; i < nonMemberList.size(); i++) {
                User user = nonMemberList.get(i);
                int index = i + 1;
                System.out.print("인덱스: " + index + "\t");
                System.out.print("이름: " + user.getName() + "\t");
                System.out.println("휴대폰 번호: " + user.getPhoneNumber());
                System.out.println("--------------------------------");
            }
        }
    }



    //비회원 상담 스케쥴 확인
    public void viewConsultReservation(int memberIndex) {
        List<User> allUsers = groupPTRepository.findAllUsers();

        if (allUsers.isEmpty()) {
            System.out.println("상담 예약 정보를 확인할 비회원이 없습니다.");
            return;
        }

        if (memberIndex < 1 || memberIndex > allUsers.size()) {
            System.out.println("잘못된 인덱스입니다.");
            return;
        }
        String phone = allUsers.get(memberIndex - 1).getPhoneNumber();
        User nonMember = groupPTRepository.findUserByPhone(phone);
        List<Reservation> consultReservations = groupPTRepository.findReservationsByPhone(nonMember.getPhoneNumber())
                .stream()
                .filter(reservation -> reservation.getType() == Reservation.Type.CONSULT)
                .collect(Collectors.toList());

        // 상담 예약 정보 출력
        System.out.println("[" + nonMember.getName() + "] " + nonMember.getPhoneNumber());


        for (Reservation reservation : consultReservations) {
            System.out.println("상담 예약일 : " + reservation.getStartDate());
        }
    }



    //4. 트레이너 목록
    // 트레이너 목록 출력
    public void getTrainerList(){
        List<Trainer> trainers = groupPTRepository.findAllTrainers();
        System.out.println();
        for (int i = 0; i < trainers.size(); i++) {
            Trainer trainer = trainers.get(i);
            int index = i + 1;
            System.out.println("-------------------------------------------------------------------------------------------");
            System.out.print(index + "\t");
            System.out.print("트레이너 이름: " + trainer.getName() + "\t");
            System.out.print("트레이너 나이: " + trainer.getAge()+ "\t");
            System.out.print("트레이너 성별: " + trainer.getSex()+ "\t");
            System.out.print("트레이너 등급: " + trainer.getGrade()+ "\t");
            System.out.println("트레이너 휴대폰 번호: " + trainer.getPhoneNumber());
            System.out.println("-------------------------------------------------------------------------------------------");
        }
    }


    //트레이너 수입 확인
    public void getTrainerDetails(int trainerIndex) {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        List<Trainer> trainers = groupPTRepository.findAllTrainers();


        if (trainerIndex >= 1 && trainerIndex <= trainers.size()) {
            Trainer trainer = trainers.get(trainerIndex - 1);
            System.out.println("-------------------------------------------------------------------------------------------");
            System.out.println("[트레이너 " + trainer.getName() + "의 수입 기록]");

            for (int year = 2023; year <= currentYear; year++) {
                int startMonth = (year == 2023) ? 12 : 1;
                int endMonth = (year == currentYear) ? currentMonth : 12;
                for (int month = startMonth; month <= endMonth; month++) {
                    int monthlyIncome = trainerService.calculateIncome(trainer, month, year);
                    System.out.println(year + "년 " + month + "월  " + monthlyIncome + "원");
                }
            }
            System.out.println("");
        }
    }


    //5. 수업 스케줄 확인 __ 현재 시간 이후의 날짜를 가지고 있는 예약만 출력되고 날짜, 시간별로 로 오름차순으로 정렬
    public void getSchedule() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        List<Reservation> reservations = groupPTRepository.findAllReservations().stream()
                .filter(reservation -> reservation.getStartDate().isAfter(currentDateTime))
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());

        if (reservations.isEmpty()) {
            System.out.println("예정된 수업이 없습니다.\n");
        } else {
            for (Reservation reservation : reservations) {
                System.out.print("날짜 / 시간 : " + reservation.getStartDate() + "\t");
                System.out.print("트레이너: " + reservation.getManager() + "\t");
                System.out.println("예약 인원 수: " + reservation.getUsers().size());
                System.out.println("-------------------------------------------------");
            }
        }
    }


    //6. 매출 및 총 인건비 확인
    public void getRevenueAndLaborCost() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        // 모든 트레이너 가져오기
        List<Trainer> trainers = groupPTRepository.findAllTrainers();

        // 해당 달에 결제한 모든 금액
        int monthlyRevenue = calculateMonthlyRevenue(currentMonth, currentYear);

        // 총 인건비
        int totalLaborCost = calculateTotalLaborCost(trainers, currentMonth, currentYear);

        // 현재까지 총 매출
        int totalRevenue = calculateTotalRevenue();

        // 결과 출력
        System.out.println("");
        System.out.println("--------------------------------");
        System.out.printf("[한달 총 매출]:\t%,d원%n", monthlyRevenue);
        System.out.printf("[총 인건비]:\t\t%,d원%n", totalLaborCost);
        System.out.printf("[총 매출]:\t\t%,d원%n", totalRevenue);
        System.out.println("--------------------------------");
        System.out.println("");

    }

    private int calculateTotalLaborCost(List<Trainer> trainers, int month, int year) {
        int totalLaborCost = 0;

        // 각 트레이너의 월별 인건비 합
        for (Trainer trainer : trainers) {
            totalLaborCost += calculateMonthlyLaborCost(trainer, month, year);
        }

        return totalLaborCost;
    }

    private int calculateMonthlyLaborCost(Trainer trainer, int month, int year) {
        return trainerService.calculateIncome(trainer, month, year);
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

    private int calculateTotalRevenue() {
        List<Payment> payments = groupPTRepository.findAllPayments();
        int totalRevenue = 0;
        for (Payment payment : payments) {
            totalRevenue += payment.getPaymentOption().getPrice();
        }
        return totalRevenue;
    }
}

