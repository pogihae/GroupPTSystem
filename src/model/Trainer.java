package model;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class Trainer extends User {
    enum TrainerGrade{
        A, B, C
    }
    enum Day {
        Mon, Tue, Wed, Thu, Fri, Sat, Sun
    }
    private User user;
    private String grade; //트레이너 등급
    private String[] lessonDay; //수업할 요일



    public void update(Trainer trainer) {
        //update all prop by setter
    }
}
