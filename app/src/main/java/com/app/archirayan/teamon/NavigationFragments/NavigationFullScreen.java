package com.app.archirayan.teamon.NavigationFragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.retrofit.ApiClient;
import com.app.archirayan.teamon.retrofit.ApiInterface;
import com.app.archirayan.teamon.retrofit.Model.EditEmailDetails;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.archirayan.teamon.Utils.Constant.USERID;
import static com.app.archirayan.teamon.Utils.Utils.ReadSharePrefrence;

/**
 * Created by archirayan on 03-Mar-17.
 */

public class NavigationFullScreen extends Fragment implements View.OnClickListener {

    public TextView addressTv, emailTv, notificationTv, passwordTv, editEmailSubmitTv, pwdSubmitTv, addressUpdateTv, updateSucessMsgTv;
    public ImageView backIv;
    public EditText editEmailEdt, pwdOldPwdEdt, pwdNewPwdEdt, pwdRePwdEdt, addressLine1Edt, addressLine2Edt, zipCodeEdt, mobileNoEdt;
    private FragmentTransaction transaction;
    private ExpandableLayout addressExpandable, emailExpandable, notificationExpandable, passwordExpandable;
    private ViewFlipper emailViewFliper, pwdViewFliper, addressViewFliper;
    private Dialog pd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_navigation_setting_main, null);

        init(view);

        return view;
    }

    private void init(View view) {

        backIv = (ImageView) view.findViewById(R.id.fragment_navigation_setting_main_back);
        updateSucessMsgTv = (TextView) view.findViewById(R.id.fragment_sucessfullyupdate_mail);

        // TODO: 09-Mar-17 main textview
        addressTv = (TextView) view.findViewById(R.id.fragment_navigation_setting_main_address);
        emailTv = (TextView) view.findViewById(R.id.fragment_navigation_setting_main_email);
        notificationTv = (TextView) view.findViewById(R.id.fragment_navigation_setting_main_notification);
        passwordTv = (TextView) view.findViewById(R.id.fragment_navigation_setting_main_password);


        editEmailEdt = (EditText) view.findViewById(R.id.fragment_edit_email);
        editEmailSubmitTv = (TextView) view.findViewById(R.id.fragment_edit_email_submit);
        pwdSubmitTv = (TextView) view.findViewById(R.id.fragment_edit_password_submit);
        pwdNewPwdEdt = (EditText) view.findViewById(R.id.fragment_edit_password_new);
        pwdRePwdEdt = (EditText) view.findViewById(R.id.fragment_edit_password_renew);
        pwdOldPwdEdt = (EditText) view.findViewById(R.id.fragment_edit_password_old);
        addressLine1Edt = (EditText) view.findViewById(R.id.fragment_edit_address_addressline1);
        addressLine2Edt = (EditText) view.findViewById(R.id.fragment_edit_address_addressline2);
        mobileNoEdt = (EditText) view.findViewById(R.id.fragment_edit_address_mobile);
        zipCodeEdt = (EditText) view.findViewById(R.id.fragment_edit_address_zipcode);
        addressUpdateTv = (TextView) view.findViewById(R.id.fragment_edit_address_submit);


        // TODO: 09-Mar-17 Expandable view
        addressExpandable = (ExpandableLayout) view.findViewById(R.id.fragment_navigation_setting_main_expand_address);
        emailExpandable = (ExpandableLayout) view.findViewById(R.id.fragment_navigation_setting_main_expand_email);
        notificationExpandable = (ExpandableLayout) view.findViewById(R.id.fragment_navigation_setting_main_expand_notification);
        passwordExpandable = (ExpandableLayout) view.findViewById(R.id.fragment_navigation_setting_main_expand_password);
        // TODO: 09-Mar-17 view fliper 
        emailViewFliper = (ViewFlipper) view.findViewById(R.id.fragment_navigation_setting_main_email_vf);
        pwdViewFliper = (ViewFlipper) view.findViewById(R.id.fragment_navigation_setting_main_password_vf);
        addressViewFliper = (ViewFlipper) view.findViewById(R.id.fragment_navigation_setting_main_address_vf);

        addressTv.setSelected(false);

        pwdSubmitTv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        addressTv.setOnClickListener(this);
        emailTv.setOnClickListener(this);
        notificationTv.setOnClickListener(this);
        passwordTv.setOnClickListener(this);
        editEmailSubmitTv.setOnClickListener(this);
        addressUpdateTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_navigation_setting_main_back:

                getActivity().onBackPressed();

                break;
            case R.id.fragment_navigation_setting_main_address:

                addressTv.setSelected(true);
                emailTv.setSelected(false);
                notificationTv.setSelected(false);
                passwordTv.setSelected(false);

                if (!addressExpandable.isExpanded())
                    addressExpandable.expand();
                else
                    addressExpandable.collapse();
                emailExpandable.collapse();
                notificationExpandable.collapse();
                passwordExpandable.collapse();


                break;
            case R.id.fragment_navigation_setting_main_email:

                addressTv.setSelected(false);
                emailTv.setSelected(true);
                notificationTv.setSelected(false);
                passwordTv.setSelected(false);

                emailViewFliper.setDisplayedChild(0);

                if (!emailExpandable.isExpanded()) {
                    emailExpandable.expand();
                } else {
                    emailExpandable.collapse();
                }
                addressExpandable.collapse();
                notificationExpandable.collapse();
                passwordExpandable.collapse();

                break;
            case R.id.fragment_navigation_setting_main_notification:

                addressTv.setSelected(false);
                emailTv.setSelected(false);
                notificationTv.setSelected(true);
                passwordTv.setSelected(false);

                if (!notificationExpandable.isExpanded())
                    notificationExpandable.expand();
                else
                    notificationExpandable.collapse();

                addressExpandable.collapse();
                emailExpandable.collapse();
                passwordExpandable.collapse();

                break;
            case R.id.fragment_navigation_setting_main_password:

                addressTv.setSelected(false);
                emailTv.setSelected(false);
                notificationTv.setSelected(false);
                passwordTv.setSelected(true);

                pwdViewFliper.setDisplayedChild(0);

                if (!passwordExpandable.isExpanded()) {
                    passwordExpandable.expand();
                } else {
                    passwordExpandable.collapse();
                }

                addressExpandable.collapse();
                emailExpandable.collapse();
                notificationExpandable.collapse();

                break;
            case R.id.fragment_edit_email_submit:

                String email = editEmailEdt.getText().toString();
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("user_id", ReadSharePrefrence(getActivity(), USERID));
                    hashMap.put("email", editEmailEdt.getText().toString());


                    pd = new Dialog(getActivity());
                    pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    pd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    pd.setContentView(R.layout.dialog_loading);
                    pd.setCancelable(false);
                    pd.show();

                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);

                    Call<EditEmailDetails> call = apiService.updateEmail(hashMap);
                    call.enqueue(new Callback<EditEmailDetails>() {
                        @Override
                        public void onResponse(Call<EditEmailDetails> call, Response<EditEmailDetails> response) {
                            pd.dismiss();
                            if (response.body().getStatus().equalsIgnoreCase("true")) {

                                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                updateSucessMsgTv.setText(R.string.changeemail);
                                emailViewFliper.setDisplayedChild(1);
                                editEmailEdt.setText("");
                            } else {

                                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<EditEmailDetails> call, Throwable t) {
                            pd.dismiss();

                        }
                    });
                } else {

                }
                break;
            case R.id.fragment_edit_password_submit:
                String newPwd = pwdNewPwdEdt.getText().toString();
                String oldPwd = pwdOldPwdEdt.getText().toString();
                String reNewPwd = pwdRePwdEdt.getText().toString();

                if (newPwd.length() > 2 && newPwd.equals(reNewPwd)) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("user_id", ReadSharePrefrence(getActivity(), USERID));
                    hashMap.put("oldpass", oldPwd);
                    hashMap.put("newpass", newPwd);

                    pd = new Dialog(getActivity());
                    pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    pd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    pd.setContentView(R.layout.dialog_loading);
                    pd.setCancelable(false);
                    pd.show();

                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

                    Call<EditEmailDetails> call = apiService.updatePassword(hashMap);
                    call.enqueue(new Callback<EditEmailDetails>() {
                        @Override
                        public void onResponse(Call<EditEmailDetails> call, Response<EditEmailDetails> response) {
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                                pd.dismiss();
                                pwdViewFliper.setDisplayedChild(1);
                                updateSucessMsgTv.setText(R.string.changepassword);
                                pwdNewPwdEdt.setText("");
                                pwdRePwdEdt.setText("");
                                pwdOldPwdEdt.setText("");

                            } else {
                                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<EditEmailDetails> call, Throwable t) {
                            pd.dismiss();
                        }
                    });
                } else {

                    Toast.makeText(getActivity(), R.string.somethingwentwrong, Toast.LENGTH_SHORT).show();

                }

                break;
            case R.id.fragment_edit_address_submit:

                String addressLine1 = addressLine1Edt.getText().toString();
                String addressLine2 = addressLine2Edt.getText().toString();
                String zipCode = zipCodeEdt.getText().toString();
                final String mobileNo = mobileNoEdt.getText().toString();

                if (addressLine1.length() > 1 && addressLine2.length() > 1 && zipCode.length() > 1 && mobileNo.length() > 1) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("user_id", ReadSharePrefrence(getActivity(), USERID));
                    hashMap.put("address_line_1", addressLine1);
                    hashMap.put("address_line_2", addressLine2);
                    hashMap.put("phone", mobileNo);
                    hashMap.put("zip_code", zipCode);

                    pd = new Dialog(getActivity());
                    pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    pd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    pd.setContentView(R.layout.dialog_loading);
                    pd.setCancelable(false);
                    pd.show();

                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);

                    Call<EditEmailDetails> call = apiService.updateAddress(hashMap);
                    call.enqueue(new Callback<EditEmailDetails>() {
                        @Override
                        public void onResponse(Call<EditEmailDetails> call, Response<EditEmailDetails> response) {
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                                pd.dismiss();
                                addressViewFliper.setDisplayedChild(1);

                                addressLine1Edt.setText("");
                                addressLine2Edt.setText("");
                                mobileNoEdt.setText("");
                                mobileNoEdt.setText("");

                            } else {
                                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<EditEmailDetails> call, Throwable t) {
                            pd.dismiss();
                        }
                    });

                }

                break;
        }

    }
}
