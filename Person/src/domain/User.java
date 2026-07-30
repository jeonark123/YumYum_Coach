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
    
    
 // 객체 데이터를 파일에 한 줄로 저장하기 위해 쉼표(,) 구분 문자열로 변환
    public String toFileString() {
        return id + "," + password + "," + name + "," + height + "," + weight + "," + disease + "," + isActive;
    }

    // 파일에서 읽은 문자열 한 줄을 파싱하여 다시 User 객체로 복원
    public static User fromFileString(String line) {
        String[] parts = line.split(",");
        // 잘못된 데이터 형식의 줄은 무시하기 위한 방어 코드
        if (parts.length < 7) return null;

        String id = parts[0];
        String password = parts[1];
        String name = parts[2];
        double height = Double.parseDouble(parts[3]);
        double weight = Double.parseDouble(parts[4]);
        String disease = parts[5];
        boolean isActive = Boolean.parseBoolean(parts[6]);

        User user = new User(id, password, name, height, weight, disease);
        user.setActive(isActive);
        return user;
    }
}