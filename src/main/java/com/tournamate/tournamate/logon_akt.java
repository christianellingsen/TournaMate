package com.tournamate.tournamate;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class logon_akt extends AppCompatActivity {

    private LoginButton myLoginButton;
    private TextView welcomeText;
    private ImageView profilePic;
    private CallbackManager myCallBackManager;
    private FacebookCallback<LoginResult> myCallBack;

    AccessTokenTracker myAccessTokenTraker;
    ProfileTracker myProfileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon_akt);

        profilePic = (ImageView) findViewById(R.id.fb_profilePic);
        myLoginButton = (LoginButton) findViewById(R.id.fb_login_b);
        welcomeText = (TextView) findViewById(R.id.fb_welcome_tv);

        myCallBackManager = CallbackManager.Factory.create();

        myAccessTokenTraker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };

        myProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                displayWelcomeMessage(currentProfile);
            }
        };

        myAccessTokenTraker.startTracking();
        myProfileTracker.startTracking();

        myCallBack = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                displayWelcomeMessage(profile);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };

        myLoginButton.setReadPermissions("user_friends");
        myLoginButton.registerCallback(myCallBackManager,myCallBack);



    }

    private void displayWelcomeMessage(Profile profile){
        if(profile!=null){
            welcomeText.setText("Welcome "+profile.getName());
            profilePic.setImageURI(profile.getProfilePictureUri(30, 30));
            profilePic.refreshDrawableState();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myCallBackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayWelcomeMessage(profile);
    }

    @Override
    protected void onStop() {
        super.onStop();
        myAccessTokenTraker.stopTracking();
        myProfileTracker.stopTracking();
    }

}
