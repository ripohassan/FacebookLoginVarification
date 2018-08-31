package com.example.ripo.fbloginvarification;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.UIManager;

public class MainActivity extends AppCompatActivity {
    public static int APP_REQUEST_CODE = 99;
    public static final String TAG = "MainActivity";
    private Button login,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout=(Button)findViewById(R.id.logout);
        login=(Button)findViewById(R.id.login);

        getCurrentAccount();
    }

    private void getCurrentAccount(){
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if (accessToken != null) {
            //Handle Returning User
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {

                @Override
                public void onSuccess(final Account account) {

                    // Get Account Kit ID
                    String accountKitId = account.getId();
                    Log.e("Account Kit Id", accountKitId);

                    if(account.getPhoneNumber()!=null) {
                        Log.e("CountryCode", "" + account.getPhoneNumber().getCountryCode());
                        Log.e("PhoneNumber", "" + account.getPhoneNumber().getPhoneNumber());

                        // Get phone number
                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        String phoneNumberString = phoneNumber.toString();
                        logout.setVisibility(View.VISIBLE);
                        login.setVisibility(View.GONE);
                        Log.e("NumberString", phoneNumberString);


                    }

                    if(account.getEmail()!=null)
                        Log.e("Email",account.getEmail());
                }

                @Override
                public void onError(final AccountKitError error) {
                    // Handle Error
                    Log.e(TAG,error.toString());
                }
            });

        } else {
            //Handle new or logged out user
            Log.e(TAG,"Logged Out");
            Toast.makeText(this,"Logged Out User",Toast.LENGTH_SHORT).show();
        }
    }

    public void phoneLogin(@Nullable View view) {
        final Intent intent = new Intent(this, MainActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder = new AccountKitConfiguration.AccountKitConfigurationBuilder(
                LoginType.PHONE, AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.CODE
        UIManager uiManager = new SkinManager(
                LoginType.PHONE,
                SkinManager.Skin.TRANSLUCENT,
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? getResources().getColor(R.color.colorPrimary,null):getResources().getColor(R.color.colorPrimary)),
                R.color.colorPurple,
                SkinManager.Tint.WHITE,
                0.55
        );
        /*If you want default country code*/
        // configurationBuilder.setDefaultCountryCode("IN");
        configurationBuilder.setUIManager(uiManager);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(final int requestCode,final int resultCode,final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE && resultCode == RESULT_OK) {
            getCurrentAccount();
        }
    }

    public void logout(@Nullable View view){
        AccountKit.logOut();
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if(accessToken!=null)
            Log.e(TAG,"Still Logged in...");

        else
            logout.setVisibility(View.GONE);
        login.setVisibility(View.VISIBLE);
    }

}
