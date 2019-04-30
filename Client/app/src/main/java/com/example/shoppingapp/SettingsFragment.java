package com.example.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shoppingapp.controller.AccountManager;
import com.example.shoppingapp.controller.LocaleManager;
import com.example.shoppingapp.ui.login.LoginActivity;

import java.util.Locale;

public class SettingsFragment extends Fragment {


    private Button skLanguageButton;
    private Button enLanguageButton;
    private Button deLanguageButton;

    private Button logoutButton;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        skLanguageButton = getActivity().findViewById(R.id.languageSkButton);
        enLanguageButton = getActivity().findViewById(R.id.languageEnButton);
        deLanguageButton = getActivity().findViewById(R.id.languageDeButton);
        logoutButton = getActivity().findViewById(R.id.logoutButton);

        skLanguageButton.setOnClickListener(new OnLanguageChangeClicked("sk_"));
        enLanguageButton.setOnClickListener(new OnLanguageChangeClicked("en_"));
        deLanguageButton.setOnClickListener(new OnLanguageChangeClicked("de_"));

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AccountManager.loggedUser = null;

                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginIntent);
                getActivity().finish();
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    private void setVisible(){
        skLanguageButton.setVisibility(View.VISIBLE);
        enLanguageButton.setVisibility(View.VISIBLE);
        deLanguageButton.setVisibility(View.VISIBLE);
    }




    public class OnLanguageChangeClicked implements View.OnClickListener {

        private String language;

        public OnLanguageChangeClicked(String language) {
            this.language = language;
        }

        @Override
        public void onClick(View v) {

            setVisible();
            v.setVisibility(View.GONE);

            LocaleManager.setLocale(getActivity(),language);

        }
    }





}
