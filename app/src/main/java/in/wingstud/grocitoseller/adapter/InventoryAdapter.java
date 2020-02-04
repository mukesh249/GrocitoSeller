package in.wingstud.grocitoseller.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

import in.wingstud.grocitoseller.Common.Constrants;
import in.wingstud.grocitoseller.Common.SharedPrefManager;
import in.wingstud.grocitoseller.R;
import in.wingstud.grocitoseller.activity.Dashboard;
import in.wingstud.grocitoseller.activity.OrderedProductDetail;
import in.wingstud.grocitoseller.databinding.AddressviewLayoutBinding;
import in.wingstud.grocitoseller.databinding.InventoryItemBinding;
import in.wingstud.grocitoseller.databinding.NewOrderItemBinding;
import in.wingstud.grocitoseller.fragment.ProductUploadList;
import in.wingstud.grocitoseller.model.InventoryModel;
import in.wingstud.grocitoseller.model.NewOrderModel;
import in.wingstud.grocitoseller.util.Utils;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {


    ArrayList<InventoryModel> arrayList = new ArrayList<>();
    Context context;

    public InventoryAdapter(ArrayList<InventoryModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InventoryItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.inventory_item,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InventoryModel model = arrayList.get(position);

        holder.binding.catNameTv.setText(String.format("%s(%s)",model.getName(),model.getCount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductUploadList fragment = new ProductUploadList();
                FragmentManager fm = ((Dashboard)context).getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("from","inventory");
                bundle.putString("cat_id",model.getId());
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.fmContainer,fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        InventoryItemBinding binding;
        public ViewHolder(@NonNull InventoryItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

}
