package com.app.archirayan.teamon.Fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.archirayan.teamon.Adapter.ProductOrderAdapter;
import com.app.archirayan.teamon.Interface.ClickListener;
import com.app.archirayan.teamon.Model.ProductOrdersDetails;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Constant;
import com.app.archirayan.teamon.Utils.RecyclerTouchListener;
import com.app.archirayan.teamon.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.app.archirayan.teamon.Utils.Constant.ORDERID;
import static com.app.archirayan.teamon.Utils.Constant.ORDERSTATUS;
import static com.app.archirayan.teamon.Utils.Utils.ReadSharePrefrence;
import static com.app.archirayan.teamon.Utils.Utils.WriteSharePrefrence;

/**
 * Created by Ravi archi on 12/29/2016.
 */

public class TabNotOver extends Fragment {

    public ArrayList<ProductOrdersDetails> arrayList;
    public ProgressBar pb;
    public TextView textView;
    public Utils utils;
    public ProductOrderAdapter adapter;
    public String pId;
    public RecyclerView recyclerView;
    public Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_notover_orders, container, false);

        utils = new Utils(getActivity());
        arrayList = new ArrayList<>();
        context = getActivity();
        pb = (ProgressBar) view.findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_tab_notover_orders_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ProductOrderAdapter(getActivity(), arrayList, getActivity().getSupportFragmentManager(), Constant.NOTCOMPLETED);
        recyclerView.setAdapter(adapter);
        textView = (TextView) view.findViewById(R.id.textView);
        init();
        return view;
    }


    private void init() {
        if (utils.isConnectingToInternet()) {
            new GetNotOverOrderList().execute();
        } else {
            Toast.makeText(getActivity(), R.string.somethingwentwrong, Toast.LENGTH_SHORT).show();
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                WriteSharePrefrence(getActivity(), ORDERID, arrayList.get(position).getOrderId());
                WriteSharePrefrence(getActivity(), ORDERSTATUS, "not-over");
                Fragment fragment = new OrderDetail();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack("orderDeliveryDetails");
                ft.replace(R.id.activity_main_frame_layout, fragment).commit();

            }
        }));

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private class GetNotOverOrderList extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList.clear();
            if (arrayList.size() <= 0) {
                recyclerView.setVisibility(View.GONE);
            }
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            return utils.getResponseofGet(Constant.BASE_URL + "get_orders.php?uid=" + ReadSharePrefrence(getActivity(), Constant.USERID) + "&status=not-over");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {

                    JSONArray mainArray = object.getJSONArray("data");
                    recyclerView.setVisibility(View.VISIBLE);

                    for (int j = 0; j < mainArray.length(); j++) {
                        JSONObject dataObject = mainArray.getJSONObject(j);

                        JSONArray secondArray = dataObject.getJSONArray("product");
                        for (int k = 0; k < secondArray.length(); k++) {
                            JSONObject productObject = secondArray.getJSONObject(k);
                            ProductOrdersDetails details = new ProductOrdersDetails();
                            details.setOrderId(dataObject.getString("order_id"));
                            details.setOrderStatus(dataObject.getString("order_tracking_status"));
                            details.setOrderProPrice(productObject.getString("product_price"));
                            details.setOrderProName(productObject.getString("post_title"));
                            details.setOrderProSalerName(productObject.getString("seller_name"));
                            if (productObject.has("product_image")) {
                                details.setOrderProImage(productObject.getString("product_image"));
                            } else {
                                details.setOrderProImage("");
                            }
                            arrayList.add(details);

                        }
                    }

                } else {
                    Toast.makeText(getActivity(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();


            pb.setVisibility(View.GONE);
        }
    }


}
