package de.ysndr.android.hgschedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.ysndr.android.hgschedule.view.SettingsActivity;
import de.ysndr.android.hgschedule.view.schedulelist.ScheduleListController;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
//    @Inject
//    ReactiveCache cache;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.container)
    ViewGroup container;

    CompositeDisposable disposables;
    Router router;
    Controller mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
//
        MyApp.getActivityComponent(this).inject(this);

        disposables = new CompositeDisposable();


        collapsingToolbarLayout.setTitleEnabled(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Substitutes");


        router = Conductor.attachRouter(this,
            container, savedInstanceState);
        if (!router.hasRootController()) {
            mainController = new ScheduleListController();
            router.setRoot(RouterTransaction.with(mainController));
        }

        disposables.addAll(
            RxView.clicks(fab).subscribe(_void_ -> {
                Intent startSettings = new Intent(this, SettingsActivity.class);
                startActivity(startSettings);
            })
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}

