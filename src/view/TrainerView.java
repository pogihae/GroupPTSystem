package view;

import model.Reservation;
import model.Trainer;
import model.User;
import util.Utils;

import java.util.Arrays;
import java.util.List;

public class TrainerView extends BaseView {

    //트레이너 메뉴
    public String requestTrainerMenu(){
        requestMenuSelect(
                "수업시간표 확인",
                "수업 요일 선택하기",
                "회원 출석체크",
                "수입 확인"
        );
        return readLine();
    }

    //메뉴 1번(수업시간표 확인)을 선택했을 경우
    public void printTrainerReservations(List<Reservation> reservations) {
        printReservations(reservations);
    }

    //메뉴 2번(수업 요일 선택하기)을 선택했을 경우
    public Utils.Day[] requestLessonDays(){
        System.out.println("원하는 요일을 선택하세요 (,로 구분): ");
        System.out.println(Arrays.toString(Utils.Day.values()));
        System.out.println(Utils.Day.of("MON"));
        return Arrays.stream(readLine().split(","))
                .map(String::trim)
                .map(Utils.Day::of)
                .toArray(Utils.Day[]::new);
    }

    //메뉴 3번(회원 출석체크)을 선택했을 경우
    /**
     * @param reservation 해당 시각 예약
     * */
    public List<User> requestAttendance(Reservation reservation) {
        StringBuilder sb = new StringBuilder();
        sb.append("********출석 명단********\n");

        List<User> users = reservation.getUsers();
        for (int i = 0; i < users.size(); i++) {
            sb.append("%d. %s\n".formatted(i + 1, users.get(i)));
        }

        sb.append("출석한 회원들을 입력(,로 구분): ");
        System.out.println(sb);

        return Arrays.stream(readLine().split(","))
                .mapToInt(Integer::parseInt)
                .mapToObj(users::get)
                .toList();
    }

    public void printIncome(Trainer trainer, int income, int year, int month) {
        System.out.println("""
                %s님의 %d년 %d월 수입은
                %d원 입니다.
                """.formatted(trainer.getName(), year, month, income).trim());
    }

    public int[] requestIncomeYearAndMonth() {
        System.out.print("확인하고 싶은 연도를 입력해주세요: ");
        int year = Integer.parseInt(readLine());
        System.out.print("확인하고 싶은 달을 입력해주세요: ");
        int month = Integer.parseInt(readLine());

        return new int[] {year, month};
    }

    //미성년자 출석체크일 경우 __ 이거는 여기서 말고 다른데서 구현해도 될덧?
    void sendMinorAttendanceMessage(){
        System.out.println("출석하였습니다.");
    }

    public void printMinorUserAttendance(boolean attempt, User user) {
        if (attempt) {
            System.out.println("미성년자 출석했습니다.");
        } else {
            System.out.println("미성년자 결석");
        }
        System.out.println(user);
    }


    //메뉴 4번(수입 확인)을 선택했을 경우
    void checkMyIncome(){
        //현재 수입 & 누적 수입이 출력됨
        displayBasicMenuForTrainer();
    }

    public void printTimeTable(List<Reservation> reservations) {
        System.out.println(reservations);
    }

    void displayBasicMenuForTrainer(){
        System.out.println("0. 트레이너 메뉴로 돌아가기");
        System.out.println("*. 로그인 화면으로 돌아가기");
        System.out.println("************************************************");
        System.out.print("원하는 메뉴를 선택하세요.");

    }



}
