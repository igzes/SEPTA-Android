package org.septa.android.app.notifications.edit;

import android.support.v7.util.DiffUtil;

import org.septa.android.app.services.apiinterfaces.model.RouteNotificationSubscription;

import java.util.List;

public class NotificationDiffCallback extends DiffUtil.Callback {

    private List<RouteNotificationSubscription> oldRoutes, newRoutes;

    public NotificationDiffCallback(List<RouteNotificationSubscription> oldRoutes, List<RouteNotificationSubscription> newRoutes) {
        this.oldRoutes = oldRoutes;
        this.newRoutes = newRoutes;
    }

    @Override
    public int getOldListSize() {
        return oldRoutes.size();
    }

    @Override
    public int getNewListSize() {
        return newRoutes.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldRoutes.get(oldItemPosition).getRouteId().equalsIgnoreCase(newRoutes.get(newItemPosition).getRouteId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldRoutes.get(oldItemPosition).equals(newRoutes.get(newItemPosition));
    }

}