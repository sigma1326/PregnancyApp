package com.simorgh.pregnancyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private int fontSize = 14;


    public CategoryAdapter(@NonNull DiffUtil.ItemCallback<ArticleWithParagraph> diffCallback) {
        super(diffCallback);
    }

    protected CategoryAdapter(@NonNull AsyncDifferConfig<ArticleWithParagraph> config) {
        super(config);
    }

    private int selectedIndex = -1;

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_article_summary, parent, false);
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
            content.setTextSize(fontSize);
            title.setTextSize(fontSize);
            if (selectedIndex != -1) {
                if (position != selectedIndex) {
                    if (expand.isExpanded()) {
                        expand.toggle(true);
                    }
                }
            }
            if (selectedIndex == -1 && position == 0) {
                expand.setExpanded(true,true);
                selectedIndex = 0;
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

    public void setFontSize(int size) {
        fontSize = size;
        notifyDataSetChanged();
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
