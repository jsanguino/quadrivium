
package com.koritsi.quadrivium;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.koritsi.quadrivium.es.R;

public class PreferencesActivity extends PreferenceActivity {
	


	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
        // Load the preferences from an XML
        addPreferencesFromResource(R.layout.preferences);
    }
}
