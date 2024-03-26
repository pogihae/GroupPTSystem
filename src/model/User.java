package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Getter
public class User implements Serializable {

    public enum Role {
        TRAINER, MEMBER, USER, ADMIN, NONMEMBER
    }//User 모델 안에 들어가야한다.

    private String name;
    private String phoneNumber;
    private Integer age;
    private String sex;
    private String id;
    private String pw;
    private Role role;

    //상담신청한 유저일경우
    public User(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = null;
        this.sex = null;
        this.id = null;
        this.pw = null;
        this.role  = null;
    }
    public User(User user) {
        update(user);
    }

    public void update(User user) {
        if (user == null) return;
        this.name = user.name;
        this.phoneNumber = user.phoneNumber;
        this.age = user.age;
        this.sex = user.sex;
        this.id = user.id;
        this.pw = user.pw;
        this.role = user.role;
    }
}
