package in.wingstud.grocitoseller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

import in.wingstud.grocitoseller.Api.JsonDeserializer;
import in.wingstud.grocitoseller.Api.RequestCode;
import in.wingstud.grocitoseller.Api.WebCompleteTask;
import in.wingstud.grocitoseller.Api.WebTask;
import in.wingstud.grocitoseller.Api.WebUrls;
import in.wingstud.grocitoseller.Common.Constrants;
import in.wingstud.grocitoseller.Common.SharedPrefManager;
import in.wingstud.grocitoseller.R;
import in.wingstud.grocitoseller.databinding.ActivityForgetPasswordBinding;
import in.wingstud.grocitoseller.model.CommanResponse;

public class ForgetPassword extends AppCompatActivity implements WebCompleteTask {

    private ActivityForgetPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_forget_password);


        binding.btnSubmit.setOnClickListener(v -> forgotPassword());

        binding.backIv.setOnClickListener(v -> finish());

    }
    public void forgotPassword() {

        HashMap objectNew = new HashMap();
        objectNew.put("mobile", binding.etMobileNo.getText().toString());

        new WebTask(ForgetPassword.this, WebUrls.BASE_URL + WebUrls.SellerForgotPassword, objectNew,
                ForgetPassword.this, RequestCode.CODE_ForgetPassword, 1);

    }

    @Override
    public void onComplete(String response, int taskcode) {

        if (RequestCode.CODE_ForgetPassword==taskcode){
            System.out.println("ForgetPassword_res: " + response);
            CommanResponse commanResponse = JsonDeserializer.deserializeJson(response,CommanResponse.class);
            if (commanResponse.statusCode==1){
                startActivity(new Intent(ForgetPassword.this, OtpActi.class)
                        .putExtra("mobile",binding.etMobileNo.getText().toString()+"")
                        .putExtra("otp",commanResponse.otp+"")
                );
                finish();
            }
        }

    }
}
