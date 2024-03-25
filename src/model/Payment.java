package model;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

//paymentOption enum class
@Data
public class Payment implements Serializable {
    //결제를 하면 결제시각, 멤버의 pk, 결제타입을 저장
    private LocalDate paymentTime;//결제 시각(yyyy-MM-dd)
    private String memberPhoneNumber;//멤버
    private PaymentOption paymentOption;//결제 타입

    @Getter
    public enum PaymentOption{
        OPTION_1(10, 90, 700_000),
        OPTION_2(20, 120, 1_200_000),
        OPTION_3(30, 180, 1_500_000);

        private final int sessions;
        private final int validDays;
        private final int price;

        PaymentOption(int sessions, int validDays, int price) {
            this.sessions = sessions;
            this.validDays = validDays;
            this.price = price;
        }

    }

    public Payment(LocalDate paymentTime, String memberPhoneNumber, PaymentOption paymentOption) {
        this.paymentTime = paymentTime;
        this.memberPhoneNumber = memberPhoneNumber;
        this.paymentOption = paymentOption;
    }
}
