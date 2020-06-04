package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;

public class login extends AppCompatActivity {
private View progressview, loginformview;
private TextView tvload;
EditText etmail,etpass;
TextView reset;
Button btnlogin, btnregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginformview=findViewById(R.id.loginform);
        progressview=findViewById(R.id.loginpro);
        tvload=findViewById(R.id.tvload);
        etmail=findViewById(R.id.etname);
        etpass=findViewById(R.id.etpass);
        reset=findViewById(R.id.reset);
        btnlogin=findViewById(R.id.btnlogin);
        btnregister=findViewById(R.id.btnregister);


        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this, register.class);
                startActivity(intent);
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etmail.getText().toString().isEmpty() || etpass.getText().toString().isEmpty()){
                    Toast.makeText(login.this, "Please fill them", Toast.LENGTH_SHORT).show();
                }else{
                    String mal=etmail.getText().toString().trim();
                    String password=etpass.getText().toString().trim();
                    showProgress(true);
                    Backendless.UserService.login(mal, password, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            appcls.user=response;
                            Toast.makeText(login.this, "Logged in sucessfully", Toast.LENGTH_SHORT).show();
                            showProgress(false);
                            startActivity(new Intent(login.this,MainActivity.class));
                            login.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(login.this, "UNnable tologin"+fault.getMessage(), Toast.LENGTH_SHORT).show();
                       showProgress(false);
                        }
                    }, true);
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etmail.getText().toString().isEmpty()){
                    Toast.makeText(login.this, "Please enter ur email address", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }else{
                    String mail=etmail.getText().toString().trim();
                    showProgress(true);
                    Backendless.UserService.restorePassword(mail, new AsyncCallback<Void>() {
                        @Override
                        public void handleResponse(Void response) {
                            Toast.makeText(login.this, "email sent", Toast.LENGTH_SHORT).show();
                            showProgress(false);

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(login.this, "Wrong Email "+ fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                }

            }
        });
showProgress(true);
        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {
                if(response){
                    String uid= UserIdStorageFactory.instance().getStorage().get();
                    Backendless.Data.of(BackendlessUser.class).findById(uid, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            appcls.user=response;
                             startActivity(new Intent(login.this,MainActivity.class));
                             login.this.finish();
                             showProgress(false);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(login.this, "Error " +fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                }else{
                    showProgress(false);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(login.this, "Fuck man u are not logged in", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
// for very easy animations. If available, use these APIs to fade-in
// the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            loginformview.setVisibility(show ? View.GONE : View.VISIBLE);
           loginformview.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                  loginformview.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            progressview.setVisibility(show ? View.VISIBLE : View.GONE);
            progressview.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                 progressview.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            tvload.setVisibility(show ? View.VISIBLE : View.GONE);
            tvload.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvload.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
// The ViewPropertyAnimator APIs are not available, so simply show
// and hide the relevant UI components.
            progressview.setVisibility(show ? View.VISIBLE : View.GONE);
            tvload.setVisibility(show ? View.VISIBLE : View.GONE);
            loginformview.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
