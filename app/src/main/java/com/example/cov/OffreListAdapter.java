package com.example.cov;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.View;


import com.example.cov.model.Offre;

import java.util.ArrayList;

public class OffreListAdapter extends ArrayAdapter<Offre> {

    private static final String TAG = "OffreListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

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
    }

    /**
     * Default constructor for the OffreListAdapter
     *
     * @param context
     * @param resource
     * @param objects
     */
    public OffreListAdapter(Context context, int resource, ArrayList<Offre> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
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
        Offre offre = new Offre(fullName, prix, heureDepart, description, telephone, nombrePlace, adresseDepart, adresseDestination, email);


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
            holder.nombrePlace = (TextView) convertView.findViewById(R.id.nombrePlace);
            holder.prix = (TextView) convertView.findViewById(R.id.prix);
//            TODO
//            holder.description = (TextView) convertView.findViewById(R.id.description);


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


        return convertView;
    }
}
