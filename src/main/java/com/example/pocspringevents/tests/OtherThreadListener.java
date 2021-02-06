package com.example.pocspringevents.tests;

import com.example.pocspringevents.model.MyEntity;
import com.example.pocspringevents.model.MyEntityRepository;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class OtherThreadListener {

  private final MyEntityRepository repository;

  public OtherThreadListener(MyEntityRepository repository) {
    this.repository = repository;
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void listenBeforeCommit(OtherThreadEvent event) throws InterruptedException {
    // Maybe the CreatedAt and StartAt field can be populated
    log(event, TransactionPhase.BEFORE_COMMIT);
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void listenAfterCommit(OtherThreadEvent event) throws InterruptedException {
    // Should have the CreatedAt and StartAt field populated
    log(event, TransactionPhase.AFTER_COMMIT);
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
  public void listenAfterRollback(OtherThreadEvent event) throws InterruptedException {
    // Can be used to apply Saga Pattern
    log(event, TransactionPhase.AFTER_ROLLBACK);
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
  public void listenAfterCompletion(OtherThreadEvent event) throws InterruptedException {
    // Can be used to apply Saga Pattern
    log(event, TransactionPhase.AFTER_COMPLETION);
  }

  private void log(OtherThreadEvent event, TransactionPhase phase) throws InterruptedException {
    final MyEntity entity = event.getEntity();
    if (entity.isSameThread()) {
      throw new RuntimeException(String.format("The event was originated in a other thread. "
              + "Event Thread=[%s] Current Thread=[%s]", entity.getSourceThread(),
          Thread.currentThread().getName()));
    }

    TimeUnit.SECONDS.sleep(3); // Simulating slow, should not impact, because is @Async
    log.info("[{}] Listen {} - {}", entity.id, phase, entity);
  }

}