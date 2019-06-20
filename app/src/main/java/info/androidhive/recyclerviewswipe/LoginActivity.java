package info.androidhive.recyclerviewswipe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.springframework.http.HttpStatus;

import info.androidhive.baltsilverapp.R;
import info.androidhive.recyclerviewswipe.entity.Login;
import info.androidhive.recyclerviewswipe.service.DialogService;
import info.androidhive.recyclerviewswipe.service.IUserService;
import info.androidhive.recyclerviewswipe.service.UserLoginCallback;
import info.androidhive.recyclerviewswipe.service.UserService;
import timber.log.Timber;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, UserLoginCallback {

    IUserService userService = new UserService();
    EditText login;
    EditText password;
    String mPassword;
    String mLogin;
    Button RegButton;
    Button EnterButton;
    ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        EnterButton  = (Button) findViewById(R.id.sign_in_button);
        EnterButton.setOnClickListener(this);
        RegButton = (Button) findViewById(R.id.imageButton6);
        RegButton.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sign_in_button:
                mLogin = login.getText().toString();
                IUserService userService = new UserService();
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8)
                {
                    mPassword = password.getText().toString();
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    attemptLogin(mLogin, mPassword);

                    progressBar.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.imageButton6:
                Intent intent = new Intent(this, RegActivity.class);
                startActivity(intent);
                break;
        }
    }

    protected void onStart() {
        super.onStart();
        SharedPreferences mySharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (mySharedPreferences.contains("login") && mySharedPreferences.contains("password")){
            mLogin = mySharedPreferences.getString("login","");
            mPassword = mySharedPreferences.getString("password","");
            progressBar.setVisibility(View.VISIBLE);
            attemptLogin(mLogin, mPassword);
        }
    }

    private void attemptLogin(String login, String password){
        userService.login(new Login(login, password), this);
    }

    public void loginResult(HttpStatus result) {
        Timber.i("Login result: "+ result);

        if (result == HttpStatus.OK) {
            SharedPreferences mySharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("login", mLogin);
            editor.putString("password", mPassword);
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else if(result == HttpStatus.NOT_FOUND) {
            MyApplication.getInstance().makeToast("Неверный логин или пароль", Toast.LENGTH_LONG);
        }
    else if(result == HttpStatus.FORBIDDEN){
            DialogService.createSubscribeDialog(this);
        }
         progressBar.setVisibility(View.GONE);
    }

    @Override
    public void complete(HttpStatus result) {
        progressBar.setVisibility(View.INVISIBLE);
        loginResult(result);
    }

}