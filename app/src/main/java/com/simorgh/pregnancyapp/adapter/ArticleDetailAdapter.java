package com.simorgh.pregnancyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simorgh.database.util.ArticleViewSubItem;
import com.simorgh.pregnancyapp.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleDetailAdapter extends ListAdapter<ArticleViewSubItem, ArticleDetailAdapter.ArticleSubItemViewHolder> {
    public ArticleDetailAdapter(@NonNull DiffUtil.ItemCallback<ArticleViewSubItem> diffCallback) {
        super(diffCallback);
    }

    protected ArticleDetailAdapter(@NonNull AsyncDifferConfig<ArticleViewSubItem> config) {
        super(config);
    }

    @NonNull
    @Override
    public ArticleSubItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case 0:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
                break;
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paragraph, parent, false);
                break;
            case 2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paragraph, parent, false);
        }
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);
        return new ArticleSubItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleSubItemViewHolder holder, final int position) {
        ArticleViewSubItem item = getItem(position);
        if (item != null) {
            switch (item.getType()) {
                case title:
                    TextView title = holder.itemView.findViewById(R.id.content);
                    title.setText(item.getContent());
                    break;
                case image:
                    ImageView imageView = holder.itemView.findViewById(R.id.content);
                    String mDrawableName = item.getContent();
                    Context context = holder.itemView.getContext();
                    int resID = context
                            .getResources()
                            .getIdentifier(mDrawableName, "drawable", Objects.requireNonNull(context.getPackageName()));
                    imageView.setImageResource(resID);
                    break;
                case paragraph:
                    title = holder.itemView.findViewById(R.id.content);
                    title.setText(item.getContent());
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType().ordinal();
    }

    public class ArticleSubItemViewHolder extends RecyclerView.ViewHolder {
        ArticleSubItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class ItemDiffCallBack extends DiffUtil.ItemCallback<ArticleViewSubItem> {

        @Override
        public boolean areItemsTheSame(@NonNull ArticleViewSubItem Answer, @NonNull ArticleViewSubItem newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ArticleViewSubItem oldItem, @NonNull ArticleViewSubItem newItem) {
            return false;
        }
    }
}
