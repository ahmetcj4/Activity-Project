package com.intern.tmob.activityextreme;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mustafa.myapplication.backend.myApi.MyApi;
import com.example.mustafa.myapplication.backend.myApi.model.Entity;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class SplashActivityFragment extends Fragment {
    private CallbackManager mCallbackManager;
    AccessToken mAccessToken;
    public static Profile mProfile;

    private static MyApi myApiService = null;

    private AccessTokenTracker mAccessTokenTracker;

    private ProfileTracker mProfileTracker;

    public SplashActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                mAccessToken = newAccessToken;
            }
        };

        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                mProfile = newProfile;
            }
        };

        mAccessTokenTracker.startTracking();
        mProfileTracker.startTracking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        FloatingActionButton floatingActionButton = (FloatingActionButton)rootView.findViewById(R.id.contiue_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FavoritesActivity.class);
                startActivity(intent);
            }
        });
        LoginButton loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("user_friends"));
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mProfile = Profile.getCurrentProfile();
                mAccessToken = AccessToken.getCurrentAccessToken();
                new SignupTask().execute(getActivity());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAccessTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }
    class SignupTask extends AsyncTask<Context, Void, Void> {

        private Context context = null;

        @Override
        protected Void doInBackground(Context... params) {
            if (myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");

                myApiService = builder.build();
            }
            context = params[0];
            try {
                Entity res = myApiService.login(mProfile.getId()).execute();
                if(res == null) {
                    myApiService.signup(mProfile.getId(), mProfile.getFirstName(), mProfile.getLastName(), " ", " ").execute();
                    startActivity(new Intent(getActivity(), FavoritesActivity.class));
                }
                else {
                    startActivity(new Intent(getActivity(),WallActivity.class));
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

    }

}
