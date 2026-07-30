package manager;

import domain.User;
import repository.UserRepository;

public class UserManager {
    private UserRepository userRepository = new UserRepository();
    private User currentUser = null;

    
     // [F106] 회원 가입 (회원 작성)
     
    public boolean registerUser(String id, String password, String name, double height, double weight, String disease) {
        // 아이디 빈값 검증 등 간단한 유효성 검사
        if (id == null || id.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.out.println("[오류] 아이디와 비밀번호는 필수 입력값입니다.");
            return false;
        }

        User newUser = new User(id, password, name, height, weight, disease);
        boolean isSuccess = userRepository.insert(newUser);
        if (isSuccess) {
            System.out.println("[안내] 회원가입이 완료되었습니다. 로그인 해주세요.");
        }
        return isSuccess;
    }

    
     // [F110] 로그인
     
    public boolean login(String id, String password) {
        User user = userRepository.selectById(id);

        // 1. 아이디 존재 여부 및 비밀번호 일치 확인
        if (user != null && user.getPassword().equals(password)) {
            // 2. [F109] 비활성화(탈퇴)된 계정인지 체크
            if (!user.isActive()) {
                System.out.println("[오류] 탈퇴 처리되거나 비활성화된 계정입니다.");
                return false;
            }
            // 로그인 성공 시 세션에 저장
            this.currentUser = user;
            System.out.println("[안내] " + user.getName() + "님 환영합니다!");
            return true;
        }

        System.out.println("[오류] 아이디 또는 비밀번호가 일치하지 않습니다.");
        return false;
    }

    
      //[F110] 로그아웃
     
    public void logout() {
        if (currentUser != null) {
            System.out.println("[안내] " + currentUser.getName() + "님이 로그아웃하셨습니다.");
            this.currentUser = null;
        } else {
            System.out.println("[안내] 현재 로그인 상태가 아닙니다.");
        }
    }

    
    //  [F107] 내 정보 조회 (회원 조회)
     
    public User getMyInfo() {
        if (currentUser == null) {
            System.out.println("[오류] 로그인 후 이용 가능한 기능입니다.");
            return null;
        }
        return currentUser;
    }

    
     // [F108] 내 정보 수정 (회원 수정)
     
    public boolean updateMyInfo(String password, String name, double height, double weight, String disease) {
        if (currentUser == null) {
            System.out.println("[오류] 로그인 후 이용 가능한 기능입니다.");
            return false;
        }

        // 현재 로그인된 회원 객체의 정보 변경
        currentUser.setPassword(password);
        currentUser.setName(name);
        currentUser.setHeight(height);
        currentUser.setWeight(weight);
        currentUser.setDisease(disease);

        // Repository를 통해 갱신
        boolean isSuccess = userRepository.update(currentUser);
        if (isSuccess) {
            System.out.println("[안내] 회원 정보가 성공적으로 수정되었습니다.");
        } else {
            System.out.println("[오류] 정보 수정에 실패했습니다.");
        }
        return isSuccess;
    }

    
    // [F109] 회원 탈퇴 (회원 삭제/비활성화)
     
    public boolean deleteMyAccount() {
        if (currentUser == null) {
            System.out.println("[오류] 로그인 후 이용 가능한 기능입니다.");
            return false;
        }

        String myId = currentUser.getId();
        boolean isSuccess = userRepository.deactivate(myId);
        if (isSuccess) {
            System.out.println("[안내] 회원 탈퇴(비활성화)가 완료되었습니다. 자동 로그아웃됩니다.");
            this.currentUser = null; // 탈퇴 후 자동 로그아웃 처리
        } else {
            System.out.println("[오류] 탈퇴 처리에 실패했습니다.");
        }
        return isSuccess;
    }

   
     // 현재 로그인 여부 반환 (UI에서 메뉴 분기할 때 사용)
     
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    // [F111] 회원 팔로우 기능
    // 자기 자신 팔로우x
    public boolean follow(String myId, String targetId) {
        if (myId.equals(targetId)) {
            System.out.println("자기 자신은 팔로우할 수 없음");
            return false;
        }

        // 존재하는 회원 조회
        User me = userRepository.selectById(myId);
        User target = userRepository.selectById(targetId);
        
        if (me == null || target == null) {
            System.out.println("존재하지 않는 회원입니다.");
            return false;
        }
        
        // 팔로우 시도
        if (me.addFollowing(targetId)) {
            userRepository.update(me);

            System.out.println("[알림]" + target.getName() + "(" + targetId + ")님을 팔로우했습니다.");
            return true;
        } else {
            System.out.println("이미 팔로우하고 있는 회원입니다.");
            return false;
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}