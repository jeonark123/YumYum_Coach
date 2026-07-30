package exception;

public class DietDateNotFoundException extends Exception {
    public DietDateNotFoundException(String dateStr) {
        super("[경고] " + dateStr + " 날짜에 등록된 식단 기록이 없습니다.");
    }
}