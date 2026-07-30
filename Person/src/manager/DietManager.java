package manager;

import java.util.Date;

import domain.Diet;

public interface DietManager {
	
	// 식단 추가
	public abstract void addDiet(Diet diet);
	
	// 등록된 식단 리스트 출력
	public abstract Diet[] getDietList();
	
	// 식단 기록 검색 : 날짜로	
	public abstract Diet[] searchByDietDate(Date dietDate);
//	Diet[] searchByditeDate(Date dietDate) throws DietDateNotFoundException; 
	// 해당 날짜에 없는 경우 예외 처리할 것!!
	
	// 총 칼로리 계산
//	int getTotalKcal();
	
	// 저장
	public abstract void saveData();
	
	// 불러오기
	public abstract void loadData();
	

}
