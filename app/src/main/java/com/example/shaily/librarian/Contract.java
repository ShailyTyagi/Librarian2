package com.example.shaily.librarian;

import android.provider.BaseColumns;

/**
 * Created by Shaily on 28-06-2017.
 */

public class Contract {

    private Contract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "BookDatabase";
        public static final String COLUMN_NAME_Bname = "BookName";
        public static final String COLUMN_NAME_AUTHOR="Author";
        public static final String COLUMN_NAME_Issue = "IssuedOn";
        public static final String COLUMN_NAME_DUE="DueDate";
        public static final String COLUMN_NAME_FINE_PER_DAY="Fine";
        public  static final String COLUMN_NAME_TFINE="TotalFine";
    }
}
