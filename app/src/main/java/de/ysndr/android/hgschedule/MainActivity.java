package de.ysndr.android.hgschedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.ysndr.android.hgschedule.view.SettingsActivity;
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
