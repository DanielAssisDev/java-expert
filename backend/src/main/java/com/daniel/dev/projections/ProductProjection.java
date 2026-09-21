package com.daniel.dev.projections;

public interface ProductProjection extends IdProjection<Long> {
    String getName();
    Double getPrice();
}
