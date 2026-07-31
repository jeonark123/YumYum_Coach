package ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import domain.Diet;
import domain.DietKcalInfo;
import domain.User;
import exception.DietDateNotFoundException;
import manager.DietKcalManager;
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
            System.out.println("1. 오늘 식단 등록");
            System.out.println("2. 내 전체 식단 조회 (총 칼로리 포함)");
            System.out.println("3. 날짜별 식단 검색");
            System.out.println("4. 원하는 날짜 식단 수정(등록)");
            System.out.println("5. 원하는 날짜 식단 삭제");
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
                case "4":
                	handleUpdateDiet(loginUser, dietManager);
                	break;
                case "5":
                	handleDeleteDietByDate(loginUser, dietManager);
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
//        String morningInput = sc.nextLine();
//        for (String m : morningInput.split(",")) {
//            if (!m.trim().isEmpty()) diet.getMorningMenus().add(m.trim());
//        }
        processMealInput(sc.nextLine(), diet.getMorningMenus());

        System.out.print("점심 메뉴 (쉼표로 구분 예: 닭가슴살,샐러드) : ");
//        String lunchInput = sc.nextLine();
//        for (String m : lunchInput.split(",")) {
//            if (!m.trim().isEmpty()) diet.getLunchMenus().add(m.trim());
//        }
        processMealInput(sc.nextLine(), diet.getLunchMenus());

        System.out.print("저녁 메뉴 (쉼표로 구분 예: 샐러드) : ");
//        String dinnerInput = sc.nextLine();
//        for (String m : dinnerInput.split(",")) {
//            if (!m.trim().isEmpty()) diet.getDinnerMenus().add(m.trim());
//        }
        processMealInput(sc.nextLine(), diet.getDinnerMenus());

        dietManager.addDiet(diet);
        dietManager.saveData();
//        System.out.println("[안내] 식단이 successfully 저장되었습니다.");
    }

    // 2. 전체 식단 및 칼로리 출력
    // //버전1
//    private void handleShowAllDiets(User user, DietManagerImpl dietManager) {
//        System.out.println("\n--- [ 내 전체 식단 목록 ] ---");
//        Diet[] list = dietManager.getDietList();
//
//        if (list == null || list.length == 0) {
//            System.out.println("등록된 식단 기록이 없습니다.");
//            return;
//        }
//
//        for (Diet d : list) {
//            if (d.getUser() != null && d.getUser().getId().equals(user.getId())) {
//                System.out.println("========================================");
//                System.out.println("기록 ID: " + d.getDietId() + " | 날짜: " + d.getDietDate());
//                System.out.println(" - 아침: " + String.join(", ", d.getMorningMenus()));
//                System.out.println(" - 점심: " + String.join(", ", d.getLunchMenus()));
//                System.out.println(" - 저녁: " + String.join(", ", d.getDinnerMenus()));
//            }
//        }
//    }
//    // 버전 2
//    private void handleShowAllDiets(User user, DietManagerImpl dietManager) {
//        System.out.println("\n--- [ 내 전체 식단 목록 ] ---");
//        Diet[] list = dietManager.getDietList();
//
//        if (list == null || list.length == 0) {
//            System.out.println("등록된 식단 기록이 없습니다.");
//            return;
//        }
//
//        boolean hasMyDiet = false;
//
//        for (Diet d : list) {
//            if (d.getUser() != null && d.getUser().getId().equals(user.getId())) {
//                hasMyDiet = true;
//
//                System.out.println("========================================");
//                System.out.println("기록 ID: " + d.getDietId() + " | 날짜: " + d.getDietDate());
//
//                // JSON DB와 연동된 포맷팅 사용
//                System.out.println(" - 아침: " + formatMenuListWithNutrients(d.getMorningMenus()));
//                System.out.println(" - 점심: " + formatMenuListWithNutrients(d.getLunchMenus()));
//                System.out.println(" - 저녁: " + formatMenuListWithNutrients(d.getDinnerMenus()));
//            }
//        }
//
//        if (!hasMyDiet) {
//            System.out.println("등록된 식단 기록이 없습니다.");
//        }
//    }
    
