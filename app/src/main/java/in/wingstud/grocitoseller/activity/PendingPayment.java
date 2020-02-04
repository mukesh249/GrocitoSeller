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
import in.wingstud.grocitoseller.adapter.PendignListAdapter;
import in.wingstud.grocitoseller.databinding.ActivityPendingPaymentBinding;
import in.wingstud.grocitoseller.model.PendingListModel;

import static in.wingstud.grocitoseller.util.Utils.Tosat;

public class PendingPayment extends AppCompatActivity implements WebCompleteTask {

    ActivityPendingPaymentBinding binding;
    PendignListAdapter adapter;
    ArrayList<PendingListModel> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_pending_payment);

        binding.toolbar.activityTitle.setText(getResources().getString(R.string.pending_payment));
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

        binding.paymentPerRV.setLayoutManager(new LinearLayoutManager(PendingPayment.this));
        adapter = new PendignListAdapter(PendingPayment.this,arrayList);
        binding.paymentPerRV.setAdapter(adapter);
        PendPaymentList();
    }
    public void PendPaymentList(){

        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));

        System.out.println("PendPayment_obj: "+objectNew);
        new WebTask(this, WebUrls.BASE_URL+ WebUrls.PendPayment,objectNew, PendingPayment.this, RequestCode.CODE_PendPayment,1);

    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_PendPayment == taskcode) {
            System.out.println("PaidPayment_res: " + response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {
                    JSONObject dataObj = jsonObject.optJSONObject("data");
                    binding.amountTv.setText(String.format("%.2f", dataObj.optDouble("total_amount")));
                    JSONArray listArray = dataObj.optJSONArray("list");
                    arrayList.clear();
                    if (listArray.length() > 0) {
//                        binding.relEmptyWL.setVisibility(View.GONE);
                        binding.paymentPerRV.setVisibility(View.VISIBLE);
                        for (int i = 0; i < listArray.length(); i++) {
                            JSONObject listObj = listArray.getJSONObject(i);
                            PendingListModel paymentPerModel = new PendingListModel();
                            paymentPerModel.setOrder_date(listObj.optString("order_date"));
                            paymentPerModel.setTotal_commission(String.format("%.2f", listObj.optDouble("total_commission")));
                            paymentPerModel.setTotal_payable_amount(String.format("%.2f", listObj.optDouble("total_payable_amount")));
                            paymentPerModel.setTotal_pending_amount(String.format("%.2f", listObj.optDouble("total_pending_amount")));

                            paymentPerModel.setOrder_date(listObj.optString("order_date"));

                            arrayList.add(paymentPerModel);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
//                        binding.relEmptyWL.setVisibility(View.VISIBLE);
                        binding.paymentPerRV.setVisibility(View.GONE);
                    }
                } else {
                    Tosat(PendingPayment.this, jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
