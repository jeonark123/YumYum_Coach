package domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Diet {
	private int dietId;
	private User user; // User 패키지 위치 확인하기
//	private String morningMenu;
//	private String lunchMenu;
//	private String dinnerMenu;
	// 사용자가 입력한 메뉴명들의 리스트
    private List<String> morningMenus = new ArrayList<>();
    private List<String> lunchMenus = new ArrayList<>();
    private List<String> dinnerMenus = new ArrayList<>();
	private Date dietdate;
	
	
	// 기본 생성자
	public Diet() {
	}

	public Diet(int dietId, User user, String morningMenu, String lunchMenu, String dinnerMenu, Date dietdate) {
		super();
		this.dietId = dietId;
		this.user = user;
//		this.morningMenu = morningMenu;
//		this.lunchMenu = lunchMenu;
//		this.dinnerMenu = dinnerMenu;
		this.morningMenus = morningMenus;
        this.lunchMenus = lunchMenus;
        this.dinnerMenus = dinnerMenus;
        this.dietdate = dietdate;
		
	}

	public int getDietId() {
		return dietId;
	}

	public void setDietId(int dietId) {
		this.dietId = dietId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<String> getMorningMenus() {
		return morningMenus;
	}

	public void setMorningMenus(List<String> morningMenus) {
		this.morningMenus = morningMenus;
	}

	public List<String> getLunchMenus() {
		return lunchMenus;
	}

	public void setLunchMenus(List<String> lunchMenus) {
		this.lunchMenus = lunchMenus;
	}

	public List<String> getDinnerMenus() {
		return dinnerMenus;
	}

	public void setDinnerMenus(List<String> dinnerMenus) {
		this.dinnerMenus = dinnerMenus;
	}

	public Date getDietdate() {
		return dietdate;
	}

	public void setDietdate(Date dietdate) {
		this.dietdate = dietdate;
	}

	// Getter & Setter
	
	
}