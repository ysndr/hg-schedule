package io.ysndr.android.hg_schedule.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.ysndr.android.hg_schedule.MyApp;
import io.ysndr.android.hg_schedule.R;
import io.ysndr.android.hg_schedule.features.schedule.view.SettingsActivity;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {


//    @Inject
//    ReactiveCache cache;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    CompositeSubscription subscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        MyApp.getScheduleComponent(this).inject(this);

        subscriptions = new CompositeSubscription();

        setSupportActionBar(toolbar);

        subscriptions.addAll(
                RxView.clicks(fab).subscribe(_void_ -> {
                    Intent startSettings = new Intent(this, SettingsActivity.class);
                    startActivity(startSettings);
                })/*,

                RxView.longClicks(fab).flatMap(__ -> cache.evictAll()).subscribe()*/
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.clear();
    }
}
