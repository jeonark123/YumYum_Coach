package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import domain.User;

public class UserRepository {
    // 메모리 상에서 회원 데이터를 관리할 리스트
    private List<User> userList = new ArrayList<>();
    // 저장할 파일 경로 및 파일명 지정
    private final String FILE_PATH = "users.txt";

    // 생성자 (프로그램 시작 시 자동으로 파일에서 데이터 불러오기)
    public UserRepository() {
        loadFromFile();
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
        saveToFile(); // // 데이터 변경 시 파일에 즉시 저장
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
            saveToFile(); // // 변경사항 파일 저장
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
            saveToFile(); // // 변경사항 파일 저장
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

    // [Util-File I/O] 텍스트 파일 기반 영구 저장 및 불러오기
    public void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            // // 저장된 파일이 없으면 새로 읽을 필요 없이 종료
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                User user = User.fromFileString(line);
                if (user != null) {
                    userList.add(user);
                }
            }
            System.out.println("[안내] 기존 회원 데이터를 파일에서 성공적으로 불러왔습니다.");
        } catch (IOException e) {
            System.out.println("[오류] 파일 불러오기 실패 : " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : userList) {
                bw.write(user.toFileString());
                bw.newLine(); // // 다음 회원을 위해 줄바꿈
            }
        } catch (IOException e) {
            System.out.println("[오류] 파일 저장 실패 : " + e.getMessage());
        }
    }
}