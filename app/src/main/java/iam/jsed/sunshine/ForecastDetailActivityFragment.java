package iam.jsed.sunshine;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.URISyntaxException;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastDetailActivityFragment extends Fragment {

    private final String LOG_TAG = ForecastDetailActivityFragment.class.getSimpleName();
    private TextView textView;
    private View rootView;

    public ForecastDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_forecast_detail, container, false);

        Intent intent = getActivity().getIntent();
        String message = intent.getStringExtra(Intent.EXTRA_TEXT);

        textView = (TextView) rootView.findViewById(R.id.forcast_detail_textview1);
        textView.setText(message);

        return rootView;


    }
}
