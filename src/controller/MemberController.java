package controller;

import model.Member;
import model.User;

public class MemberController {
    public static void main(String[] args) {
        User user1 = new User("홍길동", "010-1111-1111", "28", "여", "1", "1");
        Member member1 = new Member(user1);
        member1.payForClass();
    }
}
