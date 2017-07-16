package com.casic.simulation.core.jdbc;

public interface DataSourceInfo {
    String getName();

    void setName(String name);

    void validate();
}
