package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class User {
    private String name;
    private String phoneNumber;
    private String age;
    private String sex;
    private String id;
    private String pw;

    public void update(User user) {

    }
}
