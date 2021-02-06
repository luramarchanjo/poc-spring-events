package com.example.pocspringevents.model;

import com.example.pocspringevents.tests.ExceptionEvent;
import com.example.pocspringevents.tests.OtherThreadEvent;
import com.example.pocspringevents.tests.SameThreadEvent;
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
  public void simulateTransactionCommit(final boolean sameThread) {
    // Should trigger the BEFORE_COMMIT, AFTER_COMMIT and AFTER_COMPLETION listeners
    savedEntityAndPublishEvent(sameThread);
  }

  @Transactional
  public void simulateTransactionRollback(final boolean sameThread) {
    // Should trigger the AFTER_ROLLBACK and AFTER_COMPLETION listeners
    savedEntityAndPublishEvent(sameThread);
    throw new RuntimeException("I am forcing a transaction rollback. Please ignore this Exception");
  }

  @Transactional
  public void simulateErrorAndSlowAtListener() {
    final MyEntity savedEntity = saveEntity();
    final ExceptionEvent event = new ExceptionEvent(savedEntity);
    this.eventPublisher.publishEvent(event);
  }

  private MyEntity saveEntity() {
    final MyEntity entity = new MyEntity(this.getClass().getSimpleName());
    return this.repository.save(entity);
  }

  private MyEntity savedEntityAndPublishEvent(final boolean sameThread) {
    final MyEntity savedEntity = saveEntity();
    final Object event =
        sameThread ? new SameThreadEvent(savedEntity) : new OtherThreadEvent(savedEntity);
    this.eventPublisher.publishEvent(event);

    return savedEntity;
  }

}
