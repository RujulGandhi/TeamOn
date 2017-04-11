package com.app.archirayan.teamon.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

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
import static com.app.archirayan.teamon.Utils.Constant.USERID;
import static com.app.archirayan.teamon.Utils.Utils.ReadSharePrefrence;

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
    public String userId;
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
        userId = ReadSharePrefrence(Test.this, USERID);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> mMap = new HashMap<>();
        mMap.put("user_id", userId);
        Call<ChatDetails> callApi = apiInterface.getChatDetails(mMap);
        callApi.enqueue(new Callback<ChatDetails>() {
            @Override
            public void onResponse(Call<ChatDetails> call, Response<ChatDetails> response) {
                if (response.body().getStatus().equalsIgnoreCase("true")) {

                    // TODO: 07-Apr-17 set adapter for chat message
                    listMsgDetails = response.body().getData();
                    adapter = new ChatAdapter(listMsgDetails, userId);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
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

            mMap.put("sender", userId);
            mMap.put("subject", "");
            mMap.put("name", "");
            mMap.put("text", msgEdt.getText().toString());
            //            http://easydatasearch.com/easydata1/teamon/api/insert_chat_msg.php?sender=2&recipient=4&subject=wallet&name=rayan&text=give%20me%20new%20stock%20update
            Call<SendMessageDetails> callApi = apiInterface.addChatData(mMap);
            callApi.enqueue(new Callback<SendMessageDetails>() {
                @Override
                public void onResponse(Call<SendMessageDetails> call, Response<SendMessageDetails> response) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        MessageDetails details = new MessageDetails();
                        details.setSender(userId);
                        details.setRecipient("1");
                        details.setName("");
                        details.setSubject("");
                        details.setText(msgEdt.getText().toString());

                        listMsgDetails.add(details);
                        msgEdt.setText("");
                        adapter.notifyDataSetChanged();

                        Log.d("Test", userId);

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
