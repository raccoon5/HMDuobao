package com.haomeng.duobao.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.haomeng.duobao.R;

public class MengzhuFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private MyAdapter mAdapter;

    public MengzhuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mengzhu, container, false);
        findView(view);
        setListener();
        init();
        return view;
    }

    private void findView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_mengzhu_recycler);
    }

    private void setListener() {

    }

    private void init() {
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position==0) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter = new MyAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    private class MyAdapter extends RecyclerView.Adapter {
        private final int TYPETOPS = 0;
        private final int TYPENORMALS = 1;
        private final int TYPELOAD = 2;

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPETOPS) {
                View view = mInflater.inflate(R.layout.item_mengzhu_top, null);
                TopViewHolder vh = new TopViewHolder(view);
                return vh;
            } else if (viewType == TYPENORMALS) {
                View view = mInflater.inflate(R.layout.item_mengzhu_normal, null);
                NormalHolder vh = new NormalHolder(view);
                return vh;
            } else {
                View view = mInflater.inflate(R.layout.footer, null);
                FooterHolder vh = new FooterHolder(view);
                return vh;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof TopViewHolder) {

            } else if (holder instanceof NormalHolder) {

            } else {
                //footer
            }
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        @Override
        public int getItemViewType(int position) {
            if (position==0) {
                return TYPETOPS;
            } else {
                return TYPENORMALS;
            }
        }

        private class TopViewHolder extends RecyclerView.ViewHolder {

            public TopViewHolder(View itemView) {
                super(itemView);
            }
        }

        private class NormalHolder extends RecyclerView.ViewHolder {

            public NormalHolder(View itemView) {
                super(itemView);
            }
        }

        private class FooterHolder extends RecyclerView.ViewHolder {

            public FooterHolder(View itemView) {
                super(itemView);
            }
        }
    }

}
