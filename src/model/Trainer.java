package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import util.Utils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
public class Trainer extends User {
    private static final int MAX_LESSON_DAY = 3;
    private User user;
    private Grade grade; //트레이너 등급
    private List<Utils.Day> lessonDays; //수업할 요일

    public Trainer(String name, String phoneNumber, int age, String sex, String id, String pw) {
        super(name, phoneNumber, age, sex, id, pw, Role.TRAINER);
        this.grade = Grade.C;
        this.lessonDays = new ArrayList<>(MAX_LESSON_DAY);
    }

    public Trainer(User user) {
        this(user.getName(), user.getPhoneNumber(), user.getAge(), user.getSex(), user.getId(), user.getPw());
        this.setState(user.getState());
    }

    public int calculateIncome(int totalClassNum) {
        return totalClassNum * grade.incomePerClass;
    }

    public boolean setLessonDays(Utils.Day[] days) {
        lessonDays = List.of(days);
        return true;
    }

    public void update(Trainer trainer) {
        super.update(trainer);
        this.grade = trainer.grade;
        this.lessonDays = trainer.lessonDays;
    }

    @AllArgsConstructor
    public enum Grade {
        A(40000), B(30000), C(20000);

        public final int incomePerClass;
    }
}
