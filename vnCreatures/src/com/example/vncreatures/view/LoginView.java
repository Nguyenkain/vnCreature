package com.example.vncreatures.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.vncreatures.R;
import com.example.vncreatures.model.LoginViewModel;
import com.facebook.widget.LoginButton;

public class LoginView extends AbstractView {

    private LoginViewModel mModel = null;

    public LoginView(Context context, LoginViewModel model) {
        super(context);
        LayoutInflater li = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.login_layout, this);
        this.mModel = model;

        initUI();
    }

    private void initUI() {
        mModel.loginButton = (ImageButton) findViewById(R.id.facebookLogin_button);
    }

}
