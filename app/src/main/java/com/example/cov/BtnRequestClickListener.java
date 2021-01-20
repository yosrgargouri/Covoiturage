package com.example.cov;


import com.example.cov.model.RequestDetail;
import com.example.cov.model.StatusEnum;

public interface BtnRequestClickListener {
    public abstract void onBtnClick(RequestDetail request, StatusEnum status);
}
