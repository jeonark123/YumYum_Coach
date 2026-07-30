package manager;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import domain.Diet;
import domain.User;
import exception.DietDateNotFoundException;

import java.util.Date;



public class DietManagerImpl implements DietManager {
	
	private List<Diet> dietList = new ArrayList<>();
	private final int MAX_SIZE = 100;
	private static DietManagerImpl dm;
	
	// 저장할 파일 경로 및 파일명 지정
//	private final String DIET_FILE_PATH = "diets.txt";
	private File file = new File("diets.json");
//	private Gson gson = new Gson();
	private Gson gson = new GsonBuilder()
		    .setDateFormat("yyyy-MM-dd") // 날짜를 연-월-일 형식으로만 다루도록 지정
		    .create();
	
	public DietManagerImpl() {
		loadData();
	}
	
	public static DietManagerImpl getInstance() {
		if(dm == null) {
			dm = new DietManagerImpl();
		}
		return dm;
	}
	
	@Override
	public void addDiet(Diet diet) {
		// 1. 매개변수 유효성 검사
	    if (diet == null || diet.getUser() == null || diet.getDietDate() == null) {
	        System.out.println("⚠️ 유효하지 않은 식단 정보입니다.");
	        return;
	    }

	    // 날짜를 YYYY-MM-DD 형식의 문자열로 변환하는 포맷터 (시간 제거)
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String targetDateStr = sdf.format(diet.getDietDate());

	    // 2. 같은 사용자 & 같은 날짜(시간 제외) 중복 체크
	    for (Diet existingDiet : dietList) {
	        if (existingDiet != null && existingDiet.getUser() != null && existingDiet.getDietDate() != null) {
	            
	            boolean isSameUser = existingDiet.getUser().getId().equals(diet.getUser().getId());
	            
	            // 기존 등록된 날짜도 YYYY-MM-DD 형태로 변환해서 비교
	            String existingDateStr = sdf.format(existingDiet.getDietDate());
	            boolean isSameDate = existingDateStr.equals(targetDateStr);

	            if (isSameUser && isSameDate) {
	                System.out.println("⚠️ 이미 해당 날짜(" + targetDateStr + ")에 등록된 식단이 존재합니다.");
	                System.out.println("기존 식단을 수정하거나 삭제 후 다시 시도해주세요.");
	                return; // 등록 취소 및 종료
	            }
	        }
	    }

	    // 3. 리스트 용량 체크 및 등록
	    if (dietList.size() < MAX_SIZE) {
	        dietList.add(diet);
	        System.out.println("✅ 식단이 성공적으로 등록되었습니다.");
	    } else {
	        System.out.println("⚠️ 더 이상 식단을 등록할 수 없습니다. (최대 용량 초과)");
	    }
		
	}
	
	@Override
	public Diet[] getDietList() {
		
		Diet[] res = new Diet[dietList.size()];
		return this.dietList.toArray(res);
	}
	
	@Override
	public Diet[] searchByDietDate(Date dietDate) throws DietDateNotFoundException{
		List<Diet> dietListByDate = new ArrayList<>();

        
//		for(int i = 0; i < dietList.size();i++) {
//			if(dietList.get(i).getDietDate().equals(dietDate)) {
//				dietListByDate.add(dietList.get(i));
//			}
//		}
//		
////		// 해당 날짜가 없는 경우에 예외를 던진다 !!!
////		if(dietListByDate.size() == 0)
////			throw new DietDateNotFoundException(dietDate);
/// 
/// // Date 객체를 'yyyy-MM-dd' 형태의 문자열로 변환하여 비교
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String targetDateStr = sdf.format(dietDate);

        for (int i = 0; i < dietList.size(); i++) {
            Diet diet = dietList.get(i);
            if (diet.getDietDate() != null) {
                String dietDateStr = sdf.format(diet.getDietDate());
                if (dietDateStr.equals(targetDateStr)) {
                    dietListByDate.add(diet);
                }
            }
        }

        // 해당 날짜의 식단이 없는 경우 예외를 던집니다.
        if (dietListByDate.isEmpty()) {
            throw new DietDateNotFoundException(targetDateStr);
        }
	
		Diet[] res = new Diet[dietListByDate.size()];
		
		return dietListByDate.toArray(res);
	
	
	}
	
	@Override
	public boolean updateDiet(User user, Date dietDate, Diet newDiet) {
		if (user == null || dietDate == null || newDiet == null) {
			System.out.println("⚠️ 유효하지 않은 수정 요청입니다.");
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String targetDateStr = sdf.format(dietDate);

		for (Diet existingDiet : dietList) {
			if (existingDiet != null && existingDiet.getUser() != null && existingDiet.getDietDate() != null) {
				// 1. 동일 사용자 확인
				boolean isSameUser = existingDiet.getUser().getId().equals(user.getId());
				
				// 2. 동일 날짜(YYYY-MM-DD) 확인
				String existingDateStr = sdf.format(existingDiet.getDietDate());
				boolean isSameDate = existingDateStr.equals(targetDateStr);

				if (isSameUser && isSameDate) {
					// 3. 아침, 점심, 저녁 식단 데이터 덮어쓰기 (업데이트)
					existingDiet.setMorningMenus(newDiet.getMorningMenus());
					existingDiet.setLunchMenus(newDiet.getLunchMenus());
					existingDiet.setDinnerMenus(newDiet.getDinnerMenus());
					
					// 4. 변경사항 파일에 저장
					saveData();
					return true;
				}
			}
		}

		return false; // 해당 사용자의 날짜별 식단 기록을 찾지 못한 경우
	}
	
	
	@Override
	public void saveData() {
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
			bw.write(gson.toJson(dietList));
//			System.out.println("저장완료");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void loadData() {
		//파일에서 프로그램으로 데이터를 불러온다 (입력)
		if(file.exists()) {
			try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
				String line;
				StringBuilder sb = new StringBuilder();
				while((line = br.readLine()) != null	) {
					sb.append(line);
				}
				
//				sb -> JSON 파일을 문자열로 바꾼 형태
				Type dietListType = new TypeToken<ArrayList<Diet>>() {
				}.getType();

				dietList = gson.fromJson(sb.toString(), dietListType);
	            if (dietList == null) {
	                dietList = new ArrayList<>();
	            }
				
				
				
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}//파일이 존재하면 이거 수행하자
			
	}
	
	
//	@Override
//	public int getTotalKcal() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
	
	


}
