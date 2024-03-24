package view;
public class TrainerView {

    //트레이너 메뉴 확인
    void trainerMenu(){
        System.out.println("1. 수업시간표 확인");
        System.out.println("2. 수업 요일 선택하기");
        System.out.println("3. 회원 출석체크");
        System.out.println("4. 수입 확인");

        System.out.println("*. 로그인 화면으로 돌아가기");
        System.out.println("#. 시스템 종료하기");
        System.out.println("************************************************");
        System.out.print("원하는 메뉴를 선택하세요.");
    }

    //메뉴 1번(수업시간표 확인)을 선택했을 경우
    void viewSchedule(){
        //여기서 날짜와 시간 순으로 예약된 회원 목록볼 수 있음
        displayBasicMenuForTrainer();
    }

    //메뉴 2번(수업 요일 선택하기)을 선택했을 경우
    void chooseClassDay(){
        System.out.println("원하는 요일을 선택하세요 : ");
        displayBasicMenuForTrainer();

    }

    //메뉴 3번(회원 출석체크)을 선택했을 경우
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

    void displayBasicMenuForTrainer(){
        System.out.println("0. 트레이너 메뉴로 돌아가기");
        System.out.println("*. 로그인 화면으로 돌아가기");
        System.out.println("************************************************");
        System.out.print("원하는 메뉴를 선택하세요.");

    }



}
