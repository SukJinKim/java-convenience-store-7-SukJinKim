package store.view;

import store.dto.Inventory;

public class OutputView {
    private static final String HELLO = "안녕하세요. W편의점입니다.";
    private static final String GUIDE_INVENTORY = "현재 보유하고 있는 상품입니다.";

    public void sayHello() {
        System.out.println(HELLO);
    }

    public void showInventory(Inventory inventory) {
        System.out.println(GUIDE_INVENTORY + System.lineSeparator());
        System.out.println(inventory);
    }

    public void displayError(String message) {
        System.out.println(message);
    }
}
