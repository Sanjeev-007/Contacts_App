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
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class newContact extends AppCompatActivity {
Button newadd;
EditText etname,etmail,etphone;
    private View progressview, loginformview;
    private TextView tvload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        newadd=findViewById(R.id.newadd);
        etname=findViewById(R.id.etname);
        etmail=findViewById(R.id.etname);
        etphone=findViewById(R.id.etphone);
        loginformview=findViewById(R.id.loginform);
        progressview=findViewById(R.id.loginpro);
        tvload=findViewById(R.id.tvload);

        newadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etname.getText().toString().isEmpty() || etmail.getText().toString().isEmpty() || etphone.getText().toString().isEmpty()){
                    Toast.makeText(newContact.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }else{
                    String name=etname.getText().toString().trim();
                    String mail=etmail.getText().toString().trim();
                    String num=etphone.getText().toString().trim();
                    contacttable co=new contacttable();
                    co.setName(name);
                    co.setEmail(mail);
                    co.setNumber(num);
                co.setUseremail(appcls.user.getEmail());
                   // showProgress(true);

                        Backendless.Persistence.save(co, new AsyncCallback<contacttable>() {
                            @Override
                            public void handleResponse(contacttable response) {
                                Toast.makeText(newContact.this, "new Contact Saved Successfully ", Toast.LENGTH_SHORT).show();
                             //   showProgress(false);


                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(newContact.this, "UnABLE TO add contact "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                          //  showProgress(false);
                            }
                        });
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
