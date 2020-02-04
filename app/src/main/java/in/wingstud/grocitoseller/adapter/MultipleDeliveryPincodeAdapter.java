package in.wingstud.grocitoseller.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.wingstud.grocitoseller.R;
import in.wingstud.grocitoseller.databinding.ColorItemBinding;
import in.wingstud.grocitoseller.databinding.MulitiplePincodeItemBinding;
import in.wingstud.grocitoseller.model.SelectedPinModel;

public class MultipleDeliveryPincodeAdapter extends RecyclerView.Adapter<MultipleDeliveryPincodeAdapter.ViewHolder> {

    Context context;
    ArrayList<SelectedPinModel> arrayList;

    public MultipleDeliveryPincodeAdapter(Context context, ArrayList<SelectedPinModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MulitiplePincodeItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.mulitiple_pincode_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("color_code", arrayList.get(position) + "");
        SelectedPinModel selectedPinModel = arrayList.get(position);
        holder.binding.deliveryPin.setText(selectedPinModel.getPincode());

        holder.binding.clearIv.setOnClickListener(v -> {
            if (arrayList.size()>1) {
                arrayList.remove(position);
                notifyItemRemoved(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MulitiplePincodeItemBinding binding;

        public ViewHolder(@NonNull MulitiplePincodeItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
