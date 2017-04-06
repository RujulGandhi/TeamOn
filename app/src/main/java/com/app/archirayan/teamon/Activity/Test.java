package com.app.archirayan.teamon.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.archirayan.teamon.Adapter.ChatAdapter;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.retrofit.ApiClient;
import com.app.archirayan.teamon.retrofit.ApiInterface;
import com.app.archirayan.teamon.retrofit.Model.Chat.ChatDetails;
import com.app.archirayan.teamon.retrofit.Model.Chat.MessageDetails;
import com.app.archirayan.teamon.retrofit.Model.SendMessageDetails;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.archirayan.teamon.Utils.Constant.MESSAGE_MAX_LENGTH;
import static com.app.archirayan.teamon.Utils.Constant.MESSAGE_MIN_LENGTH;

/**
 * Created by archirayan on 09-Dec-16.
 */

public class Test extends AppCompatActivity {

    public static ChatAdapter adapter;
    public static List<MessageDetails> listMsgDetails;
    @BindView(R.id.activity_test_msg_recycler)
    public RecyclerView recyclerView;
    @BindView(R.id.activity_test_msg_edt)
    public EditText msgEdt;
    @BindView(R.id.activity_test_send)
    public ImageView sendIV;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO: 31-Mar-17 API Call for get chat
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> mMap = new HashMap<>();
        mMap.put("user_id", "5");
        Call<ChatDetails> callApi = apiInterface.getChatDetails(mMap);
        callApi.enqueue(new Callback<ChatDetails>() {
            @Override
            public void onResponse(Call<ChatDetails> call, Response<ChatDetails> response) {
                if (response.body().getStatus().equalsIgnoreCase("true")) {

                    listMsgDetails = response.body().getData();
                    adapter = new ChatAdapter(listMsgDetails);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<ChatDetails> call, Throwable t) {


            }
        });
    }

    @OnClick(R.id.activity_test_send)
    public void onClickOfSend() {

        if (msgEdt.length() > MESSAGE_MIN_LENGTH && msgEdt.length() < MESSAGE_MAX_LENGTH) {
            HashMap<String, String> mMap = new HashMap<>();
//            sender=3&subject=wallet&name=rayan&text=update
            mMap.put("sender", "5");
            mMap.put("subject", "Testing");
            mMap.put("name", "Archirayan3");
            mMap.put("text", msgEdt.getText().toString());
            //            http://easydatasearch.com/easydata1/teamon/api/insert_chat_msg.php?sender=2&recipient=4&subject=wallet&name=rayan&text=give%20me%20new%20stock%20update
            Call<SendMessageDetails> callApi = apiInterface.addChatData(mMap);
            callApi.enqueue(new Callback<SendMessageDetails>() {
                @Override
                public void onResponse(Call<SendMessageDetails> call, Response<SendMessageDetails> response) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        MessageDetails details = new MessageDetails();
                        details.setSender("5");
                        details.setRecipient("1");
                        details.setName("archi");
                        details.setSubject("Archirayan");
                        details.setText(msgEdt.getText().toString());

                        listMsgDetails.add(details);
                        msgEdt.setText("");
                        adapter.notifyDataSetChanged();

                        Toast.makeText(Test.this, "Message sent sucessfully.", Toast.LENGTH_SHORT).show();
//                        ChatAdapter adapter = new ChatAdapter(response.body().getData());
//                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
////                    recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
//                        recyclerView.setLayoutManager(mLayoutManager);
//                        recyclerView.setItemAnimator(new DefaultItemAnimator());
//                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<SendMessageDetails> call, Throwable t) {

                }
            });
        }

    }
}
