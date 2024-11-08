# java-convenience-store-precourse

## :hammer_and_pick: 구현 기능 목록
- [ ] `promotions.md`로부터 프로모션 목록을 입력받아 프로모션들을 등록한다.
- [ ] `products.md`로부터 상품 목록을 입력받아 상품들을 등록한다.
    - 동일 상품에 여러 프로모션이 적용된 경우, 에러 메시지를 출력하고 프로그램을 종료한다.
- [ ] 환영 인사와 함께 상품 정보(상품명, 가격, 프로모션 이름, 재고)를 안내한다.
    - 재고가 0이라면 `재고없음`을 출력한다.
- [ ] 사용자로부터 구매할 상품과 수량을 입력 받는다.
    - 구매할 상품과 수량 형식이 올바르지 않은 경우, 에러 메시지를 출력하고 다시 입력받는다.
    - 존재하지 않는 상품을 입력한 경우, 에러 메시지를 출력하고 다시 입력받는다.
    - 구매 수량이 재고 수량을 초과한 경우, 에러 메시지를 출력하고 다시 입력받는다.
    - 기타 잘못된 입력의 경우, 에러 메시지를 출력하고 다시 입력받는다.
- [ ] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우, 다음을 수행한다. 
    - [ ] 혜택에 대한 안내 메시지를 출력한다.
    - [ ] 그 수량만큼 추가 여부(Y/N)를 입력받는다.
      - 잘못된 입력의 경우, 에러 메시지를 출력하고 다시 입력받는다.
- [ ] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 다음을 수행한다.
    - [ ] 일부 수량에 대해 정가로 결제할지 여부에 대한 안내 메시지를 출력한다.
    - [ ] 결제 여부(Y/N)를 입력받는다.
        - 잘못된 입력의 경우, 에러 메시지를 출력하고 다시 입력받는다.
- [ ] 멤버십 할인 적용 여부를 확인하기 위해 다음을 수행한다.
    - [ ] 멤버십 할인 적용 안내 메시지를 출력한다.
    - [ ] 멤버십 할인 적용여부(Y/N)를 입력받는다.
        - 잘못된 입력의 경우, 에러 메시지를 출력하고 다시 입력받는다.
- [ ] 영수증(구매 상품 내역, 증정 상품 내역, 금액 정보)을 출력한다.
- [ ] 추가 구매 여부를 확인하기 위해 다음을 수행한다.
    - [ ] 추가 구매 여부 안내 메시지를 출력한다.
    - [ ] 추가 구매 여부(Y/N)를 입력받는다.
        - 잘못된 입력의 경우, 에러 메시지를 출력하고 다시 입력받는다.