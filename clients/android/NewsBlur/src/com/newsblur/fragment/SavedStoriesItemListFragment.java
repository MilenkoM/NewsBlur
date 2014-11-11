package com.newsblur.fragment;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.newsblur.R;
import com.newsblur.activity.Reading;
import com.newsblur.activity.SavedStoriesReading;
import com.newsblur.activity.FeedReading;
import com.newsblur.database.DatabaseConstants;
import com.newsblur.database.MultipleFeedItemsAdapter;
import com.newsblur.util.DefaultFeedView;
import com.newsblur.util.StoryOrder;
import com.newsblur.view.SocialItemViewBinder;

public class SavedStoriesItemListFragment extends ItemListFragment implements OnItemClickListener {

    private ListView itemList;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_itemlist, null);
		itemList = (ListView) v.findViewById(R.id.itemlistfragment_list);
        setupBezelSwipeDetector(itemList);
		itemList.setEmptyView(v.findViewById(R.id.empty_view));
		itemList.setOnScrollListener(this);
		itemList.setOnItemClickListener(this);
        if (adapter != null) {
            // normally the list gets set up when the adapter is created, but sometimes
            // onCreateView gets re-called.
            itemList.setAdapter(adapter);
        }

		getLoaderManager().initLoader(ITEMLIST_LOADER , null, this);

		return v;
	}

	public static ItemListFragment newInstance(DefaultFeedView defaultFeedView) {
		ItemListFragment fragment = new SavedStoriesItemListFragment();
        Bundle args = new Bundle();
        args.putSerializable("defaultFeedView", defaultFeedView);
        fragment.setArguments(args);
		return fragment;
	}

    @Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if ((adapter == null) && (cursor != null)) {
            String[] groupFrom = new String[] { DatabaseConstants.STORY_TITLE, DatabaseConstants.STORY_SHORT_CONTENT, DatabaseConstants.STORY_AUTHORS, DatabaseConstants.STORY_TIMESTAMP, DatabaseConstants.STORY_INTELLIGENCE_AUTHORS, DatabaseConstants.FEED_TITLE };
            int[] groupTo = new int[] { R.id.row_item_title, R.id.row_item_content, R.id.row_item_author, R.id.row_item_date, R.id.row_item_sidebar, R.id.row_item_feedtitle };
            adapter = new MultipleFeedItemsAdapter(getActivity(), R.layout.row_socialitem, cursor, groupFrom, groupTo, true);
            adapter.setViewBinder(new SocialItemViewBinder(getActivity(), true));
            itemList.setAdapter(adapter);
       }
       super.onLoadFinished(loader, cursor);
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (getActivity().isFinishing()) return;
		Intent i = new Intent(getActivity(), SavedStoriesReading.class);
        i.putExtra(Reading.EXTRA_FEEDSET, getFeedSet());
		i.putExtra(FeedReading.EXTRA_POSITION, position);
        i.putExtra(Reading.EXTRA_DEFAULT_FEED_VIEW, defaultFeedView);
		startActivity(i);
	}


}
