package store.exception;

public enum ExceptionMessage {
    PROMOTION_FILE_READING_FAIL("프로모션 파일을 읽는 중 오류가 발생하여 프로그램을 종료합니다."),
    PROMOTION_PARSING_FAIL("프로모션 정보를 파싱하던 중 오류가 발생하여 프로그램을 종료합니다."),
    DUPLICATE_PROMOTION_ERROR("중복된 프로모션이 존재하여 프로그램을 종료합니다."),
    INVALID_PROMOTION_FORMAT("프로모션 정보의 형식이 올바르지 않습니다. 프로그램을 종료합니다."),
    PRODUCT_FILE_READING_FAIL("상품 파일을 읽는 중 오류가 발생하여 프로그램을 종료합니다."),;
    
    private static final String PREFIX = "[ERROR] ";
    private final String message;

    ExceptionMessage(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return PREFIX + message;
    }
}
