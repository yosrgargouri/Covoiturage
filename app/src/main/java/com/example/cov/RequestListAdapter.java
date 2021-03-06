package com.example.cov;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cov.model.RequestDetail;
import com.example.cov.model.StatusEnum;

import java.util.ArrayList;

public class RequestListAdapter extends ArrayAdapter<RequestDetail> {

    private static final String TAG = "OffreListAdapter";

    private boolean onlyDetail = false;
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    private BtnRequestClickListener mClickAcceptListener = null;
    private BtnRequestClickListener mClickCancelListener = null;


    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView titleOffre;
        TextView emailOffre;
        TextView status;
        TextView nombrePlaceDetail2;
        Button buttonAccept;
        Button buttonReject;
        LinearLayout layoutButton;
        LinearLayout layoutStatus;

        TextView nombrePlace;
    }

    /**
     * Default constructor for the OffreListAdapter
     *
     * @param context
     * @param resource
     * @param objects
     */
    public RequestListAdapter(Context context, int resource, ArrayList<RequestDetail> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        onlyDetail = true;
    }

    public RequestListAdapter(Context context, int resource, ArrayList<RequestDetail> objects, BtnRequestClickListener clickAcceptListener, BtnRequestClickListener clickCancelListener) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mClickAcceptListener = clickAcceptListener;
        mClickCancelListener = clickCancelListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        //create the view result for showing the animation
        final View result;

        //get the Offres information
        String titleOffre = getItem(position).getTitleOffre();
        String email = getItem(position).getEmail_request();
        Integer nombrePlace = getItem(position).getNombre_place();

        //Create the Offre object with the information
        RequestDetail requestDetail = new RequestDetail(titleOffre, email, nombrePlace);
        requestDetail.setOffre_key(getItem(position).getOffre_key());
        requestDetail.setRequest_key(getItem(position).getRequest_key());
        requestDetail.setOffre(getItem(position).getOffre());
        requestDetail.setStatus(getItem(position).getStatus());

        //ViewHolder object
        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.titleOffre = (TextView) convertView.findViewById(R.id.titleOffre);
            holder.emailOffre = (TextView) convertView.findViewById(R.id.emailOffre);
            holder.nombrePlaceDetail2 = (TextView) convertView.findViewById(R.id.nombrePlaceDetail2);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.buttonAccept = (Button) convertView.findViewById(R.id.btnAccept);
            holder.buttonReject = (Button) convertView.findViewById(R.id.btnCancel);
            holder.layoutButton = (LinearLayout) convertView.findViewById(R.id.layoutButton);
            holder.layoutStatus = (LinearLayout) convertView.findViewById(R.id.layoutStatus);

            if (onlyDetail) {
//                ViewGroup.LayoutParams params = holder.LinearLayoutParent.getLayoutParams();
//                // Changes the height and width to the specified *pixels*
//                params.height = 350;
                holder.layoutButton.setVisibility(View.INVISIBLE);
                holder.layoutStatus.setVisibility(View.VISIBLE);
//                holder.buttonAccept.setVisibility(View.INVISIBLE);
//                holder.buttonReject.setVisibility(View.INVISIBLE);
            }
            result = convertView;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        lastPosition = position;

        holder.titleOffre.setText(requestDetail.getTitleOffre());
        holder.emailOffre.setText(requestDetail.getEmail_request());
        if(onlyDetail){
            holder.emailOffre.setText(getItem(position).getOffre().getEmail());
            holder.status.setText(requestDetail.getStatus());
        }
        holder.nombrePlaceDetail2.setText(requestDetail.getNombre_place() != null ? requestDetail.getNombre_place().toString() : null);
        if (!onlyDetail) {

            holder.buttonAccept.setTag(position); //For passing the list item index
            holder.buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickAcceptListener != null) {
                        mClickAcceptListener.onBtnClick(requestDetail, StatusEnum.ACCEPTED);
                    }
                }
            });

            holder.buttonReject.setTag(position); //For passing the list item index
            holder.buttonReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickCancelListener != null) {
                        mClickCancelListener.onBtnClick(requestDetail, StatusEnum.REFUSED);
                    }
                }
            });
        }

        return result;
    }

}
