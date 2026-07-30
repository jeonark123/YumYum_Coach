package ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import domain.Diet;
import domain.User;
import exception.DietDateNotFoundException;
import manager.DietManagerImpl;
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
                System.out.println("5. 식단 관리");
                System.out.println("6. 다른 회원 팔로우");
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
                    case "5":
                        handleDietMenu();
                        break;
                    case "6":
                        handleFollow();
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
            System.out.println("팔로우중 : " + (user.getFollowingList().isEmpty() ? "없음" : String.join(", ", user.getFollowingList())));
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

        if (confirm.equalsIgnoreCase("Y")) {
            userManager.deleteMyAccount();
        } else {
            System.out.println("[안내] 탈퇴가 취소되었습니다.");
        }
    }
    
    // 식단 관련 메뉴 처리
    private void handleDietMenu() {
        DietManagerImpl dietManager = DietManagerImpl.getInstance();
        User loginUser = userManager.getMyInfo(); 

        while (true) {
            System.out.println("\n========================================");
            System.out.println("  [ 식단 관리 메뉴 - " + loginUser.getName() + "님 ]");
            System.out.println("========================================");
            System.out.println("1. 식단 등록");
            System.out.println("2. 내 전체 식단 조회 (총 칼로리 포함)");
            System.out.println("3. 날짜별 식단 검색");
            System.out.println("0. 이전 메뉴로");
            System.out.print("선택 >> ");

            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    handleRegisterDiet(loginUser, dietManager);
                    break;
                case "2":
                    handleShowAllDiets(loginUser, dietManager);
                    break;
                case "3":
                    handleSearchDietByDate(dietManager);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("[오류] 잘못된 입력입니다.");
            }
        }
    }
    
    // 1. 식단 등록
    private void handleRegisterDiet(User user, DietManagerImpl dietManager) {
        System.out.println("\n--- [ 새 식단 등록 ] ---");
        Diet diet = new Diet();
        diet.setDietId((int) (System.currentTimeMillis() % 10000));
        diet.setUser(user);
        diet.setDietDate(new Date());

        System.out.print("아침 메뉴 (쉼표로 구분 예: 밥,계란후라이) : ");
        String morningInput = sc.nextLine();
        for (String m : morningInput.split(",")) {
            if (!m.trim().isEmpty()) diet.getMorningMenus().add(m.trim());
        }

        System.out.print("점심 메뉴 (쉼표로 구분 예: 닭가슴살,샐러드) : ");
        String lunchInput = sc.nextLine();
        for (String m : lunchInput.split(",")) {
            if (!m.trim().isEmpty()) diet.getLunchMenus().add(m.trim());
        }

        System.out.print("저녁 메뉴 (쉼표로 구분 예: 샐러드) : ");
        String dinnerInput = sc.nextLine();
        for (String m : dinnerInput.split(",")) {
            if (!m.trim().isEmpty()) diet.getDinnerMenus().add(m.trim());
        }

        dietManager.addDiet(diet);
        dietManager.saveData();
        System.out.println("[안내] 식단이 successfully 저장되었습니다.");
    }

    // 2. 전체 식단 및 칼로리 출력
    private void handleShowAllDiets(User user, DietManagerImpl dietManager) {
        System.out.println("\n--- [ 내 전체 식단 목록 ] ---");
        Diet[] list = dietManager.getDietList();

        if (list == null || list.length == 0) {
            System.out.println("등록된 식단 기록이 없습니다.");
            return;
        }

        for (Diet d : list) {
            if (d.getUser() != null && d.getUser().getId().equals(user.getId())) {
                System.out.println("========================================");
                System.out.println("기록 ID: " + d.getDietId() + " | 날짜: " + d.getDietDate());
                System.out.println(" - 아침: " + String.join(", ", d.getMorningMenus()));
                System.out.println(" - 점심: " + String.join(", ", d.getLunchMenus()));
                System.out.println(" - 저녁: " + String.join(", ", d.getDinnerMenus()));
            }
        }
    }

    // 날짜 검색
    private void handleSearchDietByDate(DietManagerImpl dietManager) {
        System.out.println("\n--- [ 날짜별 식단 검색 ] ---");
        System.out.print("조회할 날짜를 입력하세요 (예: 2026-07-30) : ");
        String dateInput = sc.nextLine().trim();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        try {
            Date searchDate = sdf.parse(dateInput);
            Diet[] results = dietManager.searchByDietDate(searchDate);
            User loginUser = userManager.getMyInfo();

            System.out.println("\n>>> " + dateInput + " 식단 검색 결과 <<<");
            boolean found = false;

            for (Diet d : results) {
                if (d.getUser() != null && d.getUser().getId().equals(loginUser.getId())) {
                    found = true;
                    System.out.println("----------------------------------------");
                    System.out.println("식단 ID: " + d.getDietId());
                    System.out.println(" - 아침: " + (d.getMorningMenus().isEmpty() ? "없음" : String.join(", ", d.getMorningMenus())));
                    System.out.println(" - 점심: " + (d.getLunchMenus().isEmpty() ? "없음" : String.join(", ", d.getLunchMenus())));
                    System.out.println(" - 저녁: " + (d.getDinnerMenus().isEmpty() ? "없음" : String.join(", ", d.getDinnerMenus())));
                }
            }

            if (!found) {
                System.out.println("[안내] 해당 날짜에 " + loginUser.getName() + "님의 식단 기록이 없습니다.");
            }

        } catch (ParseException e) {
            System.out.println("[오류] 날짜 형식이 올바르지 않습니다. (yyyy-MM-dd 형식으로 입력해주세요)");
        } catch (DietDateNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
    // [F111] 다른 회원 팔로우 입출력 처리
    private void handleFollow() {
        System.out.println("\n--- [ 다른 회원 팔로우 ] ---");
        System.out.print("팔로우할 회원의 아이디를 입력하세요 : ");
        String targetId = sc.nextLine().trim();
        
        if (targetId.isEmpty()) {
            System.out.println("[오류] 아이디를 입력해주세요.");
            return;
        }
        
        User loginUser = userManager.getMyInfo();
        if (loginUser != null) {
            userManager.follow(loginUser.getId(), targetId);
        } else {
            System.out.println("[오류] 로그인 정보를 찾을 수 없습니다.");
        }
    }
}