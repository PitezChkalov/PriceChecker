package info.androidhive.recyclerviewswipe.service;

import android.content.Context;

import org.springframework.http.HttpStatus;

import info.androidhive.recyclerviewswipe.entity.Login;

public interface UserLoginCallback {
    void complete(HttpStatus result);
}
