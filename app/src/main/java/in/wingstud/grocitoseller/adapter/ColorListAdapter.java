package in.wingstud.grocitoseller.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.wingstud.grocitoseller.R;
import in.wingstud.grocitoseller.activity.ProductUpload;
import in.wingstud.grocitoseller.databinding.ColorItemBinding;
import in.wingstud.grocitoseller.databinding.ColorSelectItemBinding;
import in.wingstud.grocitoseller.model.ColorModel;
import in.wingstud.grocitoseller.util.Utils;

public class ColorListAdapter extends RecyclerView.Adapter<ColorListAdapter.ViewHolder> {

    Context context;
    ArrayList<ColorModel> arrayList = new ArrayList<>();
    private final ArrayList<Integer> seleccionados = new ArrayList<>();
    public static ArrayList<String> colorCodeArray = new ArrayList<>();
    public static ArrayList<String> colorArray = new ArrayList<>();

    public ColorListAdapter(Context context, ArrayList<ColorModel> arrayList, ArrayList<String> colorArray) {
        this.context = context;
        this.arrayList = arrayList;
        this.colorArray = colorArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ColorSelectItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.color_select_item, parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("color_code", arrayList.get(position).getCode() + "");


        if (!arrayList.get(position).getCode().equals("null")) {
            if (colorArray.size() > 0) {
                for (int i = 0; i < colorArray.size(); i++) {
                    if (colorArray.get(i).equals(arrayList.get(position).getCode())) {
                        seleccionados.add(position);
                        colorCodeArray.add(arrayList.get(position).getCode());
                        holder.binding.colorlistRl.setBackground(context.getResources().getDrawable(R.drawable.color_primary_rounded_border));
                    }
                }

            }
            Utils.shapeBackground(holder.binding.colorCode, arrayList.get(position).getCode());
            holder.binding.colorNameRv.setText(String.format("%s(%s)"
                    , arrayList.get(position).getColor_name()
                    , arrayList.get(position).getCode())
            );
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ColorSelectItemBinding binding;

        public ViewHolder(@NonNull ColorSelectItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.colorlistRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ProductUpload.isClickable) {
                       //sdfa
                    } else {
                        int position = getAdapterPosition();
                        if (seleccionados.contains(position)) {
                            seleccionados.remove((Integer) position);
                            colorCodeArray.remove(arrayList.get(position).getCode());
                            binding.colorlistRl.setBackground(context.getResources().getDrawable(R.drawable.rounded_colorlist_bg));
                        } else {
                            seleccionados.add(position);
                            colorCodeArray.add(arrayList.get(position).getCode());
                            binding.colorlistRl.setBackground(context.getResources().getDrawable(R.drawable.color_primary_rounded_border));
                        }
                    }
                }
            });

        }
    }
}
