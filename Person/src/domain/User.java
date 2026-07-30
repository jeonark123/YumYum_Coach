package domain;

public class User {
    private String id;        
    private String password;
    private String name;
    private double height;
    private double weight;    
    private String disease;   
    private boolean isActive; 
    
   
    public User() {
        this.isActive = true; 
    }

    // 모든 필드를 초기화하는 생성자
    public User(String id, String password, String name, double height, double weight, String disease) {
        super();
        this.id = id;
        this.password = password;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.disease = disease;
        this.isActive = true; 
    }

    // Getter & Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", password=" + password + ", name=" + name 
                + ", height=" + height + ", weight=" + weight 
                + ", disease=" + disease + ", isActive=" + isActive + "]";
    }
}