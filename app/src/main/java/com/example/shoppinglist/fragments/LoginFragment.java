package com.example.shoppinglist.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.shoppinglist.R;
import com.example.shoppinglist.utils.Constant;
import com.example.shoppinglist.utils.FragmentNavigation;
import com.google.android.gms.common.api.Api;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {
    public static final String TAG = LoginFragment.class.getSimpleName();

    private View view;
    private EditText etName;
    private Button btnLogin;
    private CheckBox cbAutoLogin, cbRememberMe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container,false);
        initializeElements(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isRememberMeChecked();
        isAutoLoginChecked();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTextLengthOk(etName.getText().toString())){
                    saveDataSharedPreferences();
                    login();
                } else {
                    Toast.makeText(getContext(), R.string.name_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isTextLengthOk(String string){
        if (string.length() >= 4 ){
            return true;
        }
        else {
            return false;
        }
    }

    private void initializeElements(View view){
        etName = view.findViewById(R.id.editText_login_name);
        btnLogin = view.findViewById(R.id.button_login);
        cbAutoLogin = view.findViewById(R.id.checkBox_auto_login);
        cbRememberMe = view.findViewById(R.id.checkBox_remember_me);
    }

    private void isRememberMeChecked(){
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(Constant.MY_LOGIN_DATA, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Constant.REMEMBER_ME, false)){
            etName.setText(sharedPreferences.getString(Constant.USERNAME, Constant.EMPTY_STRING));
            cbRememberMe.setChecked(true);
        }
    }

    private void isAutoLoginChecked(){
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(Constant.MY_LOGIN_DATA, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(Constant.AUTO_LOGIN, false)){
            cbAutoLogin.setChecked(true);
            login();
        }
    }

    private void saveDataSharedPreferences(){
        SharedPreferences.Editor sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(Constant.MY_LOGIN_DATA, MODE_PRIVATE).edit();
        if (cbRememberMe.isChecked()){
            sharedPreferences.putString(Constant.USERNAME, etName.getText().toString());
            sharedPreferences.putBoolean(Constant.REMEMBER_ME, true);
        } else {
            sharedPreferences.putString(Constant.USERNAME, "");
            sharedPreferences.putBoolean(Constant.REMEMBER_ME, false);
        }

        if (cbAutoLogin.isChecked()){
            sharedPreferences.putBoolean(Constant.AUTO_LOGIN, true);
        } else {
            sharedPreferences.putBoolean(Constant.AUTO_LOGIN, false);
        }

        sharedPreferences.apply();
    }

    private void login(){
        Constant.CURRENT_USER = etName.getText().toString();
        FragmentNavigation.getInstance(getContext()).replaceFragment(new ListsFragment(), R.id.fragment_content);
    }
}
