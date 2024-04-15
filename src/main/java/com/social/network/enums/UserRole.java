package com.social.network.enums;

public enum UserRole {
  ADMIN("admin"),
  USER("user"),
  MODERATOR("moderator");

  private String role;

  UserRole(String role) {
    this.role = role;
  }

  public String getValue() {
    return role;
  }
}
