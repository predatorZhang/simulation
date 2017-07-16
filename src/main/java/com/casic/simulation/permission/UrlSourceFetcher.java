package com.casic.simulation.permission;

public abstract interface UrlSourceFetcher
{
  public abstract UserObj getSource(String username, String password);
}