package controller;

import model.Member;
import model.Reservation;
import model.Trainer;
import model.User;
import view.AdminView;

import java.util.List;

import service.AdminService;

public class AdminController {
    private final AdminView adminView;
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
        this.adminView = new AdminView();
    }

    public void runAdmin() throws IllegalAccessException {
        switch (adminView.requestAdminMenu()) {
            //(회원가입 신청 목록)을 선택했을 경우
            case "1" -> handleApproveUsers();
            //(회원 목록 보기)을 선택했을 경우
            case "2" -> handleAllMembers();
            // 비회원 목록 가져오기
            case "3" -> handleAllNonMembers();
            //(트레이너 목록)을 선택했을 경우
            case "4" -> handleAllTrainers();
            //(수업 스케줄 확인)을 선택했을 경우
            case "5" -> adminView.printReservations(adminService.getSchedule());
            //(매출 및 인건비 확인)을 선택했을 경우
            case "6" -> handleIncome();
            case "7" -> adminView.printNoShowMembers(adminService.getNoShowMembers());
            case "8" -> adminService.sendMarketingMessage();
        }
    }

    private void handleApproveUsers() {
        List<User> registrationRequests = adminService.getRegistrationRequests();
        if (registrationRequests.isEmpty()) {
            adminView.showNoPendingApprovalsMessage();
            return;
        }
        adminView.printRegistrationRequests(registrationRequests);

        //인덱스입력 받기 -> , 로 구분해서 입력한 이름 User의 Role MEMBER로 바꿔주기
        List<User> approvedUsers = adminView.readLineBySeparate(",").stream()
                .mapToInt(idx -> Integer.parseInt(idx) - 1)
                .mapToObj(registrationRequests::get)
                .toList();

        adminService.approveUsers(approvedUsers);
        adminView.printSpecial(approvedUsers.size() + "명 승인 완료\n");
    }

    private void handleAllMembers() {
        List<Member> members = adminService.getMemberList();
        //유저 목록 보여주고
        adminView.printMembers(members);

        //유저 인덱스 번호 입력하면 유저의 수업 스케줄 확인
        adminView.printRequestInput("확인할 회원 번호");
        Member member = members.get(adminView.readInt() - 1);

        List<Reservation> reservations = adminService.getMemberClassSchedule(member);

        adminView.printMemberDetailInfo(member);
        adminView.printMemberClassSchedule(member, reservations);
    }

    private void handleAllNonMembers() {
        List<User> nonMemberList = adminService.getNonMemberList();

        // 비회원 목록이 비어 있는지 확인
        if (nonMemberList.isEmpty()) {
            adminView.printNoNonMemberReservationInfoMessage();
            return;
        }

        // 비회원 목록 출력
        adminView.printNonMemberList(nonMemberList);

        // 입력받은 인덱스에 해당하는 비회원의 상담 예약 정보 확인
        adminView.printRequestInput("예약 정보 확인할 번호");
        int nonmemberIndex = adminView.readInt();
        User user = nonMemberList.get(nonmemberIndex - 1);

        List<Reservation> consults = adminService.getConsultReservation(user);
        adminView.printConsultReservationInfo(user, consults);
    }

    private void handleAllTrainers() {
        List<Trainer> trainers = adminService.getTrainerList();
        adminView.printTrainers(trainers);

        // 입력받은 인덱스에 해당하는 트레이너의 상세 정보 확인
        int trainerIndex = adminView.readInt() - 1;
        Trainer trainer = trainers.get(trainerIndex);

        adminView.printRequestInput(trainer.getName() + "님의 수입 정보 확인할 연도");
        int year = adminView.readInt();

        adminView.print(adminView.formatTitle(trainer.getName() + "님의 정보"));
        List<String> details = adminService.getTrainerDetails(trainers.get(trainerIndex), year);
        adminView.printTrainerDetails(trainer, details);
    }

    private void handleIncome() {
        int[] income = adminService.getRevenueAndLaborCost();
        adminView.printFinancialSummary(income[0], income[1], income[2]);
    }
}
