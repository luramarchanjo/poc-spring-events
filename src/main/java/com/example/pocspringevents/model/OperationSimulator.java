package com.example.pocspringevents.model;

import com.example.pocspringevents.tests.MyEvent;
import com.example.pocspringevents.tests.MyRollbackEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class OperationSimulator {

  private final ApplicationEventPublisher eventPublisher;
  private final MyEntityRepository repository;

  @Transactional
  public void simulateTransactionCommit() {
    // Should trigger the BEFORE_COMMIT, AFTER_COMMIT and AFTER_COMPLETION listeners
    savedEntityAndPublishEvent();
  }

  @Transactional
  public void simulateTransactionRollback() {
    // Should trigger the AFTER_ROLLBACK and AFTER_COMPLETION listeners
    savedEntityAndPublishEvent();
    throw new RuntimeException("I am forcing a transaction rollback. Please ignore this Exception");
  }

  @Transactional
  public void simulateErrorAndSlowAtListener() {
    final long started = System.currentTimeMillis();
    log.info("Started - Listener slow and throwing error");
    final MyEntity entity = saveEntity();
    this.eventPublisher.publishEvent(new MyRollbackEvent(entity));
    log.info("Finished=[{}] - Listener slow and throwing error",
        System.currentTimeMillis() - started);
  }

  private MyEntity saveEntity() {
    final MyEntity entity = new MyEntity(this.getClass().getSimpleName());
    return this.repository.save(entity);
  }

  private MyEntity savedEntityAndPublishEvent() {
    final MyEntity savedEntity = saveEntity();
    final MyEvent event = new MyEvent(savedEntity);
    this.eventPublisher.publishEvent(event);

    return savedEntity;
  }

}
