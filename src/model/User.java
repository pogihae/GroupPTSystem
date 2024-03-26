package model;

import lombok.*;

import javax.management.relation.Role;
import java.io.Serializable;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class User implements Serializable {

    //상담신청한 유저일경우
    public User(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = null;
        this.sex = null;
        this.id = null;
        this.pw = null;
        this.role  = null;
        this.state = State.NONMEMBER;
    }

    public User(String name, String phoneNumber, int age, String sex, String id, String pw, Role role) {
        this(name, phoneNumber,age,sex,id,pw,role, State.PENDING);
        if (role.equals(Role.ADMIN)) {
            this.state = State.APPROVED;
        }
    }

    public enum Role {
        TRAINER, MEMBER, USER, ADMIN, NONMEMBER
    }

    public enum State{
        APPROVED, PENDING, NONMEMBER
    }

    private String name;
    private String phoneNumber;
    private Integer age;
    private String sex;
    private String id;
    private String pw;
    private Role role;
    private State state;

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
        this.state = user.state;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User other = (User) obj;
            if (this.id == null || other.id == null) {
                return this.phoneNumber.equals(((User) obj).getPhoneNumber());
            }
            return this.id.equals(other.id);
        }
        return false;
    }
}
