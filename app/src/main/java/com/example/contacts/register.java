package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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

public class register extends AppCompatActivity {
    private View progressview, loginformview;
    private TextView tvload;
    EditText etname,etmail,etpass,etrepass;
    Button btnregister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginformview=findViewById(R.id.loginform);
        progressview=findViewById(R.id.loginpro);
        tvload=findViewById(R.id.tvload);
        etname=findViewById(R.id.etname);
        etmail=findViewById(R.id.etname);
        etpass=findViewById(R.id.etpass);
        etrepass=findViewById(R.id.etrepass);
        btnregister=findViewById(R.id.btnregister);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etname.getText().toString().isEmpty() ||etmail.getText().toString().isEmpty() || etpass.getText().toString().isEmpty() || etrepass.getText().toString().isEmpty()){
                    Toast.makeText(register.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                }else{
                    if(etpass.getText().toString().trim().equals(etrepass.getText().toString().trim())){
                        String name=etname.getText().toString().trim();
                        String email=etmail.getText().toString().trim();
                        String password=etpass.getText().toString().trim();
                        BackendlessUser user=new BackendlessUser();
                        user.setEmail(email);
                        user.setPassword(password);
                        user.setProperty("name",name);

                        showProgress(true);
                        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {
                                showProgress(false);
                                Toast.makeText(register.this, "Sucsessfully registered", Toast.LENGTH_SHORT).show();
                                register.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(register.this, "Email already exits try logging in."+fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                            }
                        });
                    }else{
                        Toast.makeText(register.this, "Pass and confirm pass should be same", Toast.LENGTH_SHORT).show();
                    }
                }
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
