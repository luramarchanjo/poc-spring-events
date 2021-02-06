package com.example.pocspringevents.tests;

import com.example.pocspringevents.model.MyEntity;
import com.example.pocspringevents.model.MyEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class ListenerOnTheSameThread {

  private final MyEntityRepository repository;

  public ListenerOnTheSameThread(MyEntityRepository repository) {
    this.repository = repository;
  }

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void listenBeforeCommit(MyEvent event) {
    // Should have the CreatedAt and StartAt field null
    log(event, TransactionPhase.BEFORE_COMMIT);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void listenAfterCommit(MyEvent event) {
    // Should have the CreatedAt and StartAt field populated
    log(event, TransactionPhase.AFTER_COMMIT);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
  public void listenAfterRollback(MyEvent event) {
    // Can be used to apply Saga Pattern
    log(event, TransactionPhase.AFTER_ROLLBACK);
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
  public void listenAfterCompletion(MyEvent event) {
    // Can be used to apply Saga Pattern
    log(event, TransactionPhase.AFTER_COMPLETION);
  }

  // FIXME - Testar manipulação do objeto na thread principal / async

  private void log(MyEvent event, TransactionPhase phase) {
    final MyEntity entity = event.getEntity();
    if (!entity.isSameThread()) {
      throw new RuntimeException(String.format("The event was originated in a other thread. "
          + "Event Thread=[%s] Current Thread=[%s]", entity.getSourceThread(), Thread.currentThread().getName()));
    }
    log.info("[{}] Listen {} - {}", entity.id, phase, entity);
  }

}