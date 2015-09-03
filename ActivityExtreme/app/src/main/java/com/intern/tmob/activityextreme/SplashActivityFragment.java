package com.intern.tmob.activityextreme;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import org.json.JSONException;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(AccessToken.getCurrentAccessToken()!=null)
        {
            startActivity(new Intent(getActivity(),WallActivity.class));
            getActivity().finish();
        }
        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        FloatingActionButton floatingActionButton = (FloatingActionButton)rootView.findViewById(R.id.contiue_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FavoritesActivity.class);
                startActivity(intent);
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("user_friends","user_location"));
        loginButton.setFragment(this);
        // Other app specific specialization
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mProfile = Profile.getCurrentProfile();
                mAccessToken = AccessToken.getCurrentAccessToken();
                Bundle parameters = new Bundle(1);
                parameters.putString("fields", "location");

                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        mProfile.getId(),
                        parameters,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                try {
                                    Object[] items = {getActivity(), response.getJSONObject().getJSONObject("location").getString("name")};
                                    new SignupTask().execute(items);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("error", exception.toString());
                Toast.makeText(getActivity(), "ERRORRRR", Toast.LENGTH_LONG).show();
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
    class SignupTask extends AsyncTask<Object, Void, Void> {

        private Context context = null;

        @Override
        protected Void doInBackground(Object... params) {
            if (myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");

                myApiService = builder.build();
            }
            context = (Context) params[0];
            try {
                String location = (String) params[1];
                Entity res = myApiService.login(mProfile.getId()).execute();
                if(res == null) {
                    myApiService.signup(mProfile.getId(), mProfile.getFirstName(), mProfile.getLastName(),
                            mProfile.getProfilePictureUri(100,100).toString()).execute();
                    startActivity(new Intent(getActivity(), FavoritesActivity.class));
                    getActivity().finish();
                }
                else {
                    startActivity(new Intent(getActivity(),WallActivity.class));
                    getActivity().finish();
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
