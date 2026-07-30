package manager;

import java.util.ArrayList;
import domain.Diet;
import java.util.Date;
import java.util.List;

public class DietManagerImpl implements DietManager {
	
	private List<Diet> dietList = new ArrayList<>();
	private final int MAX_SIZE = 100;
	private static DietManagerImpl dm = new DietManagerImpl();
	
	public DietManagerImpl() {
	}
	
	public static DietManagerImpl getInstance() {
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
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Diet[] searchByDietDate(Date dietDate) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getTotalKcal() {
		// TODO Auto-generated method stub
		return 0;
	}


}
