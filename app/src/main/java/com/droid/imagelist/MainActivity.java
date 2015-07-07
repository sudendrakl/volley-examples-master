package com.droid.imagelist;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.view.Menu;
import android.view.MenuItem;

import com.droid.imagelist.fragment.ExamplesListFragment;
import com.droid.imagelist.fragment.ExamplesListFragment.AttachFragmentEvent;
import com.droid.imagelist.util.Bus;
import com.droid.imagelist.util.PreferenceManager;

import javax.inject.Inject;

public class MainActivity extends FragmentActivity implements OnBackStackChangedListener {

    @Inject
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((VolleyApp) getApplication()).inject(this);
        setCustomTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        fm.addOnBackStackChangedListener(this);

        if (savedInstanceState == null) {
            fm.beginTransaction()
                    .add(R.id.container, new ExamplesListFragment())
                    .commit();
        }
    }

    private void setCustomTheme() {
        switch (preferenceManager.getFontSize()) {
            case 0://Large
                getTheme().applyStyle(R.style.FontStyle_Large, true);
                break;
            case 1://Medium
                getTheme().applyStyle(R.style.FontStyle_Medium, true);
                break;
            case 2://Small
                getTheme().applyStyle(R.style.FontStyle_Small, true);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bus.unregister(this);
    }

    public void onEventMainThread(AttachFragmentEvent event) {
        FragmentManager fm = getSupportFragmentManager();
        String tag = event.getFragmentTag();

        if (fm.findFragmentByTag(tag) == null) {
            Fragment fragment = Fragment.instantiate(this, tag);
            fragment.setArguments(event.getFragmentExtras());
            fm.beginTransaction()
                    .replace(R.id.container, fragment, tag)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                return true;
            case R.id.menu_font_size:
                changeFontDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu1, menu);
        super.onCreateOptionsMenu(menu);
        return false;
    }

    private void changeFontDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.font_sizes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preferenceManager.setFontSize(which);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                intent.putExtras(getIntent().getExtras());
                finish();
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackStackChanged() {
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        ActionBar actionBar = getActionBar();
        if (count > 0) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }
}
