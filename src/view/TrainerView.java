package view;

import model.Reservation;
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
        return  (Utils.Day[]) Arrays.stream(readLine().split(","))
                .map(Utils.Day::valueOf)
                .toArray();
    }

    //메뉴 3번(회원 출석체크)을 선택했을 경우
    /**
     * @param reservation 해당 시각 예약
     * */
    public List<User> requestAttendance(Reservation reservation) {
        StringBuilder sb = new StringBuilder();
        sb.append("********출석 명단********\n");

        List<User> users = reservation.getUsers();
        for (int i=0; i<users.size(); i++) {
            sb.append("%d. %s\n".formatted(i+1, users.get(i)));
        }

        sb.append("출석한 회원들을 입력(,로 구분): ");
        System.out.println(sb);

        return Arrays.stream(readLine().split(","))
                .mapToInt(Integer::parseInt)
                .mapToObj(users::get)
                .toList();
    }
    void checkAttendance(){
        //해당 시각을 기준으로 출석해야할 회원의 목록이 출력됨
        System.out.println("이름 옆에 O 또는 X를 입력해주세요");
        //회원의 이름이 하나씩 출력되고 O,X만 입력해서 출석체크
        displayBasicMenuForTrainer();

    }

    //미성년자 출석체크일 경우 __ 이거는 여기서 말고 다른데서 구현해도 될덧?
    void sendMinorAttendanceMessage(){
        System.out.println("출석하였습니다.");
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
