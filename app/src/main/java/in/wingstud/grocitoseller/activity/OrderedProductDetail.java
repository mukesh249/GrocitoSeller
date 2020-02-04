package in.wingstud.grocitoseller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.wingstud.grocitoseller.Api.RequestCode;
import in.wingstud.grocitoseller.Api.WebCompleteTask;
import in.wingstud.grocitoseller.Api.WebTask;
import in.wingstud.grocitoseller.Api.WebUrls;
import in.wingstud.grocitoseller.Common.Constrants;
import in.wingstud.grocitoseller.Common.SharedPrefManager;
import in.wingstud.grocitoseller.R;
import in.wingstud.grocitoseller.adapter.OrderProductAdapter;
import in.wingstud.grocitoseller.databinding.OrderProductItemBinding;
import in.wingstud.grocitoseller.databinding.OrderedProductDetailBinding;
import in.wingstud.grocitoseller.fragment.NewOrder;
import in.wingstud.grocitoseller.model.NewOrderModel;
import in.wingstud.grocitoseller.model.OrderProductModel;

import static in.wingstud.grocitoseller.util.Utils.Tosat;


public class OrderedProductDetail extends AppCompatActivity implements WebCompleteTask {

    OrderedProductDetailBinding binding;
    ArrayList<OrderProductModel> arrayList = new ArrayList<>();
    OrderProductAdapter adapter;
    String id="",order_id="",activity="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.ordered_product_detail);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderProductAdapter(arrayList,OrderedProductDetail.this);
        binding.recyclerView.setAdapter(adapter);

        if (getIntent().getExtras()!=null){
            order_id = getIntent().getExtras().getString("order_no","");
            binding.orderidTv.setText(String.format("Order Id : %s",order_id));

            id = getIntent().getExtras().getString("id","");
            activity = getIntent().getExtras().getString("activity","");

            if (activity.equals("neworder")){
                binding.pcTv.setText(getResources().getString(R.string.product_charges));
                binding.spTv.setText(getResources().getString(R.string.shipping_charges));
                OrderProductList();
            }
            if (activity.equals("payover")){
                binding.pcTv.setText(getResources().getString(R.string.order_amount_inc));
                binding.spTv.setText(getResources().getString(R.string.gro_commin));
                ProductDetails();
            }
        }
        initialize();
    }
    private void initialize() {
        setSupportActionBar(binding.toolbar.toolbar);
        binding.toolbar.bellRL.setVisibility(View.GONE);
        binding.toolbar.activityTitle.setText(getResources().getString(R.string.product_details));
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

    }
    public void ProductDetails(){
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        objectNew.put("order_id", id);

        System.out.println("ProductDetails_obj: "+objectNew);
        new WebTask(this, WebUrls.BASE_URL+ WebUrls.PaymentOrderDetails,objectNew, OrderedProductDetail.this, RequestCode.CODE_OrderDetailList,1);

    }
    public void OrderProductList(){

        HashMap objectNew = new HashMap();
        objectNew.put("order_id", id);

        System.out.println("OrderDetailList_obj: "+objectNew);
        new WebTask(this, WebUrls.BASE_URL+ WebUrls.OrderDetailList,objectNew, OrderedProductDetail.this, RequestCode.CODE_OrderDetailList,1);

    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_OrderDetailList == taskcode){
            System.out.println("OrderDetailList_res: "+response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code")==1){
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    arrayList.clear();
                    double amt =jsonObject.optDouble("total_amount");

                    double s_chrg = 0;

                    binding.payModeTv.setText(String.format("Payment Mode : %s",jsonObject.optString("payment_mode")));

                    if (activity.equals("neworder")){
                        s_chrg =jsonObject.optDouble("shipping_charge");
                        binding.productCrgTv.setText(String.format("%.2f",amt));
                        binding.shippingCrgTv.setText(String.format("%.2f",s_chrg));
                        binding.totalAmtTv.setText(String.format("Rs. %.2f",amt+s_chrg));

                        binding.deliveredTimeTv.setText(String.format("Delivery : %s,%s"
                                ,jsonObject.optString("delivery_date")
                                ,jsonObject.optString("delivery_time")));
                    }
                    if (activity.equals("payover")){
                        s_chrg =jsonObject.optDouble("gst_amount");
                        binding.productCrgTv.setText(String.format("-Rs. %.2f",jsonObject.optDouble("order_amount")));
                        binding.shippingCrgTv.setText(String.format("-Rs. %.2f",s_chrg));
                        binding.shippingCrgTv.setTextColor(getResources().getColor(R.color.red));
                        binding.totalAmtTv.setText(String.format("Rs. %.2f",amt));
                        binding.deliveredTimeTv.setText(String.format("Shipping : %s",jsonObject.optString("shipped_date")));

                    }
                    binding.deliveryTypeTv.setText(String.format("Delivery Type : %s",jsonObject.optString("delivery_type")));

                    if (dataArray.length()>0){
//                        binding.relEmptyWL.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        for (int i=0;i<dataArray.length();i++){
                            JSONObject dataObj = dataArray.getJSONObject(i);
                            OrderProductModel newOrderModel = new OrderProductModel();
                            newOrderModel.setId(dataObj.optString("id"));
                            newOrderModel.setQty(dataObj.optString("qty"));
                            newOrderModel.setName(dataObj.optString("name"));
                            newOrderModel.setStatus(dataObj.optString("status"));
                            newOrderModel.setCreated_at(dataObj.optString("created_at"));

                            if (activity.equals("neworder")) {
                                newOrderModel.setPrice(String.format("Rs. %.2f(incl. GST)",dataObj.optDouble("price")));
                            }
                            if (activity.equals("payover")){
                                newOrderModel.setPrice(String.format("Rs. %.2f",dataObj.optDouble("price")));
                            }

                            newOrderModel.setReturnStatus(dataObj.optInt("return_status"));
                            newOrderModel.setExchangeStatus(dataObj.optInt("exchange_status"));

                            newOrderModel.setWeight(dataObj.optString("weight"));
                            newOrderModel.setProduct_image(dataObj.optString("product_image"));

                            if (jsonObject.optJSONObject("delivery_address")!=null){
                               JSONObject addressObj = jsonObject.optJSONObject("delivery_address");
                                binding.nameTv.setText(addressObj.optString("name").toUpperCase());
                                binding.typeTv.setText(String.format("Type : %s"
                                        ,addressObj.optString("type")));
                                binding.addressTv.setText(String.format("Address : %s, %s ,%s",
                                        addressObj.optString("house"),
                                        addressObj.optString("street"),
                                        addressObj.optString("address")));
                                binding.cityTv.setText(String.format("City : %s"
                                        ,addressObj.optString("city")));
                                binding.stateTv.setText(String.format("%s (%s)"
                                        ,addressObj.optString("state")
                                        ,addressObj.optString("pincode")));
                            }

                            arrayList.add(newOrderModel);
                        }
                        adapter.notifyDataSetChanged();
                    }else {
//                        binding.relEmptyWL.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    }
                }else {
                    Tosat(OrderedProductDetail.this,jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
