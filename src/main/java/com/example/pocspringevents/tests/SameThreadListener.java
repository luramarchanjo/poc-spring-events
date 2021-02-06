package com.example.pocspringevents.tests;

import com.example.pocspringevents.model.MyEntity;
import com.example.pocspringevents.model.MyEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.Assert;

@Component
@Slf4j
public class SameThreadListener {

  private final MyEntityRepository repository;

  public SameThreadListener(MyEntityRepository repository) {
    this.repository = repository;
  }

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void listenBeforeCommit(SameThreadEvent event) {
    // Should have the CreatedAt and StartAt field null
    updateSourceAndLog(event, TransactionPhase.BEFORE_COMMIT);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void listenAfterCommit(SameThreadEvent event) {
    // Should have the CreatedAt and StartAt field populated
    updateSourceAndLog(event, TransactionPhase.AFTER_COMMIT);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
  public void listenAfterRollback(SameThreadEvent event) {
    // Can be used to apply Saga Pattern
    updateSourceAndLog(event, TransactionPhase.AFTER_ROLLBACK);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
  public void listenAfterCompletion(SameThreadEvent event) {
    // Can be used to apply Saga Pattern
    updateSourceAndLog(event, TransactionPhase.AFTER_COMPLETION);
  }

  private void updateSourceAndLog(SameThreadEvent event, TransactionPhase phase) {
    final MyEntity entity = event.getEntity();
    updateSource(entity);

    if (!entity.isSameThread()) {
      throw new RuntimeException(String.format("The event was originated in a other thread. "
              + "Event Thread=[%s] Current Thread=[%s]", entity.getSourceThread(),
          Thread.currentThread().getName()));
    }

    log.info("[{}] Listen {} - {}", entity.id, phase, entity);
  }

  private void updateSource(final MyEntity entity) {
    Assert.notNull(entity, "Parameter must not be null");
    entity.setSourceClass(getClass().getSimpleName());
    this.repository.save(entity);
  }

}