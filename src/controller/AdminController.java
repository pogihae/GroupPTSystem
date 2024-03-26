package controller;

import model.Member;
import model.Reservation;
import model.Trainer;
import model.User;
import view.AdminView;

import java.time.LocalDate;
import java.util.List;

import service.AdminService;

public class AdminController {
    private AdminView adminView;
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
        this.adminView = new AdminView();
    }

    public void runAdmin() {
        boolean isRunning = true;
        while (isRunning) {
            adminView.adminMenu();
            String mainchoice = adminView.readLine();
            switch (mainchoice) {
                case "1":
                    //(회원가입 신청 목록)을 선택했을 경우
                    List<User> registrationRequests = adminService.getRegistrationRequests();
                    if (registrationRequests.isEmpty()) {
                        adminView.showNoPendingApprovalsMessage();
                        break;
                    }
                    adminView.printRegistrationRequests(registrationRequests);

                    //인덱스입력 받기 -> , 로 구분해서 입력한 이름 User의 Role MEMBER로 바꿔주기
//                    System.out.println("승인할 회원의 인덱스를 입력하세요. 여러 명을 선택할 경우 쉼표(,)로 구분하세요:");
//                    String inputIndices = adminView.readLine();
                    List<User> approvedUsers = adminView.readLineBySeparate(",").stream()
                            .mapToInt(idx -> Integer.parseInt(idx) - 1)
                            .mapToObj(registrationRequests::get)
                            .toList();

                    adminService.approveUsers(approvedUsers);
                    adminView.print(approvedUsers.size() + "명 승인\n");
                    break;

                case "2":
                    //(회원 목록 보기)을 선택했을 경우
                    List<Member> members = adminService.getMemberList();
                    //유저 목록 보여주고
                    adminView.printMembers(members);

                    //유저 인덱스 번호 입력하면 유저의 수업 스케줄 확인
                    adminView.print("확인할 회원: ");
                    Member member = members.get(adminView.readInt());

                    List<Reservation> reservations = adminService.getMemberClassSchedule(member);

                    adminView.printMemberDetailInfo(member);
                    adminView.printMemberClassSchedule(member, reservations);

                    break;

                case "3":
                    // 비회원 목록 가져오기
                    List<User> nonMemberList = adminService.getNonMemberList();

                    // 비회원 목록이 비어 있는지 확인
                    if (nonMemberList.isEmpty()) {
                        adminView.printNoNonMemberReservationInfoMessage();
                        break;
                    }

                    // 비회원 목록 출력
                    adminView.printNonMemberList(nonMemberList);

                    // 입력받은 인덱스에 해당하는 비회원의 상담 예약 정보 확인
                    adminView.print("상담 예약 정보를 확인: ");
                    int nonmemberIndex = adminView.readInt();
                    User user = nonMemberList.get(nonmemberIndex - 1);

                    List<Reservation> consults = adminService.getConsultReservation(user);
                    adminView.printConsultReservationInfo(user, consults);

                    break;

                case "4":
                    //(트레이너 목록)을 선택했을 경우
                    List<Trainer> trainers = adminService.getTrainerList();
                    adminView.viewTrainerList(trainers);

                    // 입력받은 인덱스에 해당하는 트레이너의 상세 정보 확인
                    int trainerIndex = adminView.readInt();
                    Trainer trainer = trainers.get(trainerIndex);

                    adminView.print(trainer.getName() + "님의 정보 확인할 연도: ");
                    int year = adminView.readInt();

                    adminView.print(trainer.getName() + "님의 정보\n");
                    adminService.getTrainerDetails(trainers.get(trainerIndex), year);

                    break;

                case "5":
                    //(수업 스케줄 확인)을 선택했을 경우
                    adminView.printReservations(adminService.getSchedule());
                    break;
                case "6":
                    //(매출 및 인건비 확인)을 선택했을 경우
                    adminService.getRevenueAndLaborCost();
                    break;
                case "*": //로그인 화면으로 돌아가기
                    isRunning = false;
                    break;
                case "#": //시스템 종료하기
                    System.exit(0);
                    break;
                default:
                    adminView.printInvalidMessage();
            }
        }
    }
}
