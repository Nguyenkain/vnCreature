package com.example.vncreatures.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;

import com.example.vncreatures.R;
import com.example.vncreatures.model.LoginViewModel;

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
        mModel.loginButton = (Button) findViewById(R.id.login_button);
        mModel.facebookLoginButton = (Button) findViewById(R.id.facebookLogin_button);
    }

}
