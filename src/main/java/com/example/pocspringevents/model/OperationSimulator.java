package com.example.pocspringevents.model;

import com.example.pocspringevents.tests.MyEvent;
import com.example.pocspringevents.tests.MyRollbackEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class OperationSimulator {

  private final ApplicationEventPublisher eventPublisher;
  private final MyEntityRepository repository;

  @Transactional
  @Scheduled(fixedDelayString = "60000")
  public void simulateTransactionCommit() {
    // Should trigger the BEFORE_COMMIT, AFTER_COMMIT and AFTER_COMPLETION listeners
    saveEntity();
  }

  @Transactional
  @Scheduled(fixedDelayString = "60000")
  public void s() {
    // Should trigger the BEFORE_COMMIT, AFTER_COMMIT and AFTER_COMPLETION listeners
    final MyEntity entity = saveEntity();
    this.eventPublisher.publishEvent(new MyRollbackEvent(entity));
  }

  @Transactional
  @Scheduled(fixedDelayString = "60000")
  public void simulateTransactionRollback() {
    // Should trigger the AFTER_ROLLBACK and AFTER_COMPLETION listeners
    saveEntity();
    throw new RuntimeException("I am forcing a transaction rollback. Please ignore this Exception");
  }

  private MyEntity saveEntity() {
    final MyEntity entity = new MyEntity(this.getClass().getSimpleName());
    final MyEntity savedEntity = this.repository.save(entity);

    final MyEvent event = new MyEvent(savedEntity);
    this.eventPublisher.publishEvent(event);

    return entity;
  }

}
