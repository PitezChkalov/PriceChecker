package info.androidhive.recyclerviewswipe;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import info.androidhive.baltsilverapp.R;
import info.androidhive.recyclerviewswipe.service.IUserService;
import info.androidhive.recyclerviewswipe.service.UserService;


public class RegActivity extends Activity implements OnClickListener {

    EditText email;
    EditText login;
    EditText password;
    Button EnterButton;
    String status;
    static final String stat = "SUCCESS";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reg);
        password = (EditText) findViewById(R.id.password);
        login = (EditText) findViewById(R.id.Login);
        EnterButton  = (Button) findViewById(R.id.sign_in_button);
        EnterButton.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sign_in_button:
                String pass = password.getText().toString();
                String log = login.getText().toString();
                if (log.length()<5 || pass.length()<5){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Слишком мало символов", Toast.LENGTH_SHORT);
                    toast.show();

                }else{

                    IUserService userService = new UserService();
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8)
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    //your codes here
                  status = userService.AddUser(log, pass, true);

                }
                  if(status == "OK") {

                      GoToNextActivity(v);
                  }
                  else {
                      Toast toast = Toast.makeText(getApplicationContext(),
                              status, Toast.LENGTH_SHORT);
                      toast.show();
                  }
        }
    }}



    public void GoToNextActivity(View v)
    {
        Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
    }


}