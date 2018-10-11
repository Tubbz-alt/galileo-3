package com.josedlpozo.galileo.realm.realmbrowser.files.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import com.josedlpozo.galileo.R;
import com.josedlpozo.galileo.realm.realmbrowser.files.FilesContract;
import com.josedlpozo.galileo.realm.realmbrowser.files.FilesPresenter;
import com.josedlpozo.galileo.realm.realmbrowser.files.model.FilesPojo;
import java.util.ArrayList;

public class FilesActivity extends AppCompatActivity implements FilesContract.View {

    private FilesContract.Presenter presenter;
    private FilesAdapter adapter;

    public static Intent getIntent(@NonNull Context context) {
        Intent intent = new Intent(context, FilesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realm_browser_ac_recycler);
        setSupportActionBar((Toolbar) findViewById(R.id.realm_browser_toolbar));

        // Disable SwipeRefreshLayout - not used in this Activity
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setEnabled(false);

        // Adapter init
        adapter = new FilesAdapter(new ArrayList<FilesPojo>(), new FilesAdapter.OnFileSelectedListener() {
            @Override
            public void onFileSelected(FilesPojo file) {
                FilesActivity.this.presenter.onFileSelected(file);
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.realm_browser_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Presenter
        attachPresenter((FilesContract.Presenter) getLastCustomNonConfigurationInstance());
        presenter.requestForContentUpdate(this);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override
    public void showToast(@Nullable String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void attachPresenter(@Nullable FilesContract.Presenter presenter) {
        this.presenter = presenter;
        if (this.presenter == null) {
            this.presenter = new FilesPresenter();
        }
        this.presenter.attachView(this);
    }

    @Override
    public void updateWithFiles(@NonNull ArrayList<FilesPojo> filesList) {
        adapter.swapList(filesList);
        if (filesList.size() == 0) {
            // TODO show empty list placeholder
        }
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}
