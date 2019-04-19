package com.hobbyer.android.viewmodel.activity.event;

import android.os.Build;
import android.preference.Preference;

import com.hobbyer.android.R;
import com.hobbyer.android.prefs.PrefManager;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.view.activities.event.RepetitionActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;

public class ReptitionViewModel extends ActivityViewModel {
    private RepetitionActivity activity;
   String mame;
    public ReptitionViewModel(RepetitionActivity activity) {
        super(activity);
        this.activity=activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
        /*PrefManager.getSharedInstance().SaveId(activity,"","");
        mame=PrefManager.getSharedInstance().getUserBuyerId(activity);*/
    }
}
