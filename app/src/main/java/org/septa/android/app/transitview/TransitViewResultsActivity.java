package org.septa.android.app.transitview;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import org.septa.android.app.R;
import org.septa.android.app.domain.RouteDirectionModel;
import org.septa.android.app.view.TextView;

public class TransitViewResultsActivity extends AppCompatActivity implements Runnable {

    private static final String TAG = TransitViewResultsActivity.class.getSimpleName();

    private RouteDirectionModel firstRoute, secondRoute, thirdRoute;
    private String routeIds;

    private Handler refreshHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeActivity(savedInstanceState);

        setTitle(R.string.transit_view);
        setContentView(R.layout.activity_transitview_results);

        // initialize route labels
        updateRouteLabels(firstRoute, secondRoute, thirdRoute);

        // set up automatic refresh
        if (firstRoute != null) {
            refreshHandler = new Handler();
            refreshHandler.postDelayed(this, 30 * 1000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO: inflate options menu

        return true;
    }

    @Override
    public void run() {
        refreshData();
    }

    private void initializeActivity(@Nullable Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        } else {
            restoreState(bundle);
        }
    }

    private void restoreState(Bundle bundle) {
        firstRoute = (RouteDirectionModel) bundle.get(TransitViewFragment.TRANSITVIEW_ROUTE_FIRST);
        secondRoute = (RouteDirectionModel) bundle.get(TransitViewFragment.TRANSITVIEW_ROUTE_SECOND);
        thirdRoute = (RouteDirectionModel) bundle.get(TransitViewFragment.TRANSITVIEW_ROUTE_THIRD);
    }

    private void updateRouteLabels(@NonNull RouteDirectionModel first, RouteDirectionModel second, RouteDirectionModel third) {
        this.firstRoute = first;
        this.secondRoute = second;
        this.thirdRoute = third;

        TextView firstRouteLabel = (TextView) findViewById(R.id.first_route_delete);
        TextView secondRouteLabel = (TextView) findViewById(R.id.second_route_delete);
        TextView thirdRouteLabel = (TextView) findViewById(R.id.third_route_delete);
        TextView addLabel = (TextView) findViewById(R.id.header_add_label);

        StringBuilder routeIdBuilder = new StringBuilder(firstRoute.getRouteId());

        firstRouteLabel.setText(firstRoute.getRouteId());
        firstRouteLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: prompt to delete

                if (secondRoute != null || thirdRoute != null) {
                    // delete first route
                    updateRouteLabels(secondRoute, thirdRoute, null);
                } else {
                    // take user back to picker screen
                    finish();
                }
            }
        });

        if (secondRoute != null) {
            routeIdBuilder.append(",").append(secondRoute.getRouteId());
            secondRouteLabel.setText(secondRoute.getRouteId());
            secondRouteLabel.setVisibility(View.VISIBLE);
            secondRouteLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: prompt to delete

                    // delete second route
                    updateRouteLabels(firstRoute, thirdRoute, null);
                }
            });
        } else {
            secondRouteLabel.setText(null);
            secondRouteLabel.setVisibility(View.GONE);
        }

        if (thirdRoute != null) {
            // update third route label
            routeIdBuilder.append(",").append(thirdRoute.getRouteId());
            thirdRouteLabel.setText(thirdRoute.getRouteId());
            thirdRouteLabel.setVisibility(View.VISIBLE);
            thirdRouteLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: prompt to delete

                    // delete third route
                    updateRouteLabels(firstRoute, secondRoute, null);
                }
            });

            // disable add button
            disableView(addLabel);

        } else {
            thirdRouteLabel.setText(null);
            thirdRouteLabel.setVisibility(View.GONE);

            // make add button clickable
            activateView(addLabel);
        }
        routeIds = routeIdBuilder.toString();
    }

    private void disableView(View view) {
        view.setAlpha((float) .3);
        view.setClickable(false);
    }

    private void activateView(View view) {
        view.setAlpha(1);
        view.setClickable(true);
    }

    private void refreshData() {
        // TODO: refresh the data
        Log.e(TAG, "Refreshing TransitView data for " + routeIds); // TODO: remove
    }

}
