package com.example.pocspringevents.model;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.Assert;

@Data
@Entity
public class MyEntity {

  @Id
  @Builder.Default
  public String id = UUID.randomUUID().toString();

  @Column(nullable = false)
  private String sourceClass;

  @Column(nullable = false)
  private final String sourceThread = Thread.currentThread().getName();

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @Deprecated
  public MyEntity() {
    // Do nothing
  }

  public MyEntity(final String sourceClass) {
    Assert.notNull(sourceClass, "Parameter must not be null");
    this.sourceClass = sourceClass;
  }

  public boolean isSameThread() {
    return Thread.currentThread().getName().equals(this.sourceThread);
  }

}