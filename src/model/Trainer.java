package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import util.Utils;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Trainer extends User implements Serializable {
    private static final int MAX_LESSON_DAY = 3;

    public Trainer(String name, String phoneNumber, int age, String sex, String id, String pw, Role role) {
        super(name, phoneNumber, age, sex, id, pw, role);
        this.grade = Grade.C;
        this.lessonDays = null;
    }

    public Trainer(User user) {
        this(user.getName(), user.getPhoneNumber(), user.getAge(), user.getSex(), user.getId(), user.getPw(), user.getRole());
    }

    @AllArgsConstructor
    public enum Grade {
        A(40000), B(30000), C(20000);

        public final int incomePerClass;
    }

    private User user;
    private Grade grade; //트레이너 등급
    private Utils.Day[] lessonDays; //수업할 요일
  
    public int calculateIncome(int totalClassNum) {
        return totalClassNum * grade.incomePerClass;
    }

    public boolean setLessonDays(Utils.Day[] days) {
        if (days.length > MAX_LESSON_DAY) {
            return false;
        }
        lessonDays = days;
        return true;
    }

    public void update(Trainer trainer) {
        //update all prop by setter
    }
}
