package info.androidhive.recyclerviewswipe.service;

import android.os.AsyncTask;

/**
 * Created by Занятия on 07.12.2017.
 */


     public class UserServiceAsync extends AsyncTask<String, String[], String> {
    IUserService userService = new UserService();



        @Override
        protected String doInBackground(String... params) {

            return "QWE";

        }}