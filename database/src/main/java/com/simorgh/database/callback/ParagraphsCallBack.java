package com.simorgh.database.callback;

import com.simorgh.database.model.Paragraph;

import java.util.List;

public interface ParagraphsCallBack {
    void onSuccess(List<Paragraph> paragraphs);

    void onFailed(String error);
}
