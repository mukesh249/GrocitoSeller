package in.wingstud.grocitoseller.fragment;


import android.content.Context;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.wingstud.grocitoseller.Api.RequestCode;
import in.wingstud.grocitoseller.Api.WebCompleteTask;
import in.wingstud.grocitoseller.Api.WebTask;
import in.wingstud.grocitoseller.Api.WebUrls;
import in.wingstud.grocitoseller.Common.Constrants;
import in.wingstud.grocitoseller.Common.SharedPrefManager;
import in.wingstud.grocitoseller.R;
import in.wingstud.grocitoseller.activity.Dashboard;
import in.wingstud.grocitoseller.activity.PendingPayment;
import in.wingstud.grocitoseller.activity.PerviousPayment;
import in.wingstud.grocitoseller.databinding.PaymentBinding;

import static in.wingstud.grocitoseller.util.Utils.Tosat;

/**
 * A simple {@link Fragment} subclass.
 */
public class Payment extends Fragment implements WebCompleteTask {

    private Context mContext;
    private View view;
    private PaymentBinding binding;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.payment, container, false);
        view = binding.getRoot();

        binding.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PerviousPayment.class).putExtra("act","today"));
            }
        });
        binding.pendViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PendingPayment.class));
            }
        });
        binding.paidViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PerviousPayment.class).putExtra("act","paid"));
            }
        });
        PaymentOverView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Dashboard) getContext()).setTitle(mContext.getString(R.string.payment_overview), false);
    }


    public void PaymentOverView(){

        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));

        new WebTask(getActivity(), WebUrls.BASE_URL+ WebUrls.PaymentOverView,objectNew, Payment.this, RequestCode.CODE_PaymentOverView,1);

    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_PaymentOverView == taskcode){
            System.out.println("PaymentOverView_res: "+response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code")==1){
                    JSONObject dataObj = jsonObject.optJSONObject("data");
                    binding.amountTv.setText(String.format("%.2f",dataObj.optDouble("today_payemnt")));
                    binding.pendAmountTv.setText(String.format("%.2f",dataObj.optDouble("pending_amount")));
                    binding.paidAmountTv.setText(String.format("%.2f",dataObj.optDouble("paid_amount")));

                }else {
                    Tosat(getActivity(),jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
