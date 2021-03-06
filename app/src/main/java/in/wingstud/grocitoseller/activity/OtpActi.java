package in.wingstud.grocitoseller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import java.util.HashMap;

import in.wingstud.grocitoseller.Api.JsonDeserializer;
import in.wingstud.grocitoseller.Api.RequestCode;
import in.wingstud.grocitoseller.Api.WebCompleteTask;
import in.wingstud.grocitoseller.Api.WebTask;
import in.wingstud.grocitoseller.Api.WebUrls;
import in.wingstud.grocitoseller.R;
import in.wingstud.grocitoseller.databinding.ActivityOtpBinding;
import in.wingstud.grocitoseller.model.CommanResponse;
import in.wingstud.grocitoseller.util.Utils;

public class OtpActi extends AppCompatActivity implements WebCompleteTask {

    private Toolbar toolbar;
    private Context mContext;
    private ActivityOtpBinding binding;

    String otp, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);

        mContext = OtpActi.this;
        if (getIntent().getExtras() != null) {

            otp = getIntent().getExtras().getString("otp", "");
            mobile = getIntent().getExtras().getString("mobile", "");
        }
        initialize();

//
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(task -> {
//                    if (!task.isSuccessful()) {
//                        Log.w("Failed", "getInstanceId failed", task.getException());
//                        return;
//                    }
//
//                    // Get new Instance ID token
//                    String token = task.getResult().getToken();
//
//                    // Log and toast
////                        String msg = getString(R.string.msg_token_fmt, token);
//                    Log.e("NEW_TOKEN", token);
//                    SharedPrefManager.setDeviceToken(Constrants.Token,token);
////                        Toast.makeText(OtpActi.this, token, Toast.LENGTH_SHORT).show();
//                });
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_white));

        toolbar.setNavigationOnClickListener(v -> finish());

    }

    public void verifyProcess(View view) {

        Utils.PrintMsg("mobile: " + mobile);
        Utils.PrintMsg("otp: " + otp);
        if (binding.pinView.getValue().toString().equals(otp))
            OtpVerify(mobile);
        else
            Utils.Tosat(OtpActi.this, "Please enter valid otp");

    }

    public void resendProcess(View view) {
        OtpResend(mobile);
    }

    public void OtpVerify(String mobile) {

        HashMap objectNew = new HashMap();
        objectNew.put("mobile", mobile);
        Utils.PrintMsg("Otp_vefity_obj: " + objectNew);

        new WebTask(OtpActi.this, WebUrls.BASE_URL + WebUrls.VerifyForgotPasswordOtp,
                objectNew, OtpActi.this, RequestCode.CODE_VerifyForgotPasswordOtp, 1);

    }

    public void OtpResend(String mobile) {

        HashMap objectNew = new HashMap();
        objectNew.put("mobile", mobile);
        Utils.PrintMsg("OtpResend_obje: " + objectNew);
        new WebTask(OtpActi.this, WebUrls.BASE_URL + WebUrls.SellerForgotPassword, objectNew,
                OtpActi.this, RequestCode.CODE_ForgetPassword, 1);
    }


    @Override
    public void onComplete(String response, int taskcode) {

        if (RequestCode.CODE_VerifyForgotPasswordOtp == taskcode) {
            System.out.println("Otp_res: " + response);
            CommanResponse commanResponse = JsonDeserializer.deserializeJson(response, CommanResponse.class);
            if (commanResponse.statusCode == 1) {
                Utils.Tosat(OtpActi.this, commanResponse.message);
                startActivity(new Intent(OtpActi.this, LoginActi.class));
                finish();
            }
        }

        if (RequestCode.CODE_ForgetPassword == taskcode) {
            System.out.println("Otp_resend_res: " + response);
            CommanResponse commanResponse = JsonDeserializer.deserializeJson(response, CommanResponse.class);
            if (commanResponse.statusCode == 1) {
                Utils.Tosat(OtpActi.this, commanResponse.message);
                otp = commanResponse.otp;
            }
        }
    }
}
