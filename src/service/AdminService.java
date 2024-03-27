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
    private final GroupPTRepository groupPTRepository = GroupPTRepository.getInstance();
    private final TrainerService trainerService;
    //private List<User> registrationRequests;

    public AdminService(TrainerService trainerService) {
        this.trainerService = trainerService;
        //this.registrationRequests = getRegistrationRequests();
    }

    // 1. 회원가입 신청 목록
    public List<User> getRegistrationRequests() {
        List<User> allUsers = groupPTRepository.findAllUsers();
        // NONMEMBER인 회원 필터링
        return allUsers.stream()
                .filter(user -> user.getState() == User.State.PENDING)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //관리자가 승인한 목록들의 role nonmember에서 member로 바꿔주기
    public void approveUsers(List<User> users) {
        for (User user : users) {
            user.setState(User.State.APPROVED);

            if (user instanceof Trainer) {
                groupPTRepository.updateTrainer((Trainer) user);
            } else {
                groupPTRepository.updateMember((Member) user);
            }
        }
    }

    //2. 회원 목록 보기 O
    public List<Member> getMemberList() {
        return groupPTRepository.findAllMembers();
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
    public List<Reservation> getMemberClassSchedule(Member member) {
        return groupPTRepository.findReservationsByPhone(member.getPhoneNumber());
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
    public List<Member> getNoShowMembers() {
        List<List<Member>> noShowMemberLists = groupPTRepository.findAllReservations().stream()
                .filter(r -> !r.getUsers().equals(r.getAttendants()))
                .filter(r -> r.getType().equals(Reservation.Type.CLASS))
                .map(r ->
                        r.getUsers().stream()
                                .filter(u -> !r.getAttendants().contains(u))
                                .map(u -> (Member) u).toList())
                .toList();

        List<Member> noShowMembers = new ArrayList<>();
        for (List<Member> list : noShowMemberLists) {
            noShowMembers.addAll(list);
        }

        return noShowMembers;

//        // 노쇼 회원 목록 출력
//        adminView.printNoShowMembers(noShowMembers);
////        if (!noShowMembers.isEmpty()) {
////            System.out.println("노쇼 회원 목록:");
////            for (String name : noShowMembers) {
////                System.out.println(name);
////            }
////        } else {
////            System.out.println("노쇼 회원이 없습니다.\n");
////        }
    }

    // 수업 연장 마케팅 전송
    public void sendMarketingMessage() {
        List<Member> membersWithFewSessionsLeft = groupPTRepository.findAllMembers().stream()
                .filter(member -> member.getRole().equals(User.Role.MEMBER))
                .filter(member -> {
                    int remainingCount = member.getRemainSessionCount();
                    return remainingCount <= 3;
                })
                .toList();
//        adminView.sendMarketingMessagesToMembers(membersWithFewSessionsLeft);

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
    public List<User> getNonMemberList() {
        List<User> allUsers = groupPTRepository.findAllUsers();

        // Consult 타입의 예약을 가진 사용자 필터링
        List<User> nonMembersWithConsultReservation = groupPTRepository.findAllReservations().stream()
                .filter(r -> r.getType().equals(Reservation.Type.CONSULT))
                .map(r -> r.getUsers().get(0))
                .toList();

        return nonMembersWithConsultReservation;
    }


    //비회원 상담 스케쥴 확인
    public List<Reservation> getConsultReservation(User user) {
        return groupPTRepository.findAllReservations().stream()
                .filter(r -> r.getType().equals(Reservation.Type.CONSULT))
                .filter(r -> r.getUsers().contains(user))
                .collect(Collectors.toCollection(ArrayList::new));
    }


    //4. 트레이너 목록
    // 트레이너 목록 출력
    public List<Trainer> getTrainerList() {
        return groupPTRepository.findAllTrainers();
    }


    //트레이너 수입 확인
    public List<String> getTrainerDetails(Trainer trainer, int year) {
        List<String> res = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            int monthIncome = trainerService.calculateIncome(trainer, month, year);
            res.add(year + "년 " + month + "월  " + monthIncome + "원");
        }

        return res;
    }


    //5. 수업 스케줄 확인 __ 현재 시간 이후의 날짜를 가지고 있는 예약만 출력되고 날짜, 시간별로 로 오름차순으로 정렬
    public List<Reservation> getSchedule() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        return groupPTRepository.findAllReservations().stream()
                .filter(reservation -> reservation.getStartDate().isAfter(currentDateTime))
                .sorted(Comparator.comparing(Reservation::getStartDate))
                .collect(Collectors.toList());

//        if (reservations.isEmpty()) {
//            adminView.printNoClassSchedule();
//        } else {
//            for (Reservation reservation : reservations) {
//                adminView.printReservationDetails(reservation);
////                System.out.print("날짜 / 시간 : " + reservation.getStartDate() + "\t");
////                System.out.print("트레이너: " + reservation.getManager() + "\t");
////                System.out.println("예약 인원 수: " + reservation.getUsers().size());
////                System.out.println("-------------------------------------------------");
//            }
//        }
    }


    //6. 매출 및 총 인건비 확인
    public int[] getRevenueAndLaborCost() {
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

        return new int[]{monthlyRevenue, totalLaborCost, totalRevenue};
    }

    public int calculateTotalLaborCost(List<Trainer> trainers, int month, int year) {
        int totalLaborCost = 0;

        // 각 트레이너의 월별 인건비 합
        for (Trainer trainer : trainers) {
            totalLaborCost += calculateMonthlyLaborCost(trainer, month, year);
        }

        return totalLaborCost;
    }

    public int calculateMonthlyLaborCost(Trainer trainer, int month, int year) {
        return trainerService.calculateIncome(trainer, month, year);
    }


    public int calculateMonthlyRevenue(int month, int year) {
        List<Payment> payments = groupPTRepository.findAllPayments();
        int monthlyRevenue = 0;
        for (Payment payment : payments) {
            if (payment.getPaymentTime().getMonthValue() == month && payment.getPaymentTime().getYear() == year) {
                monthlyRevenue += payment.getPaymentOption().getPrice();
            }
        }
        return monthlyRevenue;
    }

    public int calculateTotalRevenue() {
        List<Payment> payments = groupPTRepository.findAllPayments();
        int totalRevenue = 0;
        for (Payment payment : payments) {
            totalRevenue += payment.getPaymentOption().getPrice();
        }
        return totalRevenue;
    }
}

