# :rotating_light: 주요 결정 사항 (리뷰 전에 한 번만 읽어주시기 바랍니다!)
1. 프로모션 조건 적용 시점 관리 관련
  - :thinking: 고민했던 부분:  
    만약 고객이 제공된 재고 현황을 기준으로 구매를 결정하는 중에 어떤 상품의 프로모션 기간이 종료된다면 어떻게 대응할 것인가?

  - :white_check_mark: 최종 결정:  
    고객이 제공된 재고 현황을 기준으로 구매를 결정했음에도, 프로모션 기간 종료를 이유로 구매를 거절하면 고객 만족도가 크게 저하될 수 있다.  
    따라서 고객이 재고를 확인하고 결제하여 영수증을 출력받을 때까지, 제공된 재고 현황의 프로모션 조건은 유지되어야 한다.

2. 상품 ID 관련
  - :thinking: 고민했던 부분:  
    동일한 이름의 상품이라도 프로모션 적용 여부에 따라 구분될 수 있으므로, 상품을 식별할 수 있는 고유 ID가 필요하다.

  - :white_check_mark: 최종 결정:  
    프로모션이 적용된 상품은 `상품명+프로모션명`, 적용되지 않은 상품은 `상품명`으로 ID를 지정했다.

3. 프로모션 혜택과 멤버십 할인 적용 기준 관련
  - :thinking: 고민했던 부분:  
    프로모션 재고 상품에서 프로모션 혜택을 받지 않은 경우, 이를 프로모션 미적용 내역으로 봐야 하는가?  
    예시) 2+1 프로모션이 적용된 사이다 2개 구매 시 1개를 증정받을 수 있음. 하지만 고객이 증정 상품을 거절하고 멤버십 할인을 선택한 경우.

  - :white_check_mark: 최종 결정:  
    프로모션 혜택은 증정 상품을 실제로 수령했을 때 적용된 것으로 간주한다.  
    따라서 증정 상품을 수령하지 않은 경우는 프로모션 미적용 내역으로 처리되며, 대신 멤버십 할인을 적용할 수 있다.  


# :convenience_store: W편의점 기능 설명
`W편의점`은 구매자의 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 안내하는 결제 시스템으로, 아래와 같은 기능을 제공합니다.  

1. 재고관리
   - 고객에게 재고 현황(상품명, 수량, 프로모션명) 안내
   - 각 상품의 재고 수량을 고려하여 결제 가능 여부 확인
   - 결재 정보를 바탕으로 재고를 차감하여 최신 재고 상태 유지
2. 프로모션 할인
   - 프로모션 기간중에는 프로모션 재고를 우선적으로 차감하며, 프로모션 재고가 부족한 경우 일반 재고 사용
   - 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내
   - 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제하게 됨을 안내
3. 멤버십 할인
    - 프로모션 미적용 금액의 30%를 할인 (최대 한도는 8,000원)
    - 프로모션 적용 후 남은 금액에 대해 멤버십 할인을 적용
4. 영수증 출력
    - 고객의 구매 내역과 할인을 요약하여 출력
    - [영수증 항목 예시]
      - 구매 상품 내역: 구매한 상품명, 수량, 가격
      - 증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
      - 금액 정보 
        - 총구매액: 구매한 상품의 총 수량과 총 금액
        - 행사할인: 프로모션에 의해 할인된 금액
        - 멤버십할인: 멤버십에 의해 추가로 할인된 금액
        - 내실돈: 최종 결제 금액  


# :hammer_and_pick: 구현 기능 목록
- [X] `promotions.md`로부터 프로모션 목록을 입력받아 프로모션들을 등록한다.
    - `promotions.md` 파일을 읽는 중 오류가 발생하면 에러 메시지를 출력하고 프로그램을 종료한다.
    - 프로모션 정보의 형식이 올바르지 않은 경우, 에러 메시지를 출력하고 프로그램을 종료한다.
    - 동일한 이름의 프로모션이 존재하는 경우, 에러 메시지를 출력하고 프로그램을 종료한다.
- [X] `products.md`로부터 상품 목록을 입력받아 상품들을 등록한다.
    - `products.md` 파일을 읽는 중 오류가 발생하면 에러 메시지를 출력하고 프로그램을 종료한다.
    - 상품 정보의 형식이 올바르지 않은 경우, 에러 메시지를 출력하고 프로그램을 종료한다.
    - 상품 정보에 기재된 프로모션이 존재하지 않는 경우, 에러 메시지를 출력하고 프로그램을 종료한다.
    - 증복된 상품이 존재하는 경우, 에러 메시지를 출력하고 프로그램을 종료한다.
    - 동일 상품에 여러 프로모션이 적용된 경우, 에러 메시지를 출력하고 프로그램을 종료한다.
- [X] 환영 인사와 함께 상품 정보(상품명, 가격, 프로모션 이름, 재고)를 안내한다.
    - 재고가 0이라면 `재고없음`을 출력한다.
- [X] 사용자로부터 구매할 상품과 수량을 입력 받는다.
    - 구매할 상품과 수량 형식이 올바르지 않은 경우, 에러 메시지를 출력하고 다시 입력받는다.
    - 존재하지 않는 상품을 입력한 경우, 에러 메시지를 출력하고 다시 입력받는다.
    - 구매 수량이 재고 수량을 초과한 경우, 에러 메시지를 출력하고 다시 입력받는다.
    - 기타 잘못된 입력의 경우, 에러 메시지를 출력하고 다시 입력받는다.
- [X] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우, 다음을 수행한다. 
    - [X] 혜택에 대한 안내 메시지를 출력한다.
    - [X] 그 수량만큼 추가 여부(Y/N)를 입력받는다.
      - 잘못된 입력의 경우, 에러 메시지를 출력하고 다시 입력받는다.
- [X] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 다음을 수행한다.
    - [X] 일부 수량에 대해 정가로 결제할지 여부에 대한 안내 메시지를 출력한다.
    - [X] 결제 여부(Y/N)를 입력받는다.
        - 잘못된 입력의 경우, 에러 메시지를 출력하고 다시 입력받는다.
- [X] 멤버십 할인 적용 여부를 확인하기 위해 다음을 수행한다.
    - [X] 멤버십 할인 적용 안내 메시지를 출력한다.
    - [X] 멤버십 할인 적용여부(Y/N)를 입력받는다.
        - 잘못된 입력의 경우, 에러 메시지를 출력하고 다시 입력받는다.
- [X] 영수증(구매 상품 내역, 증정 상품 내역, 금액 정보)을 출력한다.
- [X] 추가 구매 여부를 확인하기 위해 다음을 수행한다.
    - [X] 추가 구매 여부 안내 메시지를 출력한다.
    - [X] 추가 구매 여부(Y/N)를 입력받는다.
        - 잘못된 입력의 경우, 에러 메시지를 출력하고 다시 입력받는다.