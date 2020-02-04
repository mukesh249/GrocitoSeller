package in.wingstud.grocitoseller.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

import in.wingstud.grocitoseller.Common.Constrants;
import in.wingstud.grocitoseller.Common.SharedPrefManager;
import in.wingstud.grocitoseller.R;
import in.wingstud.grocitoseller.databinding.AddressviewLayoutBinding;
import in.wingstud.grocitoseller.databinding.OrderProductItemBinding;
import in.wingstud.grocitoseller.model.OrderProductModel;
import in.wingstud.grocitoseller.util.Utils;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder> {


    ArrayList<OrderProductModel> arrayList = new ArrayList<>();
    Context context;

    public OrderProductAdapter(ArrayList<OrderProductModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderProductItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.order_product_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderProductModel newOrderModel = arrayList.get(position);

        holder.binding.productNameTv.setText(newOrderModel.getName());
        holder.binding.weightTv.setText(newOrderModel.getWeight());
        holder.binding.amountTv.setText(newOrderModel.getPrice());
        holder.binding.qtyTb.setText(newOrderModel.getQty());
        holder.binding.statusTv.setText(newOrderModel.getStatus());

        if (newOrderModel.returnStatus == 1) {
            holder.binding.restatusTv.setVisibility(View.VISIBLE);
            holder.binding.restatusTv.setText("Item Retuned");
        }else if (newOrderModel.exchangeStatus == 1) {
            holder.binding.restatusTv.setVisibility(View.VISIBLE);
            holder.binding.restatusTv.setText("Item Exchanged");
        } else {
            holder.binding.restatusTv.setVisibility(View.GONE);
        }

        if (newOrderModel.getStatus().equalsIgnoreCase("delivered")) {
            holder.binding.dateLL.setVisibility(View.VISIBLE);
            holder.binding.dateTV.setText(newOrderModel.getCreated_at());
        }else {
            holder.binding.dateLL.setVisibility(View.GONE);
        }
        Utils.setImage(context, holder.binding.productImage, SharedPrefManager.getImagePath(Constrants.ImagePath) + "/" + newOrderModel.getProduct_image());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        OrderProductItemBinding binding;

        public ViewHolder(@NonNull OrderProductItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    private void addressDialog(JSONObject addressObj) {
        Utils.dismissRetryAlert();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AddressviewLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.addressview_layout, null, false);
        builder.setView(binding.getRoot());

        binding.nameTv.setText(addressObj.optString("name").toUpperCase());
        binding.typeTv.setText(String.format("Typue : %s", addressObj.optString("type")));
        binding.houseTv.setText(String.format("House %s", addressObj.optString("house")));
        binding.streetTv.setText(String.format("Street : %s", addressObj.optString("street")));
        binding.addressTv.setText(String.format("Address : %s", addressObj.optString("address")));
        binding.cityTv.setText(String.format("City : %s", addressObj.optString("city")));
        binding.stateTv.setText(String.format("%s (%s)", addressObj.optString("state"), addressObj.optString("pincode")));

        AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }
}
