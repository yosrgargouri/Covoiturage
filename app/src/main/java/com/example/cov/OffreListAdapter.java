package com.example.cov;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;


import com.example.cov.model.OffreDetail;

import java.util.ArrayList;

public class OffreListAdapter extends ArrayAdapter<OffreDetail> {

    private static final String TAG = "OffreListAdapter";

    private String key;
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    private BtnClickListener mClickRequestListener = null;
    Button buttonRequest2;


    //Details:
    TextView emailDetail;
    TextView fullNameDetail;
    TextView telephoneDetail;
    TextView adresseDestinationDetail;
    TextView adresseDepartDetail;
    TextView heureDepartDetail;
    TextView nombrePlaceDetail;
    TextView prixDetail;
    TextView descriptionDetail;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView email;
        TextView fullName;
        TextView telephone;
        TextView adresseDestination;
        TextView adresseDepart;
        TextView heureDepart;
        TextView nombrePlace;
        TextView prix;
        TextView description;
        Button buttonDetails;
        Button buttonRequest;
    }

    /**
     * Default constructor for the OffreListAdapter
     *
     * @param context
     * @param resource
     * @param objects
     */
    public OffreListAdapter(Context context, int resource, ArrayList<OffreDetail> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    public OffreListAdapter(Context context, int resource, ArrayList<OffreDetail> objects, BtnClickListener clickRequestListener) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mClickRequestListener = clickRequestListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        //create the view result for showing the animation
        final View result;

        //get the Offres information
        String email = getItem(position).getEmail();
        String fullName = getItem(position).getFull_name();
        Integer telephone = getItem(position).getTelephone();
        String adresseDestination = getItem(position).getAdresse_destination();
        String adresseDepart = getItem(position).getAdresse_depart();
        String heureDepart = getItem(position).getHeure_depart();
        Integer nombrePlace = getItem(position).getNombre_place();
        Integer prix = getItem(position).getPrix();
        String description = getItem(position).getDescription();

        //Create the Offre object with the information
        OffreDetail offre = new OffreDetail(fullName, prix, heureDepart, description, telephone, nombrePlace, adresseDepart, adresseDestination, email);
        offre.setKey(getItem(position).getKey());


        //ViewHolder object
        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.email = (TextView) convertView.findViewById(R.id.email);
            holder.fullName = (TextView) convertView.findViewById(R.id.fullName2);
            holder.telephone = (TextView) convertView.findViewById(R.id.telephone);
            holder.adresseDestination = (TextView) convertView.findViewById(R.id.adresseDestination);
            holder.adresseDepart = (TextView) convertView.findViewById(R.id.adresseDepart);
            holder.heureDepart = (TextView) convertView.findViewById(R.id.heureDepart);
            holder.nombrePlace = (TextView) convertView.findViewById(R.id.destinationCitySh);
            holder.prix = (TextView) convertView.findViewById(R.id.prix);
//            TODO
//            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.buttonDetails = (Button) convertView.findViewById(R.id.btnShowDetails);
            holder.buttonRequest = (Button) convertView.findViewById(R.id.btnSendRequest);


            result = convertView;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        lastPosition = position;

        holder.email.setText(offre.getEmail());
        holder.fullName.setText(offre.getFull_name());
//        holder.telephone.setText(offre.getTelephone() != null ? offre.getTelephone().toString() : null);
        holder.adresseDestination.setText(offre.getAdresse_destination());
        holder.adresseDepart.setText(offre.getAdresse_depart());
        holder.heureDepart.setText(offre.getNombre_place() != null ? offre.getHeure_depart() : null);
        holder.nombrePlace.setText(offre.getNombre_place() != null ? offre.getNombre_place().toString() : null);
//        holder.prix.setText(offre.getPrix().toString());

        holder.buttonDetails.setTag(position); //For passing the list item index
        holder.buttonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //  if(mClickListener != null){
                Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.detail);
                heureDepartDetail = (TextView) dialog.findViewById(R.id.emailOffre);
                heureDepartDetail.setText(offre.getHeure_depart());
                adresseDepartDetail = (TextView) dialog.findViewById(R.id.adresseDepartDetail);
                adresseDepartDetail.setText(offre.getAdresse_depart());
                adresseDestinationDetail = (TextView) dialog.findViewById(R.id.adresseDestinationDetail);
                adresseDestinationDetail.setText(offre.getAdresse_destination());
                prixDetail = (TextView) dialog.findViewById(R.id.prixDetail);
                prixDetail.setText(offre.getPrix() != null ? offre.getPrix().toString() : null);
                emailDetail = (TextView) dialog.findViewById(R.id.emailDetail);
                emailDetail.setText(offre.getEmail());
                fullNameDetail = (TextView) dialog.findViewById(R.id.fullNameDetail);
                fullNameDetail.setText(offre.getFull_name());
                descriptionDetail = (TextView) dialog.findViewById(R.id.descriptionDetail);
                descriptionDetail.setText(offre.getDescription());
                nombrePlaceDetail = (TextView) dialog.findViewById(R.id.nombrePlaceDetail);
                nombrePlaceDetail.setText(offre.getNombre_place() != null ? offre.getNombre_place().toString() : null);
                telephoneDetail = (TextView) dialog.findViewById(R.id.telephoneDetail);
                telephoneDetail.setText(offre.getTelephone() != null ? offre.getTelephone().toString() : null);
                dialog.show();
                EditText numberPlaceToRequest = dialog.findViewById(R.id.numberPlaceToRequest);
                buttonRequest2 = dialog.findViewById(R.id.btnSendRequest);
                //buttonRequest2.setTag();
                buttonRequest2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                 TODO Auto-generated method stub
                        if (mClickRequestListener != null) {
                            mClickRequestListener.onBtnClick(offre, offre.getKey(), numberPlaceToRequest, dialog);
                        }
                    }
                });
                // } else
                //     mClickListener.onBtnClick((Integer) v.getTag());
            }
        });

        return convertView;
    }

}
