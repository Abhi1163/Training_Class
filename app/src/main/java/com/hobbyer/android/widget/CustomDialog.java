package com.hobbyer.android.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

public class CustomDialog extends Dialog {

	
	public CustomDialog(Context context, int resourceId) {
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(resourceId);
		this.setCancelable(false);

		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
	}



}
