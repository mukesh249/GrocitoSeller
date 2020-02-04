package in.wingstud.grocitoseller.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.wingstud.grocitoseller.R;
import in.wingstud.grocitoseller.activity.OrderedProductDetail;
import in.wingstud.grocitoseller.databinding.OrderDetailItemBinding;
import in.wingstud.grocitoseller.model.OrderdetailsModel;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<OrderdetailsModel> list;

    public OrderDetailAdapter(Context mContext, ArrayList<OrderdetailsModel> list){
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderDetailItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.order_detail_item, parent,
                false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OrderdetailsModel orderdetailsModel = list.get(position);
        holder.binding.noTv.setText(String.format("%s",(position+1)));
        holder.binding.dateTV.setText(String.format("%s",orderdetailsModel.getDate()));
        holder.binding.orderNoTv.setText(String.format("%s",orderdetailsModel.getOrder_no()));
        holder.binding.statusTv.setText(String.format("%s",orderdetailsModel.getStatus()));
        holder.binding.amountTv.setText(String.format("%s",orderdetailsModel.getPrice()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, OrderedProductDetail.class)
                        .putExtra("id",orderdetailsModel.getOrder_id())
                        .putExtra("order_no",orderdetailsModel.getOrder_no())
                        .putExtra("activity","payover")
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private OrderDetailItemBinding binding;

        public MyViewHolder(final OrderDetailItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
