package view;

import model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdminView extends BaseView {

    public String requestAdminMenu() throws IllegalAccessException {
        print("\n\n\n");
        print("                ADMIN MENU                   \n");
        return requestMenuSelect(
                "회원가입\n\t\t\t\t   신청 목록",
                "회원 목록",
                "비회원 목록",
                "트레이너 목록",
                "남은 수업\n\t\t\t\t   스케줄",
                "매출 및 인건비",
                "뇨쇼 회원 확인",
                "수업 연장\n\t\t\t\t   마케팅 전송"
        );
    }

    public void printRegistrationRequests(List<User> registrationRequests) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatTitle("승인 대기 중인 사용자"));
        int index = 1;
        for (User user : registrationRequests) {
            sb.append(index).append(". ").append(user.getName()).append(" (").append(user.getPhoneNumber()).append(")\n");
            index++;
        }
        print(sb.toString());
        println(SEPARATOR);
        printRequestInput("승인할 회원의 번호(,로 구분)");
    }

    public void showNoPendingApprovalsMessage(){
        printlnError("\"승인 대기 중인 회원이 없습니다.\"\n");
    }

    public void printMemberDetailInfo(Member member) {
        println(SEPARATOR);
        printSpecial(
                """
                이름: %s
                성별: %s
                나이: %d세
                아이디: %s
                휴대폰 번호: %s
                
                """.formatted(
                        member.getName(),
                        member.getSex(),
                        member.getAge(),
                        member.getId(),
                        member.getPhoneNumber()).trim()
        );
        println(SEPARATOR);
    }

//    public void printApproveMessage(User user){
//        System.out.println(user.getName() + "님이 승인되었습니다.\n");
//    }

    public void printInvalidMessage(){
        printlnError("잘못된 입력입니다.");
    }
    public void printMemberClassSchedule(Member member, List<Reservation> reservations) {
        int totalAttendanceCount = 0;
        int noShowCount = 0;

        StringBuilder sb = new StringBuilder();
        sb.append(SEPARATOR);
        sb.append(formatTitle("%s님의 수업 스케쥴").formatted(member.getName()));
        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            sb.append("  ").append(i + 1).append("회차 ").append(reservation.getStartDate()).append(" ");
            if (reservation.isNoShowUser(member)) {
                sb.append("노쇼");
                noShowCount += 1;
            } else if (reservation.isEnd()){
                sb.append("출석 트레이너 ").append(reservation.getManager().getName());
                totalAttendanceCount++;
            } else {
                sb.append("예약 트레이너 ").append(reservation.getManager().getName());
            }
        }
        sb.append(SEPARATOR);

        int remainingCount = member.getRemainSessionCount();
        int reservationCount = reservations.size();

        println(
                "남은 횟수 : " + remainingCount + "회 /" +
                "예약된 횟수 : " + reservationCount + "회 /" +
                "출석 : " + totalAttendanceCount + "회 /" +
                "노쇼 : " + noShowCount + "회");
        println(SEPARATOR);
    }

    public void printUserAbstractInfo(User user) {
        println("[" + user.getName() + "] " + user.getSex() + " " + user.getAge() + "세 " + user.getId() + " " + user.getPhoneNumber());
    }

    public void printConsultReservationInfo(User nonMember, List<Reservation> consultReservations) {
        println(SEPARATOR);
        printSpecial("[" + nonMember.getName() + "] " + nonMember.getPhoneNumber() + "\n");
        Reservation consult = consultReservations.get(0);

        if (consult != null) {
            printSpecial("담당 트레이너 : " + consult.getManager().getName() + "\n");
            printSpecial("상담 예약일 : " + consult.getStartDate() + "\n");
        } else {
            printlnError("예약 정보 없음");
        }
        println(SEPARATOR);
    }


    public void printNoShowMembers(List<Member> noShowMembers) {
        println(SEPARATOR);
        if (!noShowMembers.isEmpty()) {
            println(formatTitle("노쇼 회원 목록"));
            for (Member member : noShowMembers) {
                printUserAbstractInfo(member);
            }
        } else {
            printlnError("\"노쇼 회원이 없습니다.\"\n");
        }
        println(SEPARATOR);
    }

    public void printNoNonMemberReservationInfoMessage(){
        printlnError("상담 예약 정보를 확인할 비회원이 없습니다.\n");
    }


    // 메뉴 2번(회원 목록 보기)을 선택했을 경우
    public void printMembers(List<Member> members){
        // !!!!!!!!!!!!!!!!여기서 회원 이름, 나이, 아이디, 휴대폰 번호 이름 순으로 출력!!!!!!!!!!!!!!!!
        members.sort(Comparator.comparing(User::getName));
        print(formatTitle("회원 목록"));
        int index = 1;
        for (Member member : members) {
            print(index + ": ");
            printUserAbstractInfo(member);
            index += 1;
        }
        println(SEPARATOR);
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
        print(formatTitle("비회원 목록"));
        if (!nonMemberList.isEmpty()) {
            for (int i = 0; i < nonMemberList.size(); i++) {
                User user = nonMemberList.get(i);
                int index = i + 1;
                print(index + ". ");
                print("이름: " + user.getName() + "\t");
                println("휴대폰 번호: " + user.getPhoneNumber());
            }
        }
        println(SEPARATOR);
    }

    //메뉴 4번(트레이너 목록)을 선택했을 경우
    public void printTrainers(List<Trainer> trainers){
        print(formatTitle("트레이너 목록"));
        //!!!!!!!!!!!!!!!!여기서 모든 트레이너의 이름과 전 월 수입이 전 월 수입이 높은순으로 출력!!!!!!!!!!!!!!!!
        for (int i=0; i< trainers.size(); i++) {
            print((i+1) + ": ");
            printUserAbstractInfo(trainers.get(i));
        }
        println(SEPARATOR);
        printRequestInput("확인할 트레이너의 번호");
    }

    public void printTrainerInfo(Trainer trainer) {
        println(SEPARATOR);
        printSpecial(
                """
                이름: %s
                성별: %s
                나이: %d세
                아이디: %s
                휴대폰 번호: %s
                등급: %s
                
                """.formatted(
                        trainer.getName(),
                        trainer.getSex(),
                        trainer.getAge(),
                        trainer.getId(),
                        trainer.getPhoneNumber(),
                        trainer.getGrade().name()
                        ).trim()
        );
        println(SEPARATOR);
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

    public void printTrainerDetails(Trainer trainer, List<String> details) {
        println(formatTitle("수입 정보"));
        printTrainerInfo(trainer);
        StringBuilder sb = new StringBuilder();
        for (String detail : details) {
            sb.append(detail).append('\n');
        }
        printSpecial(sb.toString());
        println(SEPARATOR);
    }
}
