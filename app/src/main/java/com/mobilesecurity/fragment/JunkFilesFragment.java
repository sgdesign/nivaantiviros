package com.mobilesecurity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobilesecurity.activities.ScanningResultActivity;
import com.mobilesecurity.adapter.JunkFilesAdapter;
import com.mobilesecurity.model.JunkOfApplication;
import com.mobilesecurity.util.TypeFaceUttils;
import com.mobilesecurity.util.Utils;
import com.studioninja.lockersgha.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class JunkFilesFragment extends Fragment {
    private ScanningResultActivity activity;
    private JunkFilesAdapter adapter;
    @BindView(R.id.framelayout_boost)
    View framelayout_boost;
    private long junkFilesSize;
    @BindView(R.id.rv_cache)
    RecyclerView rv_cache;
    @BindView(R.id.tv_boost)
    TextView tv_boost;
    @BindView(R.id.tv_junk_files_total)
    TextView tv_junk_files_total;
    @BindView(R.id.tv_mb)
    TextView tv_mb;
    @BindView(R.id.tv_suggested)
    TextView tv_suggested;
    @BindView(R.id.tv_title_total_found)
    TextView tv_title_total_found;
    @BindView(R.id.tv_total_found)
    TextView tv_total_found;
    @BindView(R.id.tv_total_mb)
    TextView tv_total_mb;

    private void customFont() {
        TypeFaceUttils.setNomal(getActivity(), this.tv_junk_files_total);
        TypeFaceUttils.setNomal(getActivity(), this.tv_mb);
        TypeFaceUttils.setNomal(getActivity(), this.tv_suggested);
        TypeFaceUttils.setNomal(getActivity(), this.tv_title_total_found);
        TypeFaceUttils.setNomal(getActivity(), this.tv_total_found);
        TypeFaceUttils.setNomal(getActivity(), this.tv_total_mb);
        TypeFaceUttils.setNomal(getActivity(), this.tv_boost);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_junk_files, container, false);
        ButterKnife.bind(this, view);
        customFont();
        return view;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = (ScanningResultActivity) getActivity();
        if (this.activity.getMonitorShieldService() == null) {
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            initView();
        }
    }

    private void initView() {
        for (JunkOfApplication junkOfApplication : Utils.junkOfApplications) {
            this.junkFilesSize += junkOfApplication.getCacheSize();
        }
        this.junkFilesSize /= 1048576;
        this.tv_junk_files_total.setText(String.valueOf(this.junkFilesSize));
        this.tv_total_found.setText(String.valueOf(this.junkFilesSize));
        this.rv_cache.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.rv_cache.setHasFixedSize(true);
        this.adapter = new JunkFilesAdapter(getActivity(), Utils.junkOfApplications);
        this.rv_cache.setAdapter(this.adapter);
        this.framelayout_boost.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Utils.clearCache(JunkFilesFragment.this.getActivity());
                JunkFilesFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                JunkFilesFragment.this.activity.refresh();
            }
        });
    }
}
