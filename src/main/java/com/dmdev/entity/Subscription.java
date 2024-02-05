package com.dmdev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription implements BaseEntity<Integer> {
    private Integer id;
    private Integer userId;
    private String name;
    private Provider provider;
    private Instant expirationDate;
    private Status status;
}
