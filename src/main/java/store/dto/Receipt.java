package store.dto;

import java.text.DecimalFormat;
import java.util.Map;
import store.model.ShoppingCart;

public class Receipt {
    private static final String HEADER = "==============W 편의점================";
    private static final String TEMPLATE = "%-15s %-10s %-10s";
    private static final String PURCHASE_HEADER = String.format(TEMPLATE, "상품명", "수량", "금액");
    private static final String CENTER = "=============증    정===============";
    private static final String FOOTER = "====================================";
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;
    private static final double MEMBERSHIP_DISCOUNT_RATIO = 0.3;

    private final Map<String, Integer> totalPurchaseQuantityByProductName;
    private final Map<String, Integer> totalPriceByProductName;
    private final Map<String, Integer> totalFreeQuantityByProductName;
    private final boolean hasMemberShipDiscount;
    private final int totalPurchaseQuantity;
    private final int totalPurchaseAmount;
    private final int eventDiscount;
    private final int membershipDiscount;
    private final int finalAmount;

    private Receipt(ShoppingCart cart, boolean hasMemberShipDiscount) {
        this.totalPurchaseQuantityByProductName = cart.calculateTotalPurchaseQuantityByProductName();
        this.totalPriceByProductName = cart.calculateTotalPriceByProductName();
        this.totalFreeQuantityByProductName = cart.calculateTotalFreeQuantityByProductName();
        this.hasMemberShipDiscount = hasMemberShipDiscount;
        this.totalPurchaseQuantity = cart.calculateTotalPurchaseQuantity();
        this.totalPurchaseAmount = cart.calculateTotalPurchaseAmount();
        this.eventDiscount = cart.calculateEventDiscount();
        this.membershipDiscount = calculateMembershipDiscount(cart.calculateNonPromotionalAmount());
        this.finalAmount = totalPurchaseAmount - eventDiscount - membershipDiscount;
    }

    public static Receipt of(ShoppingCart cart, boolean hasMemberShipDiscount) {
        return new Receipt(cart, hasMemberShipDiscount);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HEADER).append(System.lineSeparator());
        stringBuilder.append(writePurchaseDetails());
        stringBuilder.append(CENTER).append(System.lineSeparator());
        stringBuilder.append(writeFreeDetails());
        stringBuilder.append(FOOTER).append(System.lineSeparator());
        stringBuilder.append(writeSummary());
        return stringBuilder.toString();
    }

    private String writePurchaseDetails() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(PURCHASE_HEADER).append(System.lineSeparator());

        appendPurchaseDetails(stringBuilder);

        return stringBuilder.toString();
    }

    private void appendPurchaseDetails(StringBuilder stringBuilder) {
        for (String productName : totalPurchaseQuantityByProductName.keySet()) {
            int quantity = totalPurchaseQuantityByProductName.get(productName);
            int totalPrice = totalPriceByProductName.getOrDefault(productName, 0);

            stringBuilder.append(String.format(TEMPLATE, productName, formatNumber(quantity), formatNumber(totalPrice)))
                    .append(System.lineSeparator());
        }
    }

    private String formatNumber(int number) {
        return new DecimalFormat("###,###").format(number);
    }

    private String writeFreeDetails() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String productName : totalFreeQuantityByProductName.keySet()) {
            int quantity = totalFreeQuantityByProductName.get(productName);
            stringBuilder.append(String.format(TEMPLATE, productName, formatNumber(quantity), ""))
                    .append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    private String writeSummary() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(TEMPLATE, "총구매액", formatNumber(totalPurchaseQuantity),
                        formatNumber(totalPurchaseAmount))).append(System.lineSeparator());
        stringBuilder.append(String.format(TEMPLATE, "행사할인", "", "-" + formatNumber(eventDiscount)))
                .append(System.lineSeparator());
        stringBuilder.append(String.format(TEMPLATE, "멤버십할인", "", "-" + formatNumber(membershipDiscount)))
                .append(System.lineSeparator());
        stringBuilder.append(String.format(TEMPLATE, "내실돈", "", formatNumber(finalAmount)))
                .append(System.lineSeparator());
        return stringBuilder.toString();
    }

    private int calculateMembershipDiscount(int nonPromotionalAmount) {
        if (!hasMemberShipDiscount) {
            return 0;
        }
        return Integer.min(MAX_MEMBERSHIP_DISCOUNT, (int) (nonPromotionalAmount * MEMBERSHIP_DISCOUNT_RATIO));
    }
}
