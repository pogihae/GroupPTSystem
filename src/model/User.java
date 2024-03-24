package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {

    public enum Role{
        TRAINER, MEMBER, USER, ADMIN, NONMEMBER
    }//User 모델 안에 들어가야한다.

    private String name;
    private String phoneNumber;
    private String age;
    private String sex;
    private String id;
    private String pw;
    private Role role;

    public User(User user) {
        update(user);
    }

    public void update(User user) {
        this.name = user.name;
        this.phoneNumber = user.phoneNumber;
        this.age = user.age;
        this.sex = user.sex;
        this.id = user.id;
        this.pw = user.pw;
        this.role = user.role;
    }
}
