package store.view;

import static store.exception.ExceptionMessage.INVALID_INPUT;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String REQUEST_ORDERS = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String NOTIFY_LESS_ORDERED = "현재 %s은(는) %,d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String NOTIFY_NOT_AVAILABLE_PROMOTION =
            "현재 %s %,d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String ASK_MEMBERSHIP_DISCOUNT = "멤버십 할인을 받으시겠습니까? (Y/N)";

    public String requestOrders() {
        System.out.println(REQUEST_ORDERS);
        return Console.readLine();
    }

    public boolean notifyLessOrdered(String productName, int freeQuantity) {
        System.out.println(String.format(NOTIFY_LESS_ORDERED, productName, freeQuantity));
        while (true) {
            try {
                return receiveResponse();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean notifyNotAvailablePromotion(String productName, int nonPromotionalQuantity) {
        System.out.println(String.format(NOTIFY_NOT_AVAILABLE_PROMOTION, productName, nonPromotionalQuantity));
        while (true) {
            try {
                return receiveResponse();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean askMemberShipDiscount() {
        System.out.println(ASK_MEMBERSHIP_DISCOUNT);
        while (true) {
            try {
                return receiveResponse();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static boolean receiveResponse() {
        String response = Console.readLine();
        if (response.equalsIgnoreCase("Y")) {
            return true;
        }
        if (response.equalsIgnoreCase("N")) {
            return false;
        }
        throw new IllegalArgumentException(INVALID_INPUT.getMessage());
    }
}
