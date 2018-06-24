package com.hax.sharingplace;

import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hax on 2018/5/11.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<Friend> mfriendList;

    private String address = "http://ip/FindYou/FriendStatus";

    private String address1 = "http://ip/FindYou/PostStatus";

    private String aname, bname;

    private  OnItemClickListener mOnItemClickListener;

    /*Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = new Bundle();
            bundle = msg.getData();
            String temp = bundle.getString("result");

            if ("1".equals(temp))
                Toast.makeText(MyfrensActivity.this, "wait friend`s allow", Toast.LENGTH_SHORT).show();
        }
    };*/

    static class ViewHolder extends RecyclerView.ViewHolder {
        View friendView;

        ToggleButton friendBtn;
        TextView friendText;

        public ViewHolder(View view) {
            super(view);
            friendView = view;

            friendBtn = view.findViewById(R.id.friend_btn);
            friendText = view.findViewById(R.id.friend_text);
        }
    }

    public FriendAdapter(List<Friend> friendList) {
        mfriendList = friendList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_item, null);
        final ViewHolder holder = new ViewHolder(view);

        aname = PreferenceManager.getDefaultSharedPreferences(parent.getContext()).getString("user", "");
        holder.friendBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int position = holder.getAdapterPosition();
                Friend friend = mfriendList.get(position);
                bname = friend.getName();

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("result", String.valueOf(b));
                params.put("aname", aname);
                params.put("bname", bname);
                try {
                    String completeURL = HttpUtil.getURLWithParams(address, params);
                    HttpUtil.sendHttpRequest(completeURL, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                        }
                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });
                }catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Friend friend = mfriendList.get(position);
                bname = friend.getName();
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("aname", aname);
                params.put("bname", bname);
                try {
                    String completeURL = HttpUtil.getURLWithParams(address1, params);
                    HttpUtil.sendHttpRequest(completeURL, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {

                        }
                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });
                }catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });*/
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Friend friend = mfriendList.get(position);
        holder.friendText.setText(friend.getName());
        if ("1".equals(friend.getStatus()))
            holder.friendBtn.setChecked(true);
        else
            holder.friendBtn.setChecked(false);

        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mfriendList.size();
    }

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
}
