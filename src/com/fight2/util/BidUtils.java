package com.fight2.util;

import com.fight2.service.BidService;

public class BidUtils {

    public static Runnable createCloseSchedule(final int id, final BidService bidService) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bidService.closeBid(id);
            }

        };
        return runnable;
    }

}
