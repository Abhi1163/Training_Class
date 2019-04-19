package com.hobbyer.android.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.pastprofile.PastUserViewContentList;
import com.hobbyer.android.databinding.RowPastProfileBinding;
import com.hobbyer.android.interfaces.ProfileInterface;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.ToastUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterPast extends RecyclerView.Adapter<AdapterPast.MyViewHolder> {
    private Context context;
    private List<PastUserViewContentList> pastModelList;
    private String starttime;
    private Date dateTime;
    private   String Meridien;
    private ProfileInterface onItemClick;


    public AdapterPast(Context context, List<PastUserViewContentList> contentLists, ProfileInterface profileInterface) {
        this.context = context;
        this.pastModelList = contentLists;
        this.onItemClick=profileInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_past_profile, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (pastModelList != null) {




            String input=pastModelList.get(i).getStartTime();


            //Date/time pattern of input date
            DateFormat df = new SimpleDateFormat("HH:mm:ss");
            //Date/time pattern of desired output date
            DateFormat outputformat = new SimpleDateFormat("hh:mm aa");
            Date date = null;
            String output = null;
            try{
                //Conversion of input String to date
                date= df.parse(input);
                //old date format to new date format
                output = outputformat.format(date);
                System.out.println(output);
            }catch(ParseException pe){
                pe.printStackTrace();
            }





            SimpleDateFormat month_date = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String actualDate = pastModelList.get(i).getStartDate();
            try {
                dateTime = sdf.parse(actualDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String month_name = month_date.format(dateTime);
            System.out.println("Month :" + month_name);


            //for time
            String time=pastModelList.get(i).getStartTime();
            String result = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                result = LocalTime.parse(time).format(DateTimeFormatter.ofPattern("h:mm a"));
            }
            myViewHolder.rowPastProfileBinding.tvDate.setText(month_name);
            myViewHolder.rowPastProfileBinding.tvAddress.setText(pastModelList.get(i).getAddress());
            myViewHolder.rowPastProfileBinding.tvName.setText(pastModelList.get(i).getClassName());
            myViewHolder.rowPastProfileBinding.tvTime.setText(output);
            myViewHolder.rowPastProfileBinding.btFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onItemClick(view,i);
                }
            });



        }

    }




    @Override
    public int getItemCount() {
        return pastModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowPastProfileBinding rowPastProfileBinding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rowPastProfileBinding = DataBindingUtil.bind(itemView);
        }
    }
}
