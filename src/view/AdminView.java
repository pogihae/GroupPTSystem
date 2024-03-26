package view;

import model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdminView extends BaseView{

    public String requestAdminMenu() {
        requestMenuSelect(
                "회원가입 신청 목록",
                "회원 목록",
                "비회원 목록",
                "트레이너 목록",
                "전체 수업 스케줄",
                "매출 및 인건비",
                "뇨쇼 회원 확인",
                "수업 연장 마케팅 전송"
        );
        return readLine();
    }

    public void printRegistrationRequests(List<User> registrationRequests) {
        System.out.println("승인 대기 중인 사용자의 이름:");
        int index = 1;
        for (User user : registrationRequests) {
            System.out.println(index + ". " + user.getName() + "\t" + user.getPhoneNumber());
            index++;
        }
        System.out.println("승인할 회원의 인덱스를 입력하세요. 여러 명을 선택할 경우 쉼표(,)로 구분하세요:");

    }

    public void showNoPendingApprovalsMessage(){
        System.out.println("\"승인 대기 중인 회원이 없습니다.\"\n");
    }

    public void printMemberDetailInfo(Member member) {
        System.out.println("--------------------------------------------------------------------------");
        System.out.print("이름: " + member.getName() + "\t");
        System.out.print("성별: " + member.getSex() + "\t");
        System.out.print("나이: " + member.getAge()+ "\t");
        System.out.print("아이디: " + member.getId()+ "\t");
        System.out.println("휴대폰 번호: " + member.getPhoneNumber());
        System.out.println("--------------------------------------------------------------------------");
    }

//    public void printApproveMessage(User user){
//        System.out.println(user.getName() + "님이 승인되었습니다.\n");
//    }

    public void printInvalidMessage(){
        System.out.println("잘못된 입력입니다.\n");
    }
    public void printMemberClassSchedule(Member member, List<Reservation> reservations) {
        int totalAttendanceCount = 0;
        int noShowCount = 0;

        System.out.println(member.getName() + "님의 수업 스케줄:");
        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            System.out.print("  " + (i + 1) + "회차 " + reservation.getStartDate() + " ");
            if (reservation.isNoShowUser(member)) {
                System.out.println("노쇼");
                noShowCount += 1;
            } else {
                System.out.println("출석 트레이너 " + reservation.getManager().getName());
                totalAttendanceCount++;
            }
        }
        Payment payment = member.getPayment();
        int totalPaymentCount = (payment == null)? 0 : payment.getPaymentOption().getSessions();
        int remainingCount = member.getRemainSessionCount();
        int reservationCount = reservations.size();

        System.out.println(" 총 결제 횟수 : " + totalPaymentCount + "회 /" +
                "남은 회수 : " + remainingCount + "회 /" +
                "예약된 횟수 : " + reservationCount + "회 /" +
                "축석 : " + totalAttendanceCount + "회 /" +
                "노쇼 : " + noShowCount + "회");
    }

    public void printMemberAbstractInfo(Member member) {
        System.out.println("[" + member.getName() + "] " + member.getSex() + " " + member.getAge() + "세 " + member.getId() + " " + member.getPhoneNumber());
    }

    public void printConsultReservationInfo(User nonMember, List<Reservation> consultReservations) {
        System.out.println("[" + nonMember.getName() + "] " + nonMember.getPhoneNumber());

        for (Reservation reservation : consultReservations) {
            System.out.println("상담 예약일 : " + reservation.getStartDate());
        }
    }


    public void printNoShowMembers(List<Member> noShowMembers) {
        if (!noShowMembers.isEmpty()) {
            System.out.println("노쇼 회원 목록:");
            for (Member member : noShowMembers) {
                printMemberDetailInfo(member);
            }
        } else {
            System.out.println("\"노쇼 회원이 없습니다.\"\n");
        }
    }

    public void printNoNonMemberReservationInfoMessage(){
        System.out.println("상담 예약 정보를 확인할 비회원이 없습니다.\n");
    }


    // 메뉴 2번(회원 목록 보기)을 선택했을 경우
    public void printMembers(List<Member> members){
        // !!!!!!!!!!!!!!!!여기서 회원 이름, 나이, 아이디, 휴대폰 번호 이름 순으로 출력!!!!!!!!!!!!!!!!
        members.sort(Comparator.comparing(User::getName));

        System.out.println("---회원 목록---");

        int index = 1;
        for (Member member : members) {
            System.out.print(index + ": ");
            printMemberAbstractInfo(member);
            index += 1;
        }
    }


    public void sendMarketingMessagesToMembers(List<Member> membersWithFewSessionsLeft){
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

    //메뉴 4번(트레이너 목록)을 선택했을 경우
    public void viewTrainerList(List<Trainer> trainers){
        //!!!!!!!!!!!!!!!!여기서 모든 트레이너의 이름과 전 월 수입이 전 월 수입이 높은순으로 출력!!!!!!!!!!!!!!!!
        for (int i=0; i< trainers.size(); i++) {
            System.out.print((i+1) + ": ");
            printTrainerInfo(trainers.get(i));
        }
        System.out.print("트레이너 상세 정보를 확인할 트레이너의 인덱스를 입력하세요 (1부터 시작) : ");
    }

    public void printTrainerInfo(Trainer trainer) {
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.print("트레이너 이름: " + trainer.getName() + "\t");
        System.out.print("트레이너 나이: " + trainer.getAge() + "\t");
        System.out.print("트레이너 성별: " + trainer.getSex() + "\t");
        System.out.print("트레이너 등급: " + trainer.getGrade() + "\t");
        System.out.println("트레이너 휴대폰 번호: " + trainer.getPhoneNumber());
        System.out.println("-------------------------------------------------------------------------------------------");
    }

    public void printTrainerIncomeRecords(Trainer trainer) {
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("[트레이너 " + trainer.getName() + "의 수입 기록]");

    }

//    public void printReservationDetails(Reservation reservation) {
//        System.out.print("날짜 / 시간 : " + reservation.getStartDate() + "\t");
//        System.out.print("트레이너: " + reservation.getManager() + "\t");
//        System.out.println("예약 인원 수: " + reservation.getUsers().size());
//        System.out.println("-------------------------------------------------");
//    }

    public void printNoClassSchedule(){
        System.out.println("\"예정된 수업이 없습니다.\"\n");
    }

    public void printFinancialSummary(int monthlyRevenue, int totalLaborCost, int totalRevenue) {
        System.out.println("");
        System.out.println("--------------------------------");
        System.out.printf("[한달 총 매출]:\t%,d원%n", monthlyRevenue);
        System.out.printf("[총 인건비]:\t\t%,d원%n", totalLaborCost);
        System.out.printf("[총 매출]:\t\t%,d원%n", totalRevenue);
        System.out.println("--------------------------------");
        System.out.println("");
    }

    public void printTrainerDetails(List<String> details) {
        StringBuilder sb = new StringBuilder();
        for (String detail : details) {
            sb.append(detail).append('\n');
        }
        System.out.println(sb);
    }
}
