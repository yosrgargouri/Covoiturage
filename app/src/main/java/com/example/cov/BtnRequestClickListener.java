package com.example.cov;


import com.example.cov.model.RequestDetail;

public interface BtnRequestClickListener {
    public abstract void onBtnClick(RequestDetail request, String status);
}
