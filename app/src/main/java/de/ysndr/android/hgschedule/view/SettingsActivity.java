package de.ysndr.android.hgschedule.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.state.models.School;
import de.ysndr.android.hgschedule.util.preferences.GsonPreferenceConverter;
import de.ysndr.android.hgschedule.view.widget.preferences.SchoolPreference;
import timber.log.Timber;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.content, new SettingsFragment()).commit();

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(@Nullable Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.settings);

//            setupPreferences();
        }

        @Override
        public void onDisplayPreferenceDialog(Preference preference) {
            // Try if the preference is one of our custom Preferences
            DialogFragment dialogFragment = null;
            if (preference instanceof SchoolPreference) {
                // Create a new instance of TimePreferenceDialogFragment with the key of the related
                // Preference
                dialogFragment = SchoolPreference.DialogFragment
                    .newInstance(preference.getKey());
            }

            // If it was one of our cutom Preferences, show its dialog
            if (dialogFragment != null) {
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(this.getFragmentManager(),
                    "android.support.v7.preference" +
                        ".PreferenceFragment.DIALOG");
            }
            // Could not be handled here. Try with the super method.
            else {
                super.onDisplayPreferenceDialog(preference);
            }
        }


        private void setupPreferences() {

            Timber.d("pref_dialog exists");

            Preference p = findPreference("school2");
            RxSharedPreferences.create(getPreferenceManager().getSharedPreferences())
                .getObject(p.getKey(), School.empty(), new GsonPreferenceConverter<>(School.class))
                .asObservable()
                .doOnNext(school -> p.setSummary(school.summary().orSome("Choose")))
                .subscribe();

//            pref_dialog.setOnPreferenceClickListener(
//                DialogPreferenceDelegate.of(
//                    pref_dialog,
//                    getFragmentManager(),
//                    new SchoolSelectionDialog()).listener());


        }


    }

}
