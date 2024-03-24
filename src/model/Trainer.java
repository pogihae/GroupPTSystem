package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Trainer extends User {
    public Trainer(String name, String phoneNumber, String age, String sex, String id, String pw) {
        super(name, phoneNumber, age, sex, id, pw);
        this.grade = null;
        this.lessonDay = null;
    }

    enum TrainerGrade{
        A, B, C
    }
    enum Day {
        Mon, Tue, Wed, Thu, Fri, Sat, Sun
    }
    private User user;
    private TrainerGrade grade; //트레이너 등급
    private String[] lessonDay; //수업할 요일



    public void update(Trainer trainer) {
        //update all prop by setter
    }
}
