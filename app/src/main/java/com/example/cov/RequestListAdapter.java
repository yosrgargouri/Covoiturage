package com.example.cov;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cov.model.RequestDetail;

import java.util.ArrayList;

public class RequestListAdapter extends ArrayAdapter<RequestDetail> {

    private static final String TAG = "OffreListAdapter";

    private String key;
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    private BtnClickListener mClickAcceptListener = null;
    private BtnClickListener mClickCancelListener = null;


    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView titleOffre;
        TextView emailOffre;
        TextView nombrePlaceDetail2;


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
    }

    public RequestListAdapter(Context context, int resource, ArrayList<RequestDetail> objects, BtnClickListener clickRequestListener) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
//        mClickRequestListener = clickRequestListener;
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

        //ViewHolder object
        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.titleOffre = (TextView) convertView.findViewById(R.id.titleOffre);
            holder.emailOffre = (TextView) convertView.findViewById(R.id.emailOffre);
            holder.nombrePlaceDetail2 = (TextView) convertView.findViewById(R.id.nombrePlaceDetail2);

            result = convertView;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        lastPosition = position;

        holder.titleOffre.setText(requestDetail.getTitleOffre());
        holder.emailOffre.setText(requestDetail.getEmail_request());
        holder.nombrePlaceDetail2.setText(requestDetail.getNombre_place() != null ? requestDetail.getNombre_place().toString() : null);

        return result;
    }

}
