package com.simorgh.pregnancyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.simorgh.database.model.ArticleWithParagraph;
import com.simorgh.database.model.Paragraph;
import com.simorgh.expandablelayout.ExpandableLayout;
import com.simorgh.pregnancyapp.R;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends ListAdapter<ArticleWithParagraph, CategoryAdapter.ArticleViewHolder> {
    private ItemClickListener itemClickListener;


    public CategoryAdapter(@NonNull DiffUtil.ItemCallback<ArticleWithParagraph> diffCallback, ItemClickListener itemClickListener) {
        super(diffCallback);
        this.itemClickListener = itemClickListener;
    }

    protected CategoryAdapter(@NonNull AsyncDifferConfig<ArticleWithParagraph> config) {
        super(config);
    }

    private int selectedIndex = -1;

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_summary_layout, parent, false);
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);
        return new ArticleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, final int position) {
        ArticleWithParagraph item = getItem(position);
        if (item != null) {
            TextView title = holder.itemView.findViewById(R.id.tv_title);
            TextView content = holder.itemView.findViewById(R.id.tv_summary);
            ExpandableLayout expand = holder.itemView.findViewById(R.id.expandable_layout);

            title.setText(item.getArticle().getTitle());
            StringBuilder sb = new StringBuilder();
            for (Paragraph p : item.getParagraphs()) {
                sb.append(p.getContent());
                sb.append("\n");
            }
            content.setText(sb);
            if (selectedIndex != -1) {
                if (position != selectedIndex) {
                    if (expand.isExpanded()) {
                        expand.toggle(true);
                    }
                }
            }

            title.setOnClickListener(v -> {
                expand.toggle(true);
                if (selectedIndex != -1) {
                    notifyDataSetChanged();
                }
                selectedIndex = position;
            });

        }
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

    public interface ItemClickListener {

    }


    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class ItemDiffCallBack extends DiffUtil.ItemCallback<ArticleWithParagraph> {

        @Override
        public boolean areItemsTheSame(@NonNull ArticleWithParagraph oldItem, @NonNull ArticleWithParagraph newItem) {
            return oldItem.getArticle().equals(newItem.getArticle());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ArticleWithParagraph oldItem, @NonNull ArticleWithParagraph newItem) {
            return oldItem.getArticle().equals(newItem.getArticle());
        }
    }
}
