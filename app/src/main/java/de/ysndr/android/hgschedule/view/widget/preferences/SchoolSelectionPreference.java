package de.ysndr.android.hgschedule.view.widget.preferences;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.prefs.MaterialDialogPreference;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.ysndr.android.hgschedule.MyApp;
import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.presenters.SchoolPresenter;
import de.ysndr.android.hgschedule.state.models.GsonAdaptersModels;
import de.ysndr.android.hgschedule.state.models.School;
import de.ysndr.android.hgschedule.util.preferences.GsonPreferenceConverter;
import de.ysndr.android.hgschedule.util.reactive.ReloadIntentSource;
import de.ysndr.android.hgschedule.view.adapters.ClickListAdapter;
import de.ysndr.android.hgschedule.view.adapters.ImmutableSchoolLabelViewWrapper;
import de.ysndr.android.hgschedule.view.adapters.SchoolLabelViewWrapper;
import de.ysndr.android.hgschedule.view.adapters.ViewWrapper;
import fj.Unit;
import fj.data.Option;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


/**
 * Created by yannik on 1/22/17.
 */

public class SchoolSelectionPreference extends MaterialDialogPreference {

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
    private ClickListAdapter adapter;
    private Observable<Option<School>> close$;
    private Observable<Option<School>> value$;

    public SchoolSelectionPreference(Context context) {
        this(context, null);
    }

    public SchoolSelectionPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.dialogPreferenceStyle);
    }

    public SchoolSelectionPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setDialogLayoutResource(mDialogLayoutResId);
    }

    public SchoolSelectionPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        adapter = new ClickListAdapter();
        adapter.registerTypeMapping(new SchoolLabelViewWrapper.TypeMapper());

        preferences = RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(getContext()));
        schoolPref = preferences.getObject(getKey(), School.empty(), new GsonPreferenceConverter<>(School.class));
        school$ = schoolPref.asObservable();

        school$.map(school -> school.summary())
                .subscribe((summary) -> {
                    setSummary(summary.orSome("Choose"));
                });
    }

//    @Override
//    protected View onCreateDialogView() {
//        view = LayoutInflater.from(this.getContext()).inflate(mDialogLayoutResId, null);
//        return view;
//    }

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
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Toast.makeText(getContext(), "An error occured", Toast.LENGTH_SHORT).show())
//                .onErrorResumeNext(error ->
//                    Observable.just(Presentable.of(false, Option.none())))
                .subscribe(
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
                        }
                );

        close$ = RxView.clicks(close).map(_void_ -> Option.none());
        value$ = adapter.getClick().map(Option::some).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());

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
                        err -> Toast.makeText(getContext(), "an error occured: " + err.getMessage(), Toast.LENGTH_SHORT).show());

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

