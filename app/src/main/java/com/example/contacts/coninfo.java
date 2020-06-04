package com.example.contacts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class coninfo extends AppCompatActivity {
TextView tvchar,tvname;
ImageView ivcall,ivmail,ivedit,ivdelete;
Button btnedit;
EditText etname,etmail,etphone;
    private View progressview, loginformview;
    private TextView tvload;
    boolean edit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coninfo);
        tvchar=findViewById(R.id.tvchar);
        tvname=findViewById(R.id.tvname);
        ivcall=findViewById(R.id.ivcall);
        ivmail=findViewById(R.id.ivmail);
        ivedit=findViewById(R.id.ivedit);
        ivdelete=findViewById(R.id.ivdelete);
        btnedit=findViewById(R.id.btnedit);
        etname=findViewById(R.id.etname);
        etmail=findViewById(R.id.etmail);
        etphone=findViewById(R.id.etphone);

        loginformview=findViewById(R.id.loginform);
        progressview=findViewById(R.id.loginpro);
        tvload=findViewById(R.id.tvload);

        etname.setVisibility(View.GONE);
        etphone.setVisibility(View.GONE);
        etmail.setVisibility(View.GONE);
        btnedit.setVisibility(View.GONE);
       final int index=getIntent().getIntExtra("index",0);
etname.setText(appcls.cints.get(index).getName());
etmail.setText(appcls.cints.get(index).getEmail());
etphone.setText(appcls.cints.get(index).getNumber());

        tvchar.setText(appcls.cints.get(index).getName().toUpperCase().charAt(0)+"");
        tvname.setText(appcls.cints.get(index).getName());

        ivcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String uri="tel:"+appcls.cints.get(index).getNumber();
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);

            }
        });
        ivmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_EMAIL, appcls.cints.get(index).getEmail());
                    startActivity(Intent.createChooser(intent, "SEND Mail to "+appcls.cints.get(index).getName()));
            }
        });
        ivedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit=!edit;
                if(edit){
                    etname.setVisibility(View.VISIBLE);
                    etphone.setVisibility(View.VISIBLE);
                    etmail.setVisibility(View.VISIBLE);
                    btnedit.setVisibility(View.VISIBLE);
                }else{
                    etname.setVisibility(View.GONE);
                    etphone.setVisibility(View.GONE);
                    etmail.setVisibility(View.GONE);
                    btnedit.setVisibility(View.GONE);

                }

            }
        });
        ivdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(coninfo.this, "Clicked button", Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder dialog=new AlertDialog.Builder(coninfo.this);
                dialog.setMessage("Are u sure u want to delete contact");

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
                        Toast.makeText(coninfo.this, "inside positive button", Toast.LENGTH_SHORT).show();
                        Backendless.Persistence.of(contacttable.class).remove(appcls.cints.get(index), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                Toast.makeText(coninfo.this, "Sucessfully deleted contact ", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                coninfo.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                appcls.cints.remove(index);
                                Toast.makeText(coninfo.this, "Error did not delete "+fault.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
dialog.show();
            }
        });
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(etname.getText().toString().isEmpty() || etmail.getText().toString().isEmpty() || etphone.getText().toString().isEmpty()){
                        Toast.makeText(coninfo.this, "Please fill all the details ", Toast.LENGTH_SHORT).show();
                    }else {
                    appcls.cints.get(index).setName(etname.getText().toString().trim());
                    appcls.cints.get(index).setEmail(etmail.getText().toString().trim());
                    appcls.cints.get(index).setNumber(etphone.getText().toString().trim());
                        showProgress(true);

                        Backendless.Persistence.save(appcls.cints.get(index), new AsyncCallback<contacttable>() {
                            @Override
                            public void handleResponse(contacttable response) {
                                tvchar.setText(appcls.cints.get(index).getName().toUpperCase().charAt(0)+"");
                                tvname.setText(appcls.cints.get(index).getName());
                                Toast.makeText(coninfo.this, "Contact sucessfully changed", Toast.LENGTH_SHORT).show();
                                showProgress(false);
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(coninfo.this, "Unable to edit the contacts\n"+fault.getMessage(), Toast.LENGTH_SHORT).show();
                          showProgress(false);
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
