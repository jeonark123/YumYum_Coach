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
import com.google.gson.reflect.TypeToken;

import domain.Diet;
import exception.DietDateNotFoundException;

import java.util.Date;



public class DietManagerImpl implements DietManager {
	
	private List<Diet> dietList = new ArrayList<>();
	private final int MAX_SIZE = 100;
	private static DietManagerImpl dm;
	
	// 저장할 파일 경로 및 파일명 지정
//	private final String DIET_FILE_PATH = "diets.txt";
	private File file = new File("diets.json");
	private Gson gson = new Gson();
	
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
		if(dietList.size() < MAX_SIZE) {
			dietList.add(diet);
		}else {
			System.out.println("더 이상 식단을 등록할 수 없습니다.");
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
	public void saveData() {
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
			bw.write(gson.toJson(dietList));
			System.out.println("저장완료");
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
