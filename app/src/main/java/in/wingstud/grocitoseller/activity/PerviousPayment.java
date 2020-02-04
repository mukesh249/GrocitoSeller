package in.wingstud.grocitoseller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.wingstud.grocitoseller.Api.RequestCode;
import in.wingstud.grocitoseller.Api.WebCompleteTask;
import in.wingstud.grocitoseller.Api.WebTask;
import in.wingstud.grocitoseller.Api.WebUrls;
import in.wingstud.grocitoseller.Common.Constrants;
import in.wingstud.grocitoseller.Common.SharedPrefManager;
import in.wingstud.grocitoseller.R;
import in.wingstud.grocitoseller.adapter.PaymentAdapter;
import in.wingstud.grocitoseller.model.PaymentPerModel;
import in.wingstud.grocitoseller.databinding.ActivityPerviousPaymentBinding;

import static in.wingstud.grocitoseller.util.Utils.Tosat;

public class PerviousPayment extends AppCompatActivity implements WebCompleteTask {

    ActivityPerviousPaymentBinding binding;
    PaymentAdapter paymentAdapter;
    private ArrayList<PaymentPerModel> arrayList = new ArrayList<>();

    String act= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_pervious_payment);

        binding.toolbar.activityTitle.setText(getResources().getString(R.string.pre_payment));
        binding.toolbar.bellRL.setVisibility(View.GONE);
        setSupportActionBar(binding.toolbar.toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        binding.toolbar.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_white));
        binding.toolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.paymentPerRV.setLayoutManager(new LinearLayoutManager(PerviousPayment.this));
        paymentAdapter = new PaymentAdapter(PerviousPayment.this,arrayList);
        binding.paymentPerRV.setAdapter(paymentAdapter);

        if (getIntent().getExtras()!=null) {
            act = getIntent().getExtras().getString("act","");
            if (act.equals("today"))
                TodayPaymentList();

            if (act.equals("paid"))
                PaidPaymentList();
        }

    }
    public void TodayPaymentList(){

        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));

        System.out.println("TodayPayment_obj: "+objectNew);
        new WebTask(this, WebUrls.BASE_URL+ WebUrls.TodayPayment,objectNew, PerviousPayment.this, RequestCode.CODE_PaidPayment,1);

    }
    public void PaidPaymentList(){

        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));

        System.out.println("PaidPayment_obj: "+objectNew);
        new WebTask(this, WebUrls.BASE_URL+ WebUrls.PaidPayment,objectNew, PerviousPayment.this, RequestCode.CODE_PaidPayment,1);

    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_PaidPayment == taskcode){
            System.out.println("PaidPayment_res: "+response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code")==1){
                    JSONObject dataObj = jsonObject.optJSONObject("data");
                    binding.amountTv.setText(String.format("%.2f",jsonObject.optDouble("total_amount")));
                    JSONArray listArray = dataObj.optJSONArray("list");
                    arrayList.clear();
                    if (listArray.length()>0){
//                        binding.relEmptyWL.setVisibility(View.GONE);
                        binding.paymentPerRV.setVisibility(View.VISIBLE);
                        for (int i=0;i<listArray.length();i++){
                            JSONObject listObj = listArray.getJSONObject(i);
                            PaymentPerModel paymentPerModel = new PaymentPerModel();
                            paymentPerModel.setOrder_date(listObj.optString("order_date"));
                            paymentPerModel.setTotal_commission(String.format("%.2f",listObj.optDouble("total_commission")));

                            if (act.equals("today"))
                                paymentPerModel.setAmount(String.format("%.2f",listObj.optDouble("total_payable_amount")));

                            if (act.equals("paid"))
                                paymentPerModel.setAmount(String.format("%.2f",listObj.optDouble("amount")));

                            arrayList.add(paymentPerModel);
                        }
                        paymentAdapter.notifyDataSetChanged();
                    }else {
//                        binding.relEmptyWL.setVisibility(View.VISIBLE);
                        binding.paymentPerRV.setVisibility(View.GONE);
                    }
                }else {
                    Tosat(PerviousPayment.this,jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

}
