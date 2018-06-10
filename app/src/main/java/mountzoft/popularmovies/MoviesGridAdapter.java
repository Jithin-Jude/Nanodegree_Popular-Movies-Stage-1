package mountzoft.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class MoviesGridAdapter extends BaseAdapter {

    private Context mContext;
    private final ArrayList<String> movieTitle;
    private final ArrayList<String> moviePosterImage;

    public MoviesGridAdapter(Context context, ArrayList<String> movieTitle, ArrayList<String> moviePosterImage) {
        this.mContext = context;
        this.movieTitle = movieTitle;
        this.moviePosterImage = moviePosterImage;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return movieTitle.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        if (convertView == null) {
            grid = new View(mContext);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.grid_cell, null);

        } else {
            grid = convertView;
        }
        TextView textView = grid.findViewById(R.id.grid_text);
        ImageView imageView = grid.findViewById(R.id.grid_image);
        textView.setText(movieTitle.get(position));
        Glide.with(mContext)
                .load(mContext.getString(R.string.poster_image_url_start) + moviePosterImage.get(position))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_error_outline_white_36dp))
                .into(imageView);

        return grid;
    }
}
