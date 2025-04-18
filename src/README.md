# 항해 플러스 백엔드 코스 1주차 과제

## 1. 코틀린 동시성 제어 - ReentrantLock

### 적용 방식
- 목적: 동일 리소스(예: 사용자 ID)에 대해 동시에 접근하는 작업들을 안전하게 처리하기 위함.

- 구현 방법: 
  - ReentrantLockManager 클래스를 사용하여, 각 키마다 별도의 ReentrantLock을 관리합니다. 
  - ConcurrentHashMap: 여러 스레드가 동시에 접근할 수 있도록 안전하게 키와 락을 저장합니다. 
  - getOrPut: 특정 키에 해당하는 락이 없으면 새 ReentrantLock을 생성하여 저장합니다. 
  - execute 메서드: 해당 키에 대한 락을 획득한 후 작업(action)을 실행하고, 완료 후 락을 해제하여 동시 접근 문제를 방지합니다.

### 장점
- 세밀한 제어:
  - 특정 키(예: 사용자 ID)별로 락을 관리하므로, 동일 리소스에 대한 동시 접근을 명확하게 제어할 수 있습니다. 
- 유연성:
  - 도메인 로직 내에서 동시성 제어를 캡슐화함으로써, 여러 스레드가 동시에 접근하더라도 안전하게 상태 변경을 처리할 수 있습니다. 
- 명시적 제어:
  - 락의 획득과 해제를 명시적으로 처리하므로, 필요 시 재시도나 타임아웃 등의 추가 로직 구현이 용이합니다.

### 단점
- 수동 관리의 위험:
  - 락 획득 후 반드시 해제해야 하는데, 예외 처리나 코딩 실수로 락 해제를 누락할 경우 데드락(Deadlock)이나 리소스 누수가 발생할 수 있습니다.
- 성능 오버헤드:
  - 매우 많은 키에 대해 락을 생성하고 관리하면, 메모리 사용량 증가 및 락 관리로 인한 오버헤드가 발생할 수 있습니다. 
- 복잡성:
  - 동시성 이슈를 직접 다루어야 하므로, 구현이나 디버깅, 유지보수 과정에서 복잡성이 증가할 수 있습니다.

