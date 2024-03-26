package view;

public class AdminView {

    public void adminMenu(){
        System.out.println("****************************");
        System.out.println("        [ 관리자 메뉴 ]");
        System.out.println("****************************");
        System.out.println("1. 회원가입 신청 목록");
        System.out.println("2. 회원 목록 보기");
        System.out.println("3. 비회원 목록 보기");
        System.out.println("4. 트레이너 목록");
        System.out.println("5. 수업 스케줄 확인");
        System.out.println("6. 매출 및 인건비 확인");
        System.out.println("-----------------------------");
        System.out.println("*. 로그인 화면으로 돌아가기");
        System.out.println("#. 시스템 종료하기");

        System.out.println("*****************************");
        System.out.print("원하는 메뉴를 선택하세요.  ");


    }

    //메뉴 1번(회원가입 신청 목록)을 선택했을 경우
    public void viewRegistrationRequests(){
        //!!!!!!!!!!!!여기서 회원가입 신청을 한 사용자목록이 출력하기!!!!!!!!!!!!!!!!
        System.out.println("0. 관리자 메뉴로 돌아가기");
        System.out.println("*. 로그인 화면으로 돌아가기");
        System.out.println("*****************************");
        System.out.println("회원가입 승인할 회원의 이름을 입력해주세요.(,를 사용해서 여러명을 입력할 수 있습니다.)");

    }

    // 메뉴 2번(회원 목록 보기)을 선택했을 경우
    public void viewMemberList(){
        // !!!!!!!!!!!!!!!!여기서 회원 이름, 나이, 아이디, 휴대폰 번호 이름 순으로 출력!!!!!!!!!!!!!!!!
        System.out.println("********************************************************");
        System.out.println("회원 별 수업 스케줄 확인을 원할 경우 회원의 인덱스 번호를 입력하세요.");
        System.out.println("@. 뇨쇼 회원 확인");
        System.out.println("!. 수업 연장 마케팅 전송");

        displayBasicMenuForAdmin();
    }

    //1. 회원 별 수업 스케줄 확인
    public void viewMemberSchedule(){
        ///!!!!!!!!!!!!!!!!여기서 선택된 회원의 정보와 수업 스케줄이 출력되어야함!!!!!!!!!!!!!!!!
        displayBasicMenuForAdmin();

    }

    // 2. 뇨쇼 회원 확인
    public void viewNoShowMembers(){
        //!!!!!!!!!!!!!!!!여기서 노쇼 회원정보 출력!!!!!!!!!!!!!!!!
        displayBasicMenuForAdmin();
    }

    //3. 수업 연장 마케팅 전송
    public void sendMarketingMessage(){
        //!!!!!!!!!!!!!!!여기서 3회 이하로 남은 회원이름 출력!!!!!!!!!!!!!!!!
        System.out.println("마케팅 메세지를 전송하시겠습니까? (예 : 1 / 아니오 : 2)");
        displayBasicMenuForAdmin();

    }

    // 메뉴 3번(비회원 목록 보기)을 선택했을 경우
    public void viewNonMemberList(){
        // !!!!!!!!!!!!!!!!여기서 회원 이름, 나이, 아이디, 휴대폰 번호 이름 순으로 출력!!!!!!!!!!!!!!!!
        System.out.println("********************************************************");
        System.out.println("비회원 상담 스케줄 확인(확인하고 싶은 비회원의 인덱스 번호를 입력하세요.)");
        displayBasicMenuForAdmin();
    }





    //메뉴 4번(트레이너 목록)을 선택했을 경우
    public void viewTrainerList(){
        //!!!!!!!!!!!!!!!!여기서 모든 트레이너의 이름과 전 월 수입이 전 월 수입이 높은순으로 출력!!!!!!!!!!!!!!!!
        System.out.print("트레이너 상세 정보를 확인할 트레이너의 인덱스를 입력하세요 (1부터 시작) : ");


    }

    public void viewTrainerProfile(){
        //!!!!!!!!!!!!!!!!여기서 선택된 트레이너 정보 출력!!!!!!!!!!!!!!!!
        displayBasicMenuForAdmin();
    }


    //메뉴 5번(수업 스케줄 확인)을 선택했을 경우
    public void viewAllSchedule(){
        //!!!!!!!!!!!!!!!!여기서 예정된 모든 수업 정보 (트레이너, 해당시간, 예약 인원 수)가 출력!!!!!!!!!!!!!!!!
        displayBasicMenuForAdmin();


    }

    //메뉴 6번(매출 및 인건비 확인)을 선택했을 경우
    public void viewRevenueAndLaborCost(){
        // [한달 총 매출]  - 해당 달에 결제한 모든 금액이 출력된다.
        // [관리자의 이익] - (한달 총 매출) - (총 인건비)이 출력된다.
        // [현재까지 총 매출] - 현재까지 결제한 모든 금액이 출력된다.
        displayBasicMenuForAdmin();


    }

    void displayBasicMenuForAdmin(){
        System.out.println("--------------------------------------------------------");
        System.out.println("0. 관리자 메뉴로 돌아가기");
        System.out.println("*. 로그인 화면으로 돌아가기");
        System.out.println("********************************************************");
        System.out.print("원하는 메뉴를 선택하세요:  ");

    }
}
