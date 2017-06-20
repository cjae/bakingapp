package com.dreammesh.app.bakingapp.data.local;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Jedidiah on 13/05/2017.
 */

@Database(version = BakingDatabase.VERSION)
final class BakingDatabase {

    static final int VERSION = 1;

    @Table(BakingColumns.class)
    static final String BAKING = "baking";
}
