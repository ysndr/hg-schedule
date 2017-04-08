package io.ysndr.android.hg_schedule.features.schedule.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.prefs.MaterialDialogPreference;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fj.Unit;
import fj.data.Option;
import io.ysndr.android.hg_schedule.MyApp;
import io.ysndr.android.hg_schedule.R;
import io.ysndr.android.hg_schedule.features.schedule.models.GsonAdaptersModels;
import io.ysndr.android.hg_schedule.features.schedule.models.School;
import io.ysndr.android.hg_schedule.features.schedule.presenters.SchoolPresenter;
import io.ysndr.android.hg_schedule.features.schedule.util.preferences.GsonPreferenceAdapter;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.ReloadIntentSource;
import io.ysndr.android.hg_schedule.features.schedule.view.adapters.ClickListAdapter;
import io.ysndr.android.hg_schedule.features.schedule.view.adapters.SchoolLabelViewWrapper;
import io.ysndr.android.hg_schedule.features.schedule.view.adapters.ViewWrapper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/**
 * Created by yannik on 1/22/17.
 */

public class SchoolSelectionPreference extends MaterialDialogPreference {

    private final ClickListAdapter adapter;
    @LayoutRes
    private final int mDialogLayoutResId = R.layout.school_selection_dialog;
    RxSharedPreferences preferences;
    Preference<School> schoolPref;
    Observable<School> school$;
    View view;
    @Inject
    SchoolPresenter mPresenter;
    @BindView(R.id.school_selection_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.school_selection_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.school_selection_cancel_button)
    Button close;
    private Observable<Option<School>> close$;
    private Observable<Option<School>> value$;

    public SchoolSelectionPreference(Context context) {
        this(context, null);
    }

    public SchoolSelectionPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.dialogPreferenceStyle);
    }

    public SchoolSelectionPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    public SchoolSelectionPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        adapter = new ClickListAdapter();
        adapter.registerTypeMapping(new SchoolLabelViewWrapper.TypeMapper());

        preferences = RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(getContext()));
        schoolPref = preferences.getObject(getKey(), School.empty(), new GsonPreferenceAdapter<>(School.class));
        school$ = schoolPref.asObservable();

        school$.map(school -> school.summary())
                .subscribe((summary) -> {
                    setSummary(summary.orSome("Choose"));
                });

    }


    @Override
    protected View onCreateDialogView() {
        view = LayoutInflater.from(this.getContext()).inflate(mDialogLayoutResId, null);
        return view;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        ButterKnife.bind(this, this.view);
        MyApp.getScheduleComponent(this.getContext()).inject(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        ReloadIntentSource reloadIntentSource = new ReloadIntentSource() {
            @Override
            public Observable<Unit> reloadIntent$() {
                return RxSwipeRefreshLayout.refreshes(refreshLayout).map(_void_ -> Unit.unit()).startWith(Unit.unit());
            }
        };

        mPresenter.reloadIntentSink.bindIntent(reloadIntentSource);

        mPresenter.data$()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                data -> {
                    refreshLayout.setRefreshing(data.loading());
                    data.result().toEither(Unit.unit()).either(
                            loading -> {
                                Timber.d("Clear Adapter");
                                adapter.clear();
                                return Unit.unit();
                            },
                            content -> {
                                Timber.d("Updating Adapter");
                                adapter.setContent(wrap(content));
                                adapter.notifyDataSetChanged();
                                return Unit.unit();
                            });
                });

        close$ = RxView.clicks(close).map(_void_ -> Option.none());
        value$ = adapter.getClick().map(Option::some).observeOn(AndroidSchedulers.mainThread());

        close$.mergeWith(value$.filter(option -> option.isSome()))
                .take(1)
                .subscribe(option -> {
                            if (option.isSome()) {
                                Gson gson = new GsonBuilder()
                                        .registerTypeAdapterFactory(new GsonAdaptersModels())
                                        .create();
                                schoolPref.set(option.some());
                                mPresenter.reloadIntentSink.unbind();
                            }
                            getDialog().dismiss();
                        },
                        err -> Toast.makeText(getContext(), err.getMessage(), Toast.LENGTH_SHORT).show());

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

}

