package in.wingstud.grocitoseller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import in.wingstud.grocitoseller.Api.RequestCode;
import in.wingstud.grocitoseller.Api.WebCompleteTask;
import in.wingstud.grocitoseller.Api.WebTask;
import in.wingstud.grocitoseller.Api.WebUrls;
import in.wingstud.grocitoseller.Common.Constrants;
import in.wingstud.grocitoseller.Common.SharedPrefManager;
import in.wingstud.grocitoseller.R;
import in.wingstud.grocitoseller.databinding.ActivityLoginBinding;
import in.wingstud.grocitoseller.model.LoginUser;
import in.wingstud.grocitoseller.model.LoginViewModel;
import in.wingstud.grocitoseller.util.SharedPref;
import in.wingstud.grocitoseller.util.Utils;

import static in.wingstud.grocitoseller.util.Utils.Tosat;

public class LoginActi extends AppCompatActivity implements WebCompleteTask {

    Context mContext;
    private ActivityLoginBinding binding;

    private String userName;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setLoginViewModel(loginViewModel);
        mContext = LoginActi.this;
        loginViewModel.getUser().observe(this, new Observer<LoginUser>() {
            @Override
            public void onChanged(LoginUser loginUser) {
                if (Utils.isValidMobileNumber(mContext, Objects.requireNonNull(loginUser).getMobileNumber()) &&
                        Utils.isPassword(mContext, Objects.requireNonNull(loginUser).getStrPassword())) {
                    userName = binding.etMobileNo.getText().toString();
                    LoingApi();
//                    binding.sellerTv.setText(loginUser.getMobileNumber() +"  "+loginUser.getStrPassword());
                    SharedPrefManager.getInstance(mContext).hideSoftKeyBoard(LoginActi.this);
                }
            }
        });


        binding.sellerTv.setOnClickListener(view -> startActivity(new Intent(mContext, Registration.class)));

        binding.forgotTv.setOnClickListener(v -> startActivity(new Intent(mContext,ForgetPassword.class)));

    }

//    public void loginProcess(View view) {
//        userName = binding.etMobileNo.getText().toString();
//        if (Utils.isValidMobileNumber(mContext, userName) && Utils.isPassword(mContext,binding.passwordEt.getText().toString().trim()) ){
//            LoingApi();
//        }
//    }

    public void LoingApi() {

        HashMap objectNew = new HashMap();
        objectNew.put("mobile", userName);
        objectNew.put("password", binding.passwordEt.getText().toString().trim());
//        objectNew.put("device_token",SharedPrefManager.getDeviceToken(Constrants.Token));
        System.out.println("Login_obj: " + objectNew+"");
        new WebTask(LoginActi.this, WebUrls.BASE_URL + WebUrls.Login, objectNew, LoginActi.this, RequestCode.CODE_Login, 1);

    }

    @Override
    public void onComplete(String response, int taskcode) {

        if (RequestCode.CODE_Login == taskcode) {
            System.out.println("Login_res: " + response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {

//                    SharedPrefManager.setImagePath(Constrants.ImagePath,jsonObject.optString("image_path"));
                    JSONObject dataObj = jsonObject.optJSONArray("data").optJSONObject(0);

                    SharedPrefManager.setUserID(Constrants.UserId, dataObj.optString("id"));
                    SharedPrefManager.setUserName(Constrants.UserName, dataObj.optString("username"));
                    SharedPrefManager.setUserEmail(Constrants.UserEmail, dataObj.optString("email"));
                    SharedPrefManager.setMobile(Constrants.UserMobile, dataObj.optString("mobile"));
                    SharedPrefManager.setProfilePic(Constrants.UserPic, dataObj.optJSONObject("user_kyc").optString("seller_image"));
                    SharedPrefManager.setAddress(Constrants.UserAddress, dataObj.optJSONObject("user_kyc").optString("address_1"));
                    SharedPrefManager.setBusinessName(Constrants.BusinessName, dataObj.optJSONObject("user_kyc").optString("business_name"));
                    SharedPrefManager.setResponse(Constrants.Response, response);
                    if (dataObj.optString("verify_status").equalsIgnoreCase("verified")) {
                        SharedPrefManager.setLogin(Constrants.IsLogin, true);
                        Tosat(this, jsonObject.optString("message"));
                        startActivity(new Intent(LoginActi.this, Dashboard.class));
                        finish();
                    } else if (dataObj.optString("verify_status").equalsIgnoreCase("kyc_completed")) {
                        startActivity(new Intent(LoginActi.this, Waiting.class));
                    }else {
                        startActivity(new Intent(LoginActi.this, ProfileComplete.class)
                                .putExtra("user_kyc",dataObj.optJSONObject("user_kyc")+""));
                    }
                } else {
                    Tosat(this, jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
