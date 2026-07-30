package repository;

import java.util.ArrayList;
import java.util.List;
import domain.User;

public class UserRepository {
    // 메모리 상에서 회원 데이터를 관리할 리스트
    private List<User> userList = new ArrayList<>();

    // 생성자 (나중에 여기서 loadFromFile()을 호출해 시작하자마자 파일을 읽어오게 할 거야)
    public UserRepository() {
        // loadFromFile(); // File I/O 붙일 때 주석 해제!
    }

    /**
     * [F106] 회원 등록
     * @param user 가입할 회원 객체
     * @return 등록 성공 여부
     */
    public boolean insert(User user) {
        // 아이디 중복 체크
        if (selectById(user.getId()) != null) {
            System.out.println("[오류] 이미 존재하는 아이디입니다.");
            return false;
        }
        userList.add(user);
        // saveToFile(); // 데이터 변경 시 파일에 즉시 저장 (나중에 해제)
        return true;
    }

    /**
     * [F107, F110] 아이디로 회원 조회
     * @param id 찾을 아이디
     * @return 일치하는 User 객체 (없으면 null)
     */
    public User selectById(String id) {
        for (User u : userList) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    /**
     * [F108] 회원 정보 수정
     * @param updatedUser 변경된 정보를 담은 User 객체
     * @return 수정 성공 여부
     */
    public boolean update(User updatedUser) {
        User existingUser = selectById(updatedUser.getId());
        if (existingUser != null) {
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setName(updatedUser.getName());
            existingUser.setHeight(updatedUser.getHeight());
            existingUser.setWeight(updatedUser.getWeight());
            existingUser.setDisease(updatedUser.getDisease());
            // saveToFile(); // 변경사항 파일 저장
            return true;
        }
        return false;
    }

    /**
     * [F109] 회원 비활성화 (삭제 대신 상태 변경)
     * @param id 탈퇴할 회원 아이디
     * @return 처리 성공 여부
     */
    public boolean deactivate(String id) {
        User user = selectById(id);
        if (user != null) {
            user.setActive(false); // 비활성화 처리
            // saveToFile(); // 변경사항 파일 저장
            return true;
        }
        return false;
    }

    /**
     * 전체 회원 목록 조회 (필요 시 관리자용이나 디버깅용으로 활용)
     */
    public List<User> selectAll() {
        return userList;
    }

    // ==========================================
    // [Util-JSON 연동용 뼈대] 추후 GSON/Jackson 붙일 영역
    // ==========================================
    public void loadFromFile() {
        // TODO: JsonUtil이나 FileUtil을 이용해 "data/users.json"에서 List<User> 복원하기
    }

    public void saveToFile() {
        // TODO: JsonUtil이나 FileUtil을 이용해 userList를 "data/users.json"에 저장하기
    }
}