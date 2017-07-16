package com.casic.simulation.permission;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


public class PermissionChecker
{
  private PermissionMatcher permissionMatcher = new PermissionMatcher();
 
  private HttpUrlSourceFetcher httpUrlSourceFetcher;

  @Resource
  public void setHttpUrlSourceFetcher(HttpUrlSourceFetcher httpUrlSourceFetcher)
  {
	this.httpUrlSourceFetcher = httpUrlSourceFetcher;
  }

public boolean isAuthorized(String text,UserObj userObj) {

  List<String> haves = new ArrayList<String>();
  String authorities = userObj.getAuthorities();
  for (String au : authorities.split(",")) {
    haves.add(au);
  }

  for (String want : text.split(",")) {
    for (String have :haves) {
      if (this.permissionMatcher.match(want, have)) {
        return true;
      }
    }
  }

  return false;
}

  public void setReadOnly(boolean readOnly) {
    this.permissionMatcher.setReadOnly(readOnly);
  }

  public boolean isReadOnly() {
    return this.permissionMatcher.isReadOnly();
  }
}