package mountzoft.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class MovieDetailsActivity extends AppCompatActivity {

    ImageView mImageViewBackdrop;
    ImageView mImageViewPoster;
    TextView mTextViewMovieTitle;
    TextView mTextViewReleasDate;
    TextView mTextViewAvgRating;
    TextView mTextViewSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mImageViewBackdrop = findViewById(R.id.img_backdrop);
        mImageViewPoster = findViewById(R.id.img_poster);
        mTextViewMovieTitle = findViewById(R.id.tv_movie_title);
        mTextViewReleasDate = findViewById(R.id.tv_release_date);
        mTextViewAvgRating = findViewById(R.id.tv_avg_rating);
        mTextViewSummary = findViewById(R.id.tv_summary);

        Intent intent = getIntent();

        mTextViewMovieTitle.setText(MoviesGridActivity.movieTitle.get(intent.getIntExtra("position",1)));
        mTextViewReleasDate.setText(MoviesGridActivity.releaseDate.get(intent.getIntExtra("position",1)));
        mTextViewAvgRating.setText(MoviesGridActivity.averageVote.get(intent.getIntExtra("position",1)));
        mTextViewSummary.setText(MoviesGridActivity.summary.get(intent.getIntExtra("position",1)));
        Glide.with(this)
                .load(getString(R.string.poster_image_url_start) + MoviesGridActivity.moviePosterImage
                        .get(intent.getIntExtra("position",1)))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_error_outline_white_36dp))
                .into(mImageViewPoster);
        Glide.with(this)
                .load(getString(R.string.backbrop_url_start) + MoviesGridActivity.backdrop.get(intent.getIntExtra("position",1)))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_error_outline_white_36dp))
                .into(mImageViewBackdrop);
    }
}
