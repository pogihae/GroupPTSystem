package service;
import model.User;
import repository.GroupPTRepository;

import java.util.List;


public class AdminService {
    private final GroupPTRepository groupPTRepository;

    public AdminService(GroupPTRepository groupPTRepository) {
        this.groupPTRepository = groupPTRepository;
    }

    //1. 회원가입 신청 목록
    public void getRegistrationRequests(){
        List<User> users = groupPTRepository.findAllUsers();
        for(User user : users) {
            if(user.getRole().equals("NONMEMBER")) {

            }
        }
    }

    //2. 회원 목록 보기
    public void getMemberList(){
        groupPTRepository.findAllMembers();

    }
    //3. 트레이너 목록
    public void getTrainerList(){
        groupPTRepository.findAllTrainers();

    }

    //4. 수업 스케줄 확인
    public void getSchedule(){
        groupPTRepository.findAllReservations();

    }
    //5. 매출 및 인건비 확인
    public void getRevenueAndLaborCost(){
        groupPTRepository.findAllMembers(); // 결제정보에서 모든 회원이 결제한 거 계산해야함
        groupPTRepository.findAllReservations(); //트레이너별로 수강 인원수 확인해야함

    }

    public void receiveRegistrationRequest(User user){

    }
}
