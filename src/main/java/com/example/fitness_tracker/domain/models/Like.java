package com.example.fitness_tracker.domain.models;

import com.example.fitness_tracker.domain.models.auditable.BaseEntityWithSoftDelete;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "likes")
@Getter
@Setter
public class Like extends BaseEntityWithSoftDelete {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}