package com.app.archirayan.teamon.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.archirayan.teamon.Model.CategoryDetails;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archirayan on 27-Dec-16.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private final List<CategoryDetails> arrayList;
    private final OnItemClickListener listener;
    public String Id;
    public Utils utils;
    private Context cntx;

    public CategoryAdapter(ArrayList<CategoryDetails> arrayList, Context context, OnItemClickListener listener) {
        this.listener = listener;
        Log.d("Length", "" + arrayList.size());
        this.arrayList = arrayList;
        this.cntx = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_category, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.bind(arrayList.get(position), listener);
        final CategoryDetails details = arrayList.get(position);

        Id = details.getCatId();
        Log.d("ID", "" + Id);
        holder.title.setText(details.getCatTitle());
        holder.count.setText(details.getCatCount() + " Teams");

        // click*/
    }

    @Override
    public long getItemId(int position) {

        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public interface OnItemClickListener {
        void onItemClick(CategoryDetails item);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.adapter_category_title);
            count = (TextView) view.findViewById(R.id.adapter_category_count);
        }

        public void bind(final CategoryDetails item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

   /* private class getProductList extends AsyncTask<String, String, String> {
        String cId;
        int pos;

        public getProductList(String catId, int position) {
            this.cId = catId;
            this.pos = position;
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("uid", Utils.ReadSharePrefrence(context, Constant.USERID));
            hashMap.put("category_id", cId);
            return utils.getResponseofPost(Constant.BASE_URL + "get_product_list27.php", hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            // CategoryDetails details = new CategoryDetails();
            //  ProductDetail details = new ProductDetail();
            super.onPostExecute(s);

            try {

                JSONObject catobject = new JSONObject(s);
                //  JSONArray catArray = catobject.getJSONArray(s);
                if (catobject.getString("status").equalsIgnoreCase("true")) {

                    Toast.makeText(context, catobject.getString("msg"), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, catobject.getString("msg"), Toast.LENGTH_SHORT).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
}
