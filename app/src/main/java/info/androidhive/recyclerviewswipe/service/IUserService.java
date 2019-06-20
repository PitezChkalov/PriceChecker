package info.androidhive.recyclerviewswipe.service;


import android.content.Context;

import org.springframework.http.HttpStatus;

import java.util.List;

import info.androidhive.recyclerviewswipe.entity.Jewelry;
import info.androidhive.recyclerviewswipe.entity.Login;
import info.androidhive.recyclerviewswipe.entity.User;


/**
 * Created by Занятия on 07.12.2017.
 */

public interface IUserService {
     String AddUser(String username, String password, Boolean enabled);

     User getUser(String barCode, Context context);

    void login(Login login, UserLoginCallback userLoginCallback);

    Jewelry getJewelry(String barCode, Context context);

    HttpStatus sendOrder(List<Jewelry> jewelries, String user, Context context);
}