package de.ysndr.android.hgschedule.view.widget.preferences;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding.view.RxView;
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

/**
 * Created by yannik on 6/25/17.
 */

public class SchoolPreference extends DialogPreference {

    static int mDialogLayoutResId = R.layout.school_selection_dialog;
    RxSharedPreferences preferences;
    Preference<School> schoolPref;
    BehaviorRelay<School> school$;
    @Inject
    SchoolPresenter mPresenter;

    public SchoolPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        MyApp.getScheduleComponent(this.getContext()).inject(this);

        preferences = RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(getContext()));
        schoolPref = preferences.getObject(getKey(), School.empty(), new GsonPreferenceAdapter<>(School.class));
        school$ = BehaviorRelay.create(schoolPref.get());
        school$
            .doOnNext(schoolPref::set)
            .doOnNext(school -> setSummary(school.summary().orSome("Choose")))
            .doOnError(Timber::e)
            .subscribe();

        setPositiveButtonText(null);
        setNegativeButtonText(null);
    }

    public SchoolPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    public SchoolPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.dialogPreferenceStyle);
    }

    public SchoolPreference(Context context) {
        this(context, null);
    }

//    @Override
//    public int getDialogLayoutResource() {
//        return mDialogLayoutResId;
//    }


    public BehaviorRelay<School> school$() {
        return school$;
    }

    public School getSchool() {
        return school$.toBlocking().first();
    }

    public void setSchool(School school) {
        schoolPref.set(school);
    }

    public static class DialogFragment extends PreferenceDialogFragmentCompat {

        @BindView(R.id.school_selection_refresh_layout)
        SwipeRefreshLayout refreshLayout;
        @BindView(R.id.school_selection_recycler_view)
        RecyclerView recyclerView;
        @BindView(R.id.school_selection_cancel_button)
        Button close;

        Unbinder unbinder;

        public static DialogFragment newInstance(String key) {
            final DialogFragment
                fragment = new DialogFragment();
            final Bundle b = new Bundle(1);
            b.putString(ARG_KEY, key);
            fragment.setArguments(b);

            return fragment;
        }


        @Override
        protected View onCreateDialogView(Context context) {
            View view = LayoutInflater.from(this.getContext()).inflate(mDialogLayoutResId, null);
            return view;
        }

        @Override
        protected void onBindDialogView(View view) {
            super.onBindDialogView(view);

            SchoolPreference preference = null;
            DialogPreference dialogPreference = getPreference();
            if (dialogPreference instanceof SchoolPreference) {
                preference = (SchoolPreference) dialogPreference;
            }
            if (preference == null) return;

            BehaviorRelay<School> school$ = preference.school$();
            this.unbinder = ButterKnife.bind(this, view);

            ClickListAdapter adapter = new ClickListAdapter();
            adapter.registerTypeMapping(new SchoolLabelViewWrapper.TypeMapper());

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);

            preference.mPresenter.reloadIntentSink
                .bindIntent(() -> RxSwipeRefreshLayout.refreshes(refreshLayout)
                    .doOnNext(__ -> adapter.clear())
                    .map(_void_ -> Unit.unit())
                    .startWith(Unit.unit()));

            preference.mPresenter.data$()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> Toast.makeText(
                    getContext(),
                    "An error occured",
                    Toast.LENGTH_SHORT).show())
                .onErrorResumeNext(error -> Observable.just(Presentable.of(false, Option.none())))
                .doOnNext(listPresentable -> refreshLayout.setRefreshing(listPresentable.loading()))
                .filter(presentable -> presentable.result().isSome())
                .map(presentable -> presentable.result().some())
                .map(this::wrap)
                .subscribe(adapter::setContent);

            adapter.getClick().doOnNext(__ -> this.dismiss()).subscribe(school$);

            RxView.clicks(close).doOnNext(__ -> this.dismiss()).subscribe();
        }


        @Override
        public void onDialogClosed(boolean positiveResult) {
//
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


}
