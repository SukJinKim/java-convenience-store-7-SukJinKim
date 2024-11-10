package store.exception;

public enum ExceptionMessage {
    PROMOTION_FILE_READING_FAIL("프로모션 파일을 읽는 중 오류가 발생하여 프로그램을 종료합니다."),
    PROMOTION_PARSING_FAIL("프로모션 정보를 파싱하던 중 오류가 발생하여 프로그램을 종료합니다."),
    DUPLICATE_PROMOTION_ERROR("중복된 프로모션이 존재하여 프로그램을 종료합니다."),
    INVALID_PROMOTION_FORMAT("프로모션 정보의 형식이 올바르지 않습니다. 프로그램을 종료합니다."),
    PRODUCT_FILE_READING_FAIL("상품 파일을 읽는 중 오류가 발생하여 프로그램을 종료합니다."),
    PRODUCT_PARSING_FAIL("상품 정보를 파싱하던 중 오류가 발생하여 프로그램을 종료합니다."),
    INVALID_PRODUCT_FORMAT("상품 정보의 형식이 올바르지 않습니다. 프로그램을 종료합니다."),
    NO_SUCH_PROMOTION("상품 정보에 기재된 프로모션이 존재하지 않습니다. 프로그램을 종료합니다."),
    DUPLICATE_PRODUCT_ERROR("상품 정보에 중복된 상품이 있거나 동일 상품에 여러 프로모션이 적용되었습니다. 프로그램을 종료합니다."),
    INVALID_ORDER_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
    
    private static final String PREFIX = "[ERROR] ";
    private final String message;

    ExceptionMessage(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return PREFIX + message;
    }
}
