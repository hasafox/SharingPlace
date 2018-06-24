package com.hax.sharingplace;

import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hax on 2018/5/14.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> mnewsList;

    private String address = "http://ip/FindYou/AddResult";

    private String aname, bname;

    static class ViewHolder extends RecyclerView.ViewHolder{

        View newsView;

        TextView newsText;
        TextView agrText;
        TextView disText;
        TextView agrdText;
        TextView disgText;

        public ViewHolder(View view) {
            super(view);
            newsView = view;

            newsText = view.findViewById(R.id.news_text);
            agrText = view.findViewById(R.id.news_agree);
            disText = view.findViewById(R.id.news_disagree);
            agrdText = view.findViewById(R.id.news_agreed);
            disgText = view.findViewById(R.id.news_disagreed);
        }
    }

    public NewsAdapter(List<News> newsList) {
        mnewsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, null);

        final ViewHolder holder = new ViewHolder(view);

        aname = PreferenceManager.getDefaultSharedPreferences(parent.getContext()).getString("user", "");
        holder.agrText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                News news = mnewsList.get(position);
                bname = news.getAsk();
                Log.d(aname, bname);
                holder.agrdText.setVisibility(View.VISIBLE);
                holder.agrText.setVisibility(View.GONE);
                holder.disText.setVisibility(View.GONE);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("result", "1");
                params.put("aname", aname);
                params.put("bname", bname);
                try {
                    String completeURL = HttpUtil.getURLWithParams(address, params);
                    HttpUtil.sendHttpRequest(completeURL, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            Log.d("response", response);
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

        holder.disText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                News news = mnewsList.get(position);
                bname = news.getAsk();
                holder.disgText.setVisibility(View.VISIBLE);
                holder.agrText.setVisibility(View.GONE);
                holder.disText.setVisibility(View.GONE);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("result", "0");
                params.put("aname", aname);
                params.put("bname", bname);
                try {
                    String completeURL = HttpUtil.getURLWithParams(address, params);
                    HttpUtil.sendHttpRequest(completeURL, new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            Log.d("response", response);
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

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = mnewsList.get(position);
        holder.newsText.setText(news.getAsk());
    }

    @Override
    public int getItemCount() {
        return mnewsList.size();
    }
}