//    // 버전3
//    private void handleShowAllDiets(User user, DietManagerImpl dietManager) {
//        System.out.println("\n--- [ 내 전체 식단 목록 ] ---");
//        Diet[] list = dietManager.getDietList();
//
//        if (list == null || list.length == 0) {
//            System.out.println("등록된 식단 기록이 없습니다.");
//            return;
//        }
//
//        boolean hasMyDiet = false;
//
//        for (Diet d : list) {
//            if (d.getUser() != null && d.getUser().getId().equals(user.getId())) {
//                hasMyDiet = true;
//
//                System.out.println("========================================");
//                System.out.println("기록 ID: " + d.getDietId() + " | 날짜: " + d.getDietDate());
//
//                // 1. 아침, 점심, 저녁 메뉴별 상세 출력
//                System.out.println(" - 아침: " + formatMenuListWithNutrients(d.getMorningMenus()));
//                System.out.println(" - 점심: " + formatMenuListWithNutrients(d.getLunchMenus()));
//                System.out.println(" - 저녁: " + formatMenuListWithNutrients(d.getDinnerMenus()));
//
//                // 2. 각 끼니별 총 칼로리 계산
//                double morningKcal = DietKcalManager.calculateTotalKcal(d.getMorningMenus());
//                double lunchKcal = DietKcalManager.calculateTotalKcal(d.getLunchMenus());
//                double dinnerKcal = DietKcalManager.calculateTotalKcal(d.getDinnerMenus());
//
//                // 3. 하루 전체 총 칼로리 합계 계산
//                double dailyTotalKcal = morningKcal + lunchKcal + dinnerKcal;
//
//                // 4. 총 칼로리 출력 (소수점 없이 정수로 표시)
//                System.out.println("----------------------------------------");
//                System.out.printf("🔥 하루 총 섭취 칼로리: %,d kcal\n", (int) dailyTotalKcal);
//            }
//        }
//
//        if (!hasMyDiet) {
//            System.out.println("등록된 식단 기록이 없습니다.");
//        }
//    }
    
    private void handleShowAllDiets(User user, DietManagerImpl dietManager) {
        System.out.println("  📊  " + user.getName() + "님의 전체 식단 목록");
        System.out.println("────────────────────────────────────────────────────────");
        
        Diet[] list = dietManager.getDietList();

        if (list == null || list.length == 0) {
            System.out.println("등록된 식단 기록이 없습니다.");
            return;
        }

        boolean hasMyDiet = false;

        // 날짜를 보기 좋게 출력하기 위한 포맷터 (2026-07-31 (금) 형식)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd (E)", Locale.KOREAN);

        for (Diet d : list) {
            if (d.getUser() != null && d.getUser().getId().equals(user.getId())) {
                hasMyDiet = true;

                String formattedDate = (d.getDietDate() != null) ? dateFormat.format(d.getDietDate()) : String.valueOf(d.getDietDate());

                System.out.println("\n────────────────────────────────────────────────────────");
                System.out.printf("│ 📅 식단 날짜 : %-22s (기록 ID: %-4d) \n", formattedDate, d.getDietId());
                System.out.println("────────────────────────────────────────────────────────");

                // 1. 아침, 점심, 저녁 메뉴별 상세 출력
                System.out.println("│ 🌅 아침: " + formatMenuListWithNutrients(d.getMorningMenus()));
                System.out.println("│ ☀️ 점심: " + formatMenuListWithNutrients(d.getLunchMenus()));
                System.out.println("│ 🌙 저녁: " + formatMenuListWithNutrients(d.getDinnerMenus()));

                // 2. 각 끼니별 총 칼로리 계산
                double morningKcal = DietKcalManager.calculateTotalKcal(d.getMorningMenus());
                double lunchKcal = DietKcalManager.calculateTotalKcal(d.getLunchMenus());
                double dinnerKcal = DietKcalManager.calculateTotalKcal(d.getDinnerMenus());

                // 3. 하루 전체 총 칼로리 합계 계산
                double dailyTotalKcal = morningKcal + lunchKcal + dinnerKcal;

                // 4. 총 칼로리 출력
                System.out.println("────────────────────────────────────────────────────────");
                System.out.printf("│ 🔥 하루 총 섭취 칼로리: %,d kcal\n", (int) dailyTotalKcal);
                System.out.println("────────────────────────────────────────────────────────");
            }
        }

        if (!hasMyDiet) {
            System.out.println("등록된 식단 기록이 없습니다.");
        }
    }
    
 // 5. 날짜별 식단 삭제
    private void handleDeleteDietByDate(User user, DietManagerImpl dietManager) {
        System.out.println("\n--- [ 날짜별 식단 삭제 ] ---");
        System.out.print("삭제할 식단의 날짜를 입력하세요 (예: 2026-07-30) : ");
        String dateInput = sc.nextLine().trim();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        try {
            Date targetDate = sdf.parse(dateInput);

            // 해당 날짜에 본인의 식단 기록이 존재하는지 사전 확인
            boolean exists = false;
            try {
                Diet[] results = dietManager.searchByDietDate(targetDate);
                for (Diet d : results) {
                    if (d.getUser() != null && d.getUser().getId().equals(user.getId())) {
                        exists = true;
                        break;
                    }
                }
            } catch (DietDateNotFoundException e) {
                exists = false;
            }

            if (!exists) {
                System.out.println("[안내] 해당 날짜(" + dateInput + ")에 등록된 식단 기록이 없습니다.");
                return;
            }

            // 진짜 삭제할 것인지 사용자 재확인
            System.out.print("정말로 " + dateInput + " 날짜의 식단을 삭제하시겠습니까? (Y/N) : ");
            String confirm = sc.nextLine().trim();

            if (confirm.equalsIgnoreCase("Y")) {
                // DietManager의 삭제 메서드 호출 (구현된 manager 메서드 명에 맞춰 전달)
                boolean isDeleted = dietManager.deleteDiet(user, targetDate);

                if (isDeleted) {
                    System.out.println("[안내] " + dateInput + " 날짜의 식단이 성공적으로 삭제되었습니다.");
                } else {
                    System.out.println("[오류] 식단 삭제 처리에 실패했습니다.");
                }
            } else {
                System.out.println("[안내] 삭제가 취소되었습니다.");
            }

        } catch (ParseException e) {
            System.out.println("[오류] 날짜 형식이 올바르지 않습니다. (yyyy-MM-dd 형식으로 입력해주세요)");
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


            System.out.println("\n  🔍  " + dateInput + " 식단 검색 결과");
            System.out.println("────────────────────────────────────────────────────────");boolean found = false;
         // 날짜를 보기 좋게 출력하기 위한 포맷터 (2026-07-30 (목) 형식)
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd (E)", Locale.KOREAN); 
            
            
            for (Diet d : results) {
                if (d.getUser() != null && d.getUser().getId().equals(loginUser.getId())) {
                    found = true;
                    String formattedDate = (d.getDietDate() != null) ? dateFormat.format(d.getDietDate()) : dateInput;
                    
                    System.out.println("\n────────────────────────────────────────────────────────");
                    System.out.printf("│ 📅 식단 날짜 : %-22s (기록 ID: %-4d) \n", formattedDate, d.getDietId());
                    System.out.println("────────────────────────────────────────────────────────");
                 // 영양 성분이 포함된 포맷터 적용
                    System.out.println("│ 🌅 아침: " + formatMenuListWithNutrients(d.getMorningMenus()));
                    System.out.println("│ ☀️ 점심: " + formatMenuListWithNutrients(d.getLunchMenus()));
                    System.out.println("│ 🌙 저녁: " + formatMenuListWithNutrients(d.getDinnerMenus()));

                    // 해당 날짜의 총 칼로리 계산 및 출력
                    double morningKcal = DietKcalManager.calculateTotalKcal(d.getMorningMenus());
                    double lunchKcal = DietKcalManager.calculateTotalKcal(d.getLunchMenus());
                    double dinnerKcal = DietKcalManager.calculateTotalKcal(d.getDinnerMenus());
                    double dailyTotalKcal = morningKcal + lunchKcal + dinnerKcal;

                    System.out.println("────────────────────────────────────────────────────────");
                    System.out.printf("│ 🔥 하루 총 섭취 칼로리: %,d kcal\n", (int) dailyTotalKcal);
                    System.out.println("────────────────────────────────────────────────────────");
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
    
 // 4. 지난 날짜 식단 수정 및 신규 등록
    private void handleUpdateDiet(User user, DietManagerImpl dietManager) {
        System.out.println("\n--- [ 지난 날짜 식단 수정 / 등록 ] ---");
        System.out.print("수정 또는 등록할 날짜를 입력하세요 (예: 2026-07-30) : ");
        String dateInput = sc.nextLine().trim();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        try {
            Date targetDate = sdf.parse(dateInput);

            // 해당 날짜에 등록된 식단 데이터가 있는지 미리 확인
            boolean exists = false;
            try {
                Diet[] results = dietManager.searchByDietDate(targetDate);
                for (Diet d : results) {
                    if (d.getUser() != null && d.getUser().getId().equals(user.getId())) {
                        exists = true;
                        break;
                    }
                }
            } catch (DietDateNotFoundException e) {
                exists = false;
            }

            // 등록된 식단이 없을 경우 안내 문구 출력
            if (!exists) {
                System.out.println("[안내] 해당 날짜(" + dateInput + ")에 등록된 식단이 없으므로 신규 등록합니다!");
            }

            // 새 식단 객체 생성
            Diet dietData = new Diet();
            dietData.setDietId((int) (System.currentTimeMillis() % 10000));
            dietData.setUser(user);
            dietData.setDietDate(targetDate);

            // 메뉴 입력받기 (아침, 점심, 저녁 중 최소 하나 이상 입력될 때까지 반복)
            while (true) {
                System.out.println("\n식단 메뉴를 입력해주세요. (최소 하나의 끼니에는 메뉴를 입력해야 합니다)");

                System.out.print("아침 메뉴 (쉼표로 구분 예: 밥,계란후라이) : ");
                String morningInput = sc.nextLine();
                for (String m : morningInput.split(",")) {
                    if (!m.trim().isEmpty()) dietData.getMorningMenus().add(m.trim());
                }

                System.out.print("점심 메뉴 (쉼표로 구분 예: 닭가슴살,샐러드) : ");
                String lunchInput = sc.nextLine();
                for (String m : lunchInput.split(",")) {
                    if (!m.trim().isEmpty()) dietData.getLunchMenus().add(m.trim());
                }

                System.out.print("저녁 메뉴 (쉼표로 구분 예: 샐러드) : ");
                String dinnerInput = sc.nextLine();
                for (String m : dinnerInput.split(",")) {
                    if (!m.trim().isEmpty()) dietData.getDinnerMenus().add(m.trim());
                }

                // 입력된 메뉴가 하나라도 있는지 검증
                boolean isMorningEmpty = dietData.getMorningMenus().isEmpty();
                boolean isLunchEmpty = dietData.getLunchMenus().isEmpty();
                boolean isDinnerEmpty = dietData.getDinnerMenus().isEmpty();

                if (isMorningEmpty && isLunchEmpty && isDinnerEmpty) {
                    System.out.println("\n[오류] 아무런 메뉴도 입력되지 않아 저장할 수 없습니다! 식단 정보를 다시 입력해주세요.");
                    // 리스트 초기화 후 다시 입력받기
                    dietData.getMorningMenus().clear();
                    dietData.getLunchMenus().clear();
                    dietData.getDinnerMenus().clear();
                } else {
                    break; // 유효한 입력이 들어왔으므로 반복문 탈출
                }
            }

            // 기존 식단이 있으면 수정(updateDiet), 없으면 신규 추가(addDiet)
            if (exists) {
                boolean isUpdated = dietManager.updateDiet(user, targetDate, dietData);
                if (isUpdated) {
                    System.out.println("\n[안내] " + dateInput + " 날짜의 식단이 성공적으로 수정되었습니다!");
                } else {
                    System.out.println("\n[오류] 식단 수정에 실패했습니다.");
                }
            } else {
                dietManager.addDiet(dietData);
                dietManager.saveData();
                System.out.println("\n[안내] " + dateInput + " 날짜에 새로운 식단이 성공적으로 등록되었습니다!");
            }

        } catch (ParseException e) {
            System.out.println("[오류] 날짜 형식이 올바르지 않습니다. (yyyy-MM-dd 형식으로 입력해주세요)");
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
    
    /**
     * 입력된 메뉴 문자열을 쉼표 기준으로 파싱하고, DB에 없는 메뉴는 즉시 사용자에게 영양정보를 입력받아 등록합니다.
     */
    private void processMealInput(String input, List<String> targetList) {
        if (input == null || input.trim().isEmpty()) return;

        String[] rawMenus = input.split(",");
        for (String raw : rawMenus) {
            String menuName = raw.trim();
            if (menuName.isEmpty()) continue;

            // DB에서 존재 여부 검증
            DietKcalInfo info = DietKcalManager.findByFoodName(menuName);

            if (info != null) {
                // DB에 존재하는 경우 그대로 추가
                targetList.add(menuName);
            } else {
                // DB에 없는 메뉴 처리 흐름
                while (true) {
                    System.out.println("  [!] '" + menuName + "'은(는) DB에 영양정보가 없는 음식입니다.");
                    System.out.print("      [1] 직접 영양정보 입력 후 저장  [2] 메뉴 등록 취소 >> ");
                    String choice = sc.nextLine().trim();

                    if ("1".equals(choice)) {
                        try {
                            System.out.print("      - 칼로리(kcal): ");
                            double kcal = Double.parseDouble(sc.nextLine().trim());
                            System.out.print("      - 탄수화물(g): ");
                            double carbo = Double.parseDouble(sc.nextLine().trim());
                            System.out.print("      - 단백질(g): ");
                            double protein = Double.parseDouble(sc.nextLine().trim());
                            System.out.print("      - 지방(g): ");
                            double fat = Double.parseDouble(sc.nextLine().trim());

                            // DietKcalManager를 통해 메모리 및 json 파일에 저장
                            DietKcalManager.saveCustomNutrient(menuName, kcal, carbo, protein, fat);
                            targetList.add(menuName);
                            System.out.println("  [성공] '" + menuName + "'의 영양정보가 DB에 새로 추가되었습니다!");
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("  [오류] 숫자만 입력할 수 있습니다. 다시 입력해주세요.");
                        }
                    } else if ("2".equals(choice)) {
                        System.out.println("  [안내] '" + menuName + "' 메뉴 등록을 스킵합니다.");
                        break;
                    } else {
                        System.out.println("  [오류] 1번 또는 2번을 선택해주세요.");
                    }
                }
            }
        }
    }
    
    
    /**
     * 메뉴 이름 목록을 받아서 "음식명(칼로리: ~kcal, 탄수화물: ~g, 단백질: ~g, 지방: ~g)" 형태로 포맷팅합니다.
     */
    private String formatMenuListWithNutrients(List<String> menuList) {
        if (menuList == null || menuList.isEmpty()) {
            return "없음";
        }

        List<String> formattedMenus = new ArrayList<>();

        for (String menu : menuList) {
            if (menu == null || menu.trim().isEmpty()) continue;

            String trimmedMenu = menu.trim();
            // JSON DB에서 검색
            DietKcalInfo info = DietKcalManager.findByFoodName(trimmedMenu);

            if (info != null) {
                String formatted = String.format("%s(칼로리: %.0fkcal, 탄수화물: %.1fg, 단백질: %.1fg, 지방: %.1fg)",
                        info.getFoodName(), // DTO의 Getter명에 맞게 조정
                        info.getKcal(),
                        info.getCarbohydrate(),
                        info.getProtein(),
                        info.getFat());
                formattedMenus.add(formatted);
            } else {
                // DB에 없는 음식일 경우 안전하게 표시
                formattedMenus.add(trimmedMenu + "(정보없음)");
            }
        }

        return String.join(", ", formattedMenus);
    }
}