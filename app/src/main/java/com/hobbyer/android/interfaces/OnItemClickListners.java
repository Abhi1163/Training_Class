package com.hobbyer.android.interfaces;

import android.view.View;

import com.hobbyer.android.api.response.auth.planlist.PlanListData;

public interface OnItemClickListners {
    public void  onItemClick(View view, int postion, PlanListData listData);
}
