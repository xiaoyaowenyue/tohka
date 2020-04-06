package com.ht.authorization;

public class AuthenticationContext {

    private static final ThreadLocal<Authentication> holder = new ThreadLocal<>();

    public static Authentication current() {
        return holder.get();
    }

    public static void setAuthentication(Authentication authentication) {
        holder.set(authentication);
    }

    public static void clearContext() {
        holder.remove();
    }

}
