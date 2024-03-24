package view;

import java.util.Scanner;

public class UserView {
    public void showSignupMenu() {
        System.out.println("*************************************************");
        System.out.println("형식에 맞는 양식을 작성해 주세요.");
        System.out.println("*************************************************");
    }

    public void requestName() {
        System.out.print("이름: ");
    }

    public void requestAge() {
        System.out.print("나이: ");
    }

    public void requestSex() {
        System.out.print("성별: ");
    }

    public void requestId() {
        System.out.print("아이디: ");
    }

    public void requestPw() {
        System.out.print("비밀번호: ");
    }

    public void requestPhoneNumber() {
        System.out.print("휴대폰번호: ");
    }

    public void requestRole() {
        System.out.print("뭘로가입? : ");
    }

    public void showSigned() {
        System.out.println("*************************************************");
        System.out.println("회원가입 신청이 완료되었습니다. 승인 메시지가 전송되면 로그인이 가능합니다.");
        System.out.println("*************************************************");

    }
}
