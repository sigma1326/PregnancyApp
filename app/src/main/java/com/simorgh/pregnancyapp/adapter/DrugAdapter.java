package com.simorgh.pregnancyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.simorgh.database.model.Drug;
import com.simorgh.expandablelayout.ExpansionLayout;
import com.simorgh.pregnancyapp.R;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class DrugAdapter extends ListAdapter<Drug, DrugAdapter.DrugViewHolder> {
    private ItemClickListener itemClickListener;
    private boolean canEdit = true;

    public DrugAdapter(@NonNull DiffUtil.ItemCallback<Drug> diffCallback, ItemClickListener itemClickListener) {
        super(diffCallback);
        this.itemClickListener = itemClickListener;
    }

    protected DrugAdapter(@NonNull AsyncDifferConfig<Drug> config) {
        super(config);
    }

    @NonNull
    @Override
    public DrugViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drug, parent, false);
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);
        return new DrugViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DrugViewHolder holder, final int position) {
        Drug item = getItem(position);
//        holder.itemView.setAlpha(0);
        if (item != null) {
            TextView name = holder.itemView.findViewById(R.id.tv_drug_name);
            TextView description = holder.itemView.findViewById(R.id.tv_description);
            ImageView expand = holder.itemView.findViewById(R.id.img_description);
            ImageView remove = holder.itemView.findViewById(R.id.img_delete);
            ExpansionLayout expandableLayout = holder.itemView.findViewById(R.id.expandable_layout);

            expand.setAlpha(1f);
            expand.setEnabled(true);

            name.setText(item.getDrugName());
            if (item.getInfo() != null && !item.getInfo().isEmpty()) {
                description.setText(item.getInfo());
                expand.setOnClickListener(v -> expandableLayout.toggle(true));
                expand.setAlpha(1f);
                expand.setEnabled(true);
            } else {
                if (expandableLayout.isExpanded()) {
                    expandableLayout.collapse(true);
                }
                expand.setAlpha(0.5f);
                expand.setEnabled(false);
            }

            name.setOnClickListener(v -> {
                if (itemClickListener != null && canEdit) {
                    itemClickListener.selectItemToEdit(item, position);
                }
            });

            remove.animate().alpha(canEdit ? 1f : 0.5f);
            remove.setOnClickListener(v -> {
                if (itemClickListener != null && canEdit) {
                    itemClickListener.removeItem(item.getId(), position);
                }
            });
        }
//        setFadeAnimation(holder.itemView);
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void removeItem(long itemId, int position);

        void selectItemToEdit(Drug drug, int position);
    }


    public class DrugViewHolder extends RecyclerView.ViewHolder {
        DrugViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class ItemDiffCallBack extends DiffUtil.ItemCallback<Drug> {

        @Override
        public boolean areItemsTheSame(@NonNull Drug oldItem, @NonNull Drug newItem) {
            return oldItem.equals(newItem);
//            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Drug oldItem, @NonNull Drug newItem) {
            return oldItem.isSameContent(newItem);
//            return false;
        }
    }
}
