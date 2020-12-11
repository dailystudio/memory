package com.dailystudio.memory.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.dailystudio.app.fragment.BaseIntentFragment;

import java.util.List;

/**
 * Created by nan on 2015/2/12.
 */
public abstract class AbsLoaderActionBarFragmentActivity<T> extends ActionBarFragmentActivity
    implements LoaderManager.LoaderCallbacks<T> {

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        getSupportLoaderManager().initLoader(getLoaderId(), createLoaderArguments(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        restartLoader();
    }

    public void restartLoader() {
        getSupportLoaderManager().restartLoader(
                getLoaderId(),
                createLoaderArguments(),
                this);
    }

    abstract protected int getLoaderId();
    abstract protected Bundle createLoaderArguments();

}
