package com.hobbyer.android.interfaces;

import android.view.View;

import com.hobbyer.android.api.response.auth.filter.CategoryListContentList;

public interface OnClickInterFace {

    void onItemClick(View view, int position, CategoryListContentList list);
}
