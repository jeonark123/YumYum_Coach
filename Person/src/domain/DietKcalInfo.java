package domain;

import com.google.gson.annotations.SerializedName;

public class DietKcalInfo {
    @SerializedName("식품코드")
    private String foodCode;

    @SerializedName("식품명")
    private String foodName;

    @SerializedName("에너지(kcal)")
    private double kcal;

    @SerializedName("탄수화물(g)")
    private double carbohydrate;

    @SerializedName("단백질(g)")
    private double protein;

    @SerializedName("지방(g)")
    private double fat;

    public String getFoodCode() {
        return foodCode;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getKcal() {
        return kcal;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    @Override
    public String toString() {
        return foodName + " (" + kcal + " kcal)";
    }
}
