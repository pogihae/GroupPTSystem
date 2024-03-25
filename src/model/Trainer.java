package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;

@Getter
@Setter
@AllArgsConstructor
public class Trainer extends User {
    public Trainer(String name, String phoneNumber, String age, String sex, String id, String pw, Role role) {
        super(name, phoneNumber, age, sex, id, pw, role);
        this.grade = null;
        this.lessonDay = null;
    }

    public enum TrainerGrade{
        A, B, C
    }
    public enum Day {
        Mon(DayOfWeek.MONDAY),
        Tue(DayOfWeek.TUESDAY),
        Wed(DayOfWeek.WEDNESDAY),
        Thu(DayOfWeek.THURSDAY),
        Fri(DayOfWeek.FRIDAY),
        Sat(DayOfWeek.SATURDAY),
        Sun(DayOfWeek.SUNDAY);

        private final DayOfWeek dayOfWeek;

        Day(DayOfWeek dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public DayOfWeek toDayOfWeek() {
            return this.dayOfWeek;
        }
    }
    private User user;
    private TrainerGrade grade; //트레이너 등급
    private Day[] lessonDay; //수업할 요일



    public void update(Trainer trainer) {
        //update all prop by setter
    }
}
