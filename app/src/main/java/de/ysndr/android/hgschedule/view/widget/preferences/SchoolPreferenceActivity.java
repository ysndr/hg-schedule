package de.ysndr.android.hgschedule.view.widget.preferences;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.Toast;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxrelay.BehaviorRelay;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.ysndr.android.hgschedule.MyApp;
import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.presenters.SchoolPresenter;
import de.ysndr.android.hgschedule.state.models.School;
import de.ysndr.android.hgschedule.util.Presentable;
import de.ysndr.android.hgschedule.util.preferences.GsonPreferenceAdapter;
import de.ysndr.android.hgschedule.view.adapters.ClickListAdapter;
import de.ysndr.android.hgschedule.view.adapters.ImmutableSchoolLabelViewWrapper;
import de.ysndr.android.hgschedule.view.adapters.SchoolLabelViewWrapper;
import de.ysndr.android.hgschedule.view.adapters.ViewWrapper;
import fj.Unit;
import fj.data.Option;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SchoolPreferenceActivity extends AppCompatActivity {

    RxSharedPreferences preferences;
    Preference<School> schoolPref;
    BehaviorRelay<School> school$;

    @Inject
    SchoolPresenter mPresenter;

    @BindView(R.id.school_selection_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.school_selection_recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_school_preference);

        ViewStub stub = (ViewStub) findViewById(R.id.content);
        stub.setLayoutResource(R.layout.school_selection_dialog);
        View inflated = stub.inflate();


        MyApp.getScheduleComponent(this).inject(this);
        unbinder = ButterKnife.bind(this, inflated);

        ClickListAdapter adapter = new ClickListAdapter();
        adapter.registerTypeMapping(new SchoolLabelViewWrapper.TypeMapper());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        school$ = BehaviorRelay.create(schoolPref.get());
        setupPreferenceConnection();

        mPresenter.reloadIntentSink
            .bindIntent(() -> RxSwipeRefreshLayout.refreshes(refreshLayout)
                .doOnNext(__ -> adapter.clear())
                .map(_void_ -> Unit.unit())
                .startWith(Unit.unit()));

        mPresenter.data$()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(error -> Toast.makeText(
                this,
                "An error occured",
                Toast.LENGTH_SHORT).show())
            .onErrorResumeNext(error -> Observable.just(Presentable.of(false, Option.none())))
            .doOnNext(listPresentable -> refreshLayout.setRefreshing(listPresentable.loading()))
            .filter(presentable -> presentable.result().isSome())
            .map(presentable -> presentable.result().some())
            .map(this::wrap)
            .subscribe(adapter::setContent);
    }

    private List<ViewWrapper> wrap(List<School> list) {
        fj.data.List<School> immList = fj.data.List.iterableList(list);
        return immList
            .<ViewWrapper>map(school -> ImmutableSchoolLabelViewWrapper
                .builder()
                .school(school)
                .build())
            .toJavaList();
    }


    private void setupPreferenceConnection() {
        preferences = RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(this));
        schoolPref = preferences.getObject(
            getIntent().getStringExtra("key"),
            School.empty(),
            new GsonPreferenceAdapter<>(School.class));
        school$.subscribe(schoolPref::set, Timber::e);
    }


}
