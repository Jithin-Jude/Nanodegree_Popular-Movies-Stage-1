package mountzoft.popularmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MoviesGridActivity extends AppCompatActivity {

    private static String POPULAR_MOVIE_DB;
    URL mUrl;

    public static ArrayList<String> movieTitle, releaseDate, averageVote, summary, backdrop, moviePosterImage;

    private String RESULTS = "results";
    private String TITLE = "title";
    private String RELEASE_DATE = "release_date";
    private String AVERAGE_VOTE = "vote_average";
    private String SUMMARY = "overview";
    private String POSTER = "poster_path";
    private String BACKDROP = "backdrop_path";
    private String POSITION = "position";

    GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_grid);

        movieTitle = new ArrayList<>();
        releaseDate = new ArrayList<>();
        averageVote = new ArrayList<>();
        summary = new ArrayList<>();
        backdrop = new ArrayList<>();
        moviePosterImage = new ArrayList<>();

        POPULAR_MOVIE_DB = getString(R.string.most_popular_url_start) + getResources().getString(R.string.THEMOVIEDB_API_KEY);
        buildUrl();


        MoviesGridAdapter adapter = new MoviesGridAdapter(MoviesGridActivity.this, movieTitle, moviePosterImage);
        mGridView = findViewById(R.id.movies_grid);
        mGridView.setAdapter(adapter);
    }

    private void buildUrl() {
        try {
            mUrl = new URL(POPULAR_MOVIE_DB);
        } catch (Exception e) {
            Context context = MoviesGridActivity.this;
            String errorMessage = "URL is not found...";
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
        }
        new MovieTask().execute(mUrl);
    }

    private class MovieTask extends AsyncTask<URL, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(URL... params) {
            URL url = params[0];
            String results = null;
            try {
                results = NetworkUtils.httpConnectionResponse(url);
                try {
                    JSONObject jsonObject = new JSONObject(results);
                    JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);
                    movieTitle = new ArrayList<>(jsonArray.length());
                    releaseDate = new ArrayList<>(jsonArray.length());
                    averageVote = new ArrayList<>(jsonArray.length());
                    summary = new ArrayList<>(jsonArray.length());
                    backdrop = new ArrayList<>(jsonArray.length());
                    moviePosterImage = new ArrayList<>(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        movieTitle.add(jObject.getString(TITLE));
                        releaseDate.add(jObject.getString(RELEASE_DATE));
                        averageVote.add(jObject.getString(AVERAGE_VOTE));
                        summary.add(jObject.getString(SUMMARY));
                        backdrop.add(jObject.getString(BACKDROP));
                        moviePosterImage.add(jObject.getString(POSTER));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            mGridView = findViewById(R.id.movies_grid);
            int orientation = getResources().getConfiguration().orientation;
            mGridView.setNumColumns(orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 3);
            mGridView.setAdapter(new MoviesGridAdapter(MoviesGridActivity.this, movieTitle, moviePosterImage));
            mGridView.setVisibility(View.VISIBLE);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Intent intent = new Intent(MoviesGridActivity.this, MovieDetailsActivity.class);
                    intent.putExtra(POSITION, position );
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MoviesGridActivity.this, mGridView, "simple_transition");
                    startActivity(intent, options.toBundle());
                }

            });
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MoviesGridActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("Please Wait...");
            progressDialog.setMessage("Fetching data...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.top_rated:
                POPULAR_MOVIE_DB = getString(R.string.top_rated_url_start) + getResources().getString(R.string.THEMOVIEDB_API_KEY);
                buildUrl();
                return true;

            case R.id.most_popular:
                POPULAR_MOVIE_DB = getString(R.string.most_popular_url_start) + getResources().getString(R.string.THEMOVIEDB_API_KEY);
                buildUrl();
                return true;
        }
        return false;
    }
}
