package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Trainer extends User {
    public Trainer(String name, String phoneNumber, String age, String sex, String id, String pw, Role role) {
        super(name, phoneNumber, age, sex, id, pw, role);
        this.grade = null;
        this.lessonDay = null;
    }
    public  Trainer(User user){
        this(user.getName(), user.getPhoneNumber(),user.getAge(),user.getSex(),user.getId(),user.getPw(),user.getRole());
    }
    public enum TrainerGrade{
        A, B, C
    }
    public enum Day {
        Mon, Tue, Wed, Thu, Fri, Sat, Sun
    }
    private User user;
    private TrainerGrade grade; //트레이너 등급
    private Day[] lessonDay; //수업할 요일



    public void update(Trainer trainer) {
        //update all prop by setter
    }
}
