package controller;

import model.User;
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
                    List<User> registrationRequests = adminService.getRegistrationRequests();
                    if (registrationRequests.isEmpty()) {
                        System.out.println("승인 대기 중인 회원이 없습니다.");
                        System.out.println("");
                        break;
                    }

                    System.out.println("승인 대기 중인 사용자의 이름:");
                    int index = 1;
                    for (User user : registrationRequests) {
                        System.out.println(index + ". " + user.getName() + "\t" + user.getPhoneNumber());
                        index++;
                    }

                    //인덱스입력 받기 -> , 로 구분해서 입력한 이름 User의 Role MEMBER로 바꿔주기
                    System.out.println("승인할 회원의 인덱스를 입력하세요. 여러 명을 선택할 경우 쉼표(,)로 구분하세요:");
                    String inputIndices = scanner.nextLine();
                    String[] selectedIndicesStr = inputIndices.split(",");

                    for (String selectedIndexStr : selectedIndicesStr) {
                        int selectedIndex = Integer.parseInt(selectedIndexStr.trim());
                        adminService.approveMember(selectedIndex);
                    }
                    break;

                case "2":
                    //(회원 목록 보기)을 선택했을 경우
                    adminService.getMemberList(); //유저 목록 보여주고
                    adminView.viewMemberList();
                    //유저 인덱스 번호 입력하면 유저의 수업 스케줄 확인
                    String memberIndex = scanner.nextLine();
                    adminService.viewMemberClassSchedule(memberIndex);
                    break;

                case "3":
                    // 비회원 목록 가져오기
                    List<User> nonMemberList = adminService.getNonMemberList();

                    // 비회원 목록이 비어 있는지 확인
                    if (nonMemberList.isEmpty()) {
                        System.out.println("상담을 예약한 회원이 없습니다.\n");
                    } else {
                        // 비회원 목록 출력
                        adminService.printNonMemberList(nonMemberList);

                        System.out.println("상담 예약 정보를 확인할 비회원의 인덱스를 입력하세요 (1부터 시작):");
                        String nonmemberIndexString = scanner.nextLine();

                        int nonmemberIndex;
                        try {
                            nonmemberIndex = Integer.parseInt(nonmemberIndexString);
                            // 입력받은 인덱스에 해당하는 비회원의 상담 예약 정보 확인
                            adminService.viewConsultReservation(nonmemberIndex);
                        } catch (NumberFormatException e) {
                            System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
                        }
                    }
                    break;



//

                case "4":
                    //(트레이너 목록)을 선택했을 경우
                    adminService.getTrainerList();


                    Scanner scan = new Scanner(System.in);
                    adminView.viewTrainerList();
                    String trainerIndexString = scan.nextLine();

                    int trainerIndex;
                    try {
                        trainerIndex = Integer.parseInt(trainerIndexString);
                        // 입력받은 인덱스에 해당하는 트레이너의 상세 정보 확인
                        adminService.getTrainerDetails(trainerIndex);
                    } catch (NumberFormatException e) {
                        System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
                    }
                    break;

                case "5":
                    //(수업 스케줄 확인)을 선택했을 경우
                    adminService.getSchedule();
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
                    System.out.println("잘못된 입력입니다. 다시 입력해주세요.");

            }
        }
    }
}
