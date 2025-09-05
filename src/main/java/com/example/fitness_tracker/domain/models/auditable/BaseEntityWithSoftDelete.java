package com.example.fitness_tracker.domain.models.auditable;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

@MappedSuperclass
@SQLDelete(sql = "UPDATE #{#entityName} SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public abstract class BaseEntityWithSoftDelete extends BaseEntity {

    @Column(name = "deleted_at")
    private Instant deletedAt;

    /** Soft delete instead of actual removal */
    public void softDelete() {
        this.deletedAt = Instant.now();
    }

    /** Restore a soft-deleted entity */
    public void restore() {
        this.deletedAt = null;
    }

    /** Check if entity is deleted */
    public boolean isDeleted() {
        return deletedAt != null;
    }
}
