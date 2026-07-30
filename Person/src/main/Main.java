package main;

import ui.UserMenu;

public class Main {
    public static void main(String[] args) {
        // // UI 메뉴 객체를 생성하고 회원 관리 콘솔 프로그램을 시작
        UserMenu userMenu = new UserMenu();
        userMenu.displayMenu();
    }
}