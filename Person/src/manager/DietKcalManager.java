package manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import domain.DietKcalInfo;

public class DietKcalManager {
	private static Map<String, DietKcalInfo> kcalMap = new HashMap<>();
	private static final String JSON_FILE_PATH = "food_kcal.json";
	
	static {
		kcalLoadData();
	}

	public static void kcalLoadData() {
		try (BufferedReader br = new BufferedReader(new FileReader(JSON_FILE_PATH))) {
		    Gson gson = new Gson();

		    List<DietKcalInfo> list = gson.fromJson(
		        br,
		        new TypeToken<List<DietKcalInfo>>() {}.getType()
		    );

            kcalMap.clear();

            for (DietKcalInfo info : list) {
                if (info == null || info.getFoodName() == null) {
                    continue;
                }

                kcalMap.put(normalize(info.getFoodName()), info);
            }

//            System.out.println("칼로리 DB 로딩 완료: " + kcalMap.size() + "개");
        } catch (IOException e) {
            System.out.println("칼로리 DB 파일을 읽을 수 없습니다: " + JSON_FILE_PATH);
        }
    }

	public static DietKcalInfo findByFoodName(String foodName) {
	    if (foodName == null) {
	        return null;
	    }

	    String key = normalize(foodName);
	    return kcalMap.get(key);
	}


	private static String normalize(String text) {
	    return text.trim().replace(" ", "");
	}
	
	public static double calculateTotalKcal(List<String> menuList) {
	    if (menuList == null || menuList.isEmpty()) {
	        return 0.0;
	    }

	    double totalKcal = 0.0;

	    for (String menu : menuList) {
	        if (menu == null || menu.trim().isEmpty()) continue;
	        
	        DietKcalInfo info = findByFoodName(menu.trim());
	        if (info != null) {
	            totalKcal += info.getKcal();
	        }
	    }

	    return totalKcal;
	}
	
	/**
     * DB에 없는 새로운 영양정보를 메모리 Map에 추가하고 JSON 파일에 영구 저장합니다.
     */
    public static void saveCustomNutrient(String foodName, double kcal, double carbo, double protein, double fat) {
        DietKcalInfo newInfo = new DietKcalInfo();
        newInfo.setFoodName(foodName.trim());
        newInfo.setKcal(kcal);
        newInfo.setCarbohydrate(carbo);
        newInfo.setProtein(protein);
        newInfo.setFat(fat);

        // 1. 메모리 Map 업데이트
        kcalMap.put(normalize(foodName), newInfo);

        // 2. JSON 파일에 전체 목록 다시 쓰기
        try (FileWriter writer = new FileWriter(JSON_FILE_PATH)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<DietKcalInfo> currentList = new ArrayList<>(kcalMap.values());
            gson.toJson(currentList, writer);
        } catch (IOException e) {
            System.out.println("[오류] 새로운 영양정보를 파일에 저장하는 중 오류 발생: " + e.getMessage());
        }
    }

}
