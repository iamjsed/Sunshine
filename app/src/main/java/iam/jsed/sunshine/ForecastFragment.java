package iam.jsed.sunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    private final String LOG_TAG = ForecastFragment.class.getSimpleName();

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forcast_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ( item.getItemId() == R.id.action_refresh ) {
            ForecastWeatherTask forecastWeatherTask = new ForecastWeatherTask();
            forecastWeatherTask.execute("Quezon City");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] forcastArray = {
            "Today - Sunny - 88/63",
            "Tomorrow - Cloudy - 73/56",
            "Wed - Rainy - 60/20"
        };

        List<String> weekForcast = new ArrayList<String>(Arrays.asList(forcastArray));

        ArrayAdapter<String> arrayAdapterForcast = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forcast_textview,
                weekForcast);

        ListView lvWeekForcast = (ListView) rootView.findViewById(R.id.list_view_forecast);

        lvWeekForcast.setAdapter(arrayAdapterForcast);

        return rootView;
    }

    public class ForecastWeatherTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = ForecastWeatherTask.class.getSimpleName();

        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;


        private String format = "json";
        private String unit = "metric";
        private int numofdays = 7;

        @Override
        protected Void doInBackground(String... strings) {

            if ( strings.length == 0 ) {
                return null;
            }

            String forecastJsonStr = null;

            try {

                final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String MODE_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String COUNT_PARAM = "cnt";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, strings[0])
                        .appendQueryParameter(MODE_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, unit)
                        .appendQueryParameter(COUNT_PARAM, Integer.toString(numofdays))
                        .build();

                Log.d(LOG_TAG, builtUri.toString());
                //http://api.openweathermap.org/data/2.5/forecast/daily?q=Quezon+City&mode=json&unit=metric&cnt=7
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                forecastJsonStr = buffer.toString();

                Log.d(LOG_TAG, forecastJsonStr);

            } catch ( IOException ioe ) {
                Log.e("PlaceholderFragment", "Error ", ioe);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }

}
