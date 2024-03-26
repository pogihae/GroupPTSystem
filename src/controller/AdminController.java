package controller;

import model.Member;
import repository.GroupPTRepository;
import view.AdminView;

import java.util.List;
import java.util.Scanner;
import service.AdminService;

public class AdminController {
    private AdminView adminView;
    private Scanner scanner;

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
        this.adminView = new AdminView();
        this.scanner = new Scanner(System.in);
    }

    public void runAdmin() {
        boolean isRunning = true;
        while (isRunning) {
            adminView.adminMenu();
            String mainchoice = scanner.nextLine();
            switch (mainchoice) {
                case "1":
                    //(회원가입 신청 목록)을 선택했을 경우
                    adminService.getRegistrationRequests();
                    adminView.viewRegistrationRequests();

                    //이름입력 받기 -> , 로 구분해서 입력한 이름 User의 Role MEMBER로 바꿔주기
                    System.out.println("회원가입 승인할 회원의 이름을 입력해주세요.(,를 사용해서 여러명을 입력할 수 있습니다.)");
                    String inputName = scanner.nextLine();
                    String[] approvedNames = inputName.split(",");

                    // 입력받은 이름들을 서비스로 전달하여 회원 승인 처리
                    adminService.approveMembers(approvedNames);
                    break;

                case "2":
                    //(회원 목록 보기)을 선택했을 경우
                    adminService.getMemberList(); //유저 목록 보여주고
                    adminView.viewMemberList();
                    //유저 인덱스 번호 입력하면 유저의 수업 스케줄 확인
                    System.out.println("수업 정보를 확인할 회원의 인덱스를 입력하세요:");
                    String memberIndex = scanner.nextLine();
                    adminService.viewMemberClassSchedule(memberIndex);
                    break;

                case "3":
                    //(비회원 목록 보기)을 선택했을 경우
                    adminService.getNonMemberList();
                    adminView.viewNonMemberList();
                    // 사용자에게 비회원의 인덱스 번호를 입력받음
                    System.out.println("상담 예약 정보를 확인할 비회원의 인덱스를 입력하세요 (1부터 시작):");
                    String nonmemberIndexString = scanner.nextLine();

                    // 숫자인지 확인하고 숫자로 변환하거나 그대로 사용
                    int nonmemberIndex;
                    try {
                        nonmemberIndex = Integer.parseInt(nonmemberIndexString);
                        // 입력받은 인덱스에 해당하는 비회원의 상담 예약 정보 확인
                        adminService.viewConsultReservation(nonmemberIndex);
                    } catch (NumberFormatException e) {
                        // 숫자가 아닌 경우
                        System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
                    }
                    break;
                //비회원 인덱스 번호 입력하면 비회원의 수업 스케줄 확인
//

                case "4":
                    //(트레이너 목록)을 선택했을 경우
                    adminService.getTrainerList();
                    adminView.viewTrainerList();

                    Scanner scan = new Scanner(System.in);
                    System.out.println("트레이너 상세 정보를 확인할 트레이너의 인덱스를 입력하세요 (1부터 시작):");
                    String trainerIndexString = scan.nextLine();

                    int trainerIndex;
                    try {
                        trainerIndex = Integer.parseInt(trainerIndexString);
                        // 입력받은 인덱스에 해당하는 트레이너의 상세 정보 확인
                        adminService.getTrainerDetails(trainerIndex);
                    } catch (NumberFormatException e) {
                        // 숫자가 아닌 경우
                        System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
                    }
                    break;

                case "5":
                    //(수업 스케줄 확인)을 선택했을 경우
                    adminService.getSchedule();
                    adminView.viewAllSchedule();
                    break;
                case "6":
                    //(매출 및 인건비 확인)을 선택했을 경우
                    adminView.viewRevenueAndLaborCost();
                    adminService.getRevenueAndLaborCost();
                    break;
                case "*": //로그인 화면으로 돌아가기
                    isRunning = false;
                    break;
                case "#": //시스템 종료하기
                    System.exit(0);
                    break;
                default:
                    System.out.println("잘못된 입력입니다. 다시 입력해주세요.");

            }
        }
    }



}
