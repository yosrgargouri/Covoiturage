package com.example.cov;

import android.app.Dialog;
import android.widget.EditText;

import com.example.cov.model.Offre;

public interface BtnClickListener {
    public abstract void onBtnClick(Offre offre, String key, EditText numberPlaceToRequest, Dialog dialog);
}
