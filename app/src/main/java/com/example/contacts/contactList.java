package com.example.contacts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class contactList extends AppCompatActivity {
    private View progressview, loginformview;
    private TextView tvload;
     ListView lstview;
     conadaptar adaptar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        lstview=findViewById(R.id.lstview);
        loginformview=findViewById(R.id.loginform);
        progressview=findViewById(R.id.loginpro);
        tvload=findViewById(R.id.tvload);

        lstview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(contactList.this,coninfo.class);
                    intent.putExtra("index",position);
                    startActivityForResult(intent,108);
            }
        });
        String whereClause="useremail = '"+appcls.user.getEmail()+"'";
        DataQueryBuilder queryBuilder= DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        queryBuilder.setGroupBy("email");
        //showProgress(true);
        Backendless.Persistence.of(contacttable.class).find(queryBuilder, new AsyncCallback<List<contacttable>>() {
            @Override
            public void handleResponse(List<contacttable> response) {
                appcls.cints=response;
                adaptar=new conadaptar( contactList.this, appcls.cints);
                lstview.setAdapter(adaptar);


                Toast.makeText(contactList.this, "Thos us ths data", Toast.LENGTH_SHORT).show();
          //  showProgress(false);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(contactList.this, "Failde to get data"+fault.getMessage(), Toast.LENGTH_SHORT).show();
               // showProgress(false);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==108){
            adaptar.notifyDataSetChanged();
        }
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
