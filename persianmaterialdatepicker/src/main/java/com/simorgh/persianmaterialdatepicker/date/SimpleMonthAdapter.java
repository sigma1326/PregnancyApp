
package com.simorgh.persianmaterialdatepicker.date;

import android.content.Context;

/**
 * An adapter for a list of {@link SimpleMonthView} items.
 */
public class SimpleMonthAdapter extends MonthAdapter {

    public SimpleMonthAdapter(Context context, DatePickerController controller) {
        super(context, controller);
    }

    @Override
    public MonthView createMonthView(Context context) {
        return new SimpleMonthView(context, null, mController);
    }
}
