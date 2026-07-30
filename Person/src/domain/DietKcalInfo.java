package domain;

public class DietKcalInfo {
    private String foodId;      // 식품코드 (예: D101-004160000-0001)
    private String menuName;    // 식품명
    private double kcal;        // 에너지(kcal)
    private double carbohydrate;// 탄수화물(g)
    private double protein;     // 단백질(g)
    private double fat;         // 지방(g)

    public DietKcalInfo() {}

    public DietKcalInfo(String foodId, String menuName, double kcal, double carbohydrate, double protein, double fat) {
        this.foodId = foodId;
        this.menuName = menuName;
        this.kcal = kcal;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
    }

    public String getFoodId() { return foodId; }
    public void setFoodId(String foodId) { this.foodId = foodId; }

    public String getMenuName() { return menuName; }
    public void setMenuName(String menuName) { this.menuName = menuName; }

    public double getKcal() { return kcal; }
    public void setKcal(double kcal) { this.kcal = kcal; }

    public double getCarbohydrate() { return carbohydrate; }
    public void setCarbohydrate(double carbohydrate) { this.carbohydrate = carbohydrate; }

    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }

    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }
}