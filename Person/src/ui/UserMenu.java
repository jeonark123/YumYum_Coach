package ui;

import java.util.Scanner;
import domain.User;
import manager.UserManager;

public class UserMenu {
    // 키보드 입력을 받기 위한 Scanner 객체 생성
    private Scanner sc = new Scanner(System.in);
    // 비즈니스 로직 처리를 담당하는 UserManager 연결
    private UserManager userManager = new UserManager();

    // 회원 관리 콘솔 메뉴를 시작하는 메인 반복 메서드
    public void displayMenu() {
        while (true) {
            System.out.println("\n=============================");
            System.out.println("     [ 냠냠코치 회원 관리 ]     ");
            System.out.println("=============================");

            // 로그인 상태에 따라 다른 메뉴 콘솔에 출력 (F110 요구사항 반영)
            if (!userManager.isLoggedIn()) {
                // 로그인 전 메뉴
                System.out.println("1. 로그인");
                System.out.println("2. 회원가입");
                System.out.println("0. 뒤로가기(메인으로)");
                System.out.print("메뉴 선택 >> ");
                
                String choice = sc.nextLine();
                switch (choice) {
                    case "1":
                        handleLogin();
                        break;
                    case "2":
                        handleRegister();
                        break;
                    case "0":
                        System.out.println("[안내] 메인 메뉴로 돌아갑니다.");
                        return; // 메서드를 종료하여 상위 메뉴로 이동
                    default:
                        System.out.println("[오류] 잘못된 입력입니다. 다시 선택해주세요.");
                }
            } else {
                // 로그인 후 메뉴
                System.out.println("1. 내 정보 조회");
                System.out.println("2. 내 정보 수정");
                System.out.println("3. 회원 탈퇴 (비활성화)");
                System.out.println("4. 로그아웃");
                System.out.println("0. 뒤로가기(메인으로)");
                System.out.print("메뉴 선택 >> ");
                
                String choice = sc.nextLine();
                switch (choice) {
                    case "1":
                        handleShowInfo();
                        break;
                    case "2":
                        handleUpdateInfo();
                        break;
                    case "3":
                        handleDeleteAccount();
                        break;
                    case "4":
                        userManager.logout();
                        break;
                    case "0":
                        System.out.println("[안내] 메인 메뉴로 돌아갑니다.");
                        return;
                    default:
                        System.out.println("[오류] 잘못된 입력입니다. 다시 선택해주세요.");
                }
            }
        }
    }

    // [F110] 로그인 입출력 처리
    private void handleLogin() {
        System.out.println("\n--- [ 로그인 ] ---");
        System.out.print("아이디 : ");
        String id = sc.nextLine();
        System.out.print("비밀번호 : ");
        String password = sc.nextLine();

        // UserManager의 login 메서드 호출하여 결과 확인
        userManager.login(id, password);
    }

    // [F106] 회원가입 입출력 처리
    private void handleRegister() {
        System.out.println("\n--- [ 회원가입 ] ---");
        System.out.print("사용할 아이디 : ");
        String id = sc.nextLine();
        System.out.print("비밀번호 : ");
        String password = sc.nextLine();
        System.out.print("이름 : ");
        String name = sc.nextLine();
        
        // 숫자 입력 시 예외 발생을 방지하기 위해 Double.parseDouble() 사용
        // (sc.nextDouble()을 쓰면 엔터 버퍼 문제로 다음 nextLine()이 씹히는 현상이 발생함)
        System.out.print("키(cm) : ");
        double height = Double.parseDouble(sc.nextLine());
        System.out.print("몸무게(kg) : ");
        double weight = Double.parseDouble(sc.nextLine());
        
        System.out.print("질환 정보(없으면 '없음' 입력) : ");
        String disease = sc.nextLine();

        // 입력받은 데이터로 회원가입 요청
        userManager.registerUser(id, password, name, height, weight, disease);
    }

    // [F107] 내 정보 조회 출력 처리
    private void handleShowInfo() {
        System.out.println("\n--- [ 내 프로필 정보 ] ---");
        User user = userManager.getMyInfo();
        if (user != null) {
            System.out.println("아이디   : " + user.getId());
            System.out.println("이름     : " + user.getName());
            System.out.println("키       : " + user.getHeight() + " cm");
            System.out.println("몸무게   : " + user.getWeight() + " kg");
            System.out.println("질환정보 : " + user.getDisease());
            System.out.println("계정상태 : " + (user.isActive() ? "활성화" : "비활성화"));
        }
    }

    // [F108] 내 정보 수정 입출력 처리
    private void handleUpdateInfo() {
        System.out.println("\n--- [ 내 정보 수정 ] ---");
        System.out.println("(수정을 원치 않는 항목도 새로운 값을 입력해야 합니다)");
        System.out.print("새 비밀번호 : ");
        String password = sc.nextLine();
        System.out.print("이름 : ");
        String name = sc.nextLine();
        System.out.print("키(cm) : ");
        double height = Double.parseDouble(sc.nextLine());
        System.out.print("몸무게(kg) : ");
        double weight = Double.parseDouble(sc.nextLine());
        System.out.print("질환 정보 : ");
        String disease = sc.nextLine();

        // 수정된 정보 저장 요청
        userManager.updateMyInfo(password, name, height, weight, disease);
    }

    // [F109] 회원 탈퇴(비활성화) 확인 및 처리
    private void handleDeleteAccount() {
        System.out.println("\n--- [ 회원 탈퇴 ] ---");
        System.out.print("정말로 탈퇴하시겠습니까? (Y/N) : ");
        String confirm = sc.nextLine();

        // 대소문자 상관없이 y가 입력되었을 때만 탈퇴 진행
        if (confirm.equalsIgnoreCase("Y")) {
            userManager.deleteMyAccount();
        } else {
            System.out.println("[안내] 탈퇴가 취소되었습니다.");
        }
    }
}