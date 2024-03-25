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
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    //(회원가입 신청 목록)을 선택했을 경우
                    adminView.viewRegistrationRequests();
                    adminService.getRegistrationRequests();
                    break;
                case "2":
                    //(회원 목록 보기)을 선택했을 경우
                    adminView.viewMemberList();
                    adminService.getMemberList();
                    //handleMemberListMenu();
                    break;

                case "3":
                    //(비회원 목록 보기)을 선택했을 경우
                    adminView.viewNonMemberList();

                case "4":
                    //(트레이너 목록)을 선택했을 경우
                    adminView.viewTrainerList();
                    adminService.getTrainerList();
                    break;
                case "5":
                    //(수업 스케줄 확인)을 선택했을 경우
                    adminView.viewAllSchedule();
                    adminService.getSchedule();
                    break;
                case "6":
                    //(매출 및 인건비 확인)을 선택했을 경우
                    adminView.viewRevenueAndLaborCost();
                    //adminService.getRevenueAndLaborCost();
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
