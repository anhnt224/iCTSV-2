package com.bk.ctsv.data

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.bk.ctsv.Converters
import com.bk.ctsv.dao.*
import com.bk.ctsv.models.entity.*
import com.bk.ctsv.models.entity.run.RunningLocation
import com.bk.ctsv.models.entity.run.RunningWay
import com.bk.ctsv.utilities.InjectorUtils
import com.bk.ctsv.utilities.VERSION_DB

@Database(entities = [
    Activity::class,
    Criteria::class,
    SVGroup::class,
    User::class,
    UserCriteriaActivity::class,
    UserCriteriaPoint::class,
    UserGroup::class,
    UserRole::class,
    UserCheckInActivity::class,
    ScheduleStudent::class,
    WeekNumber::class,
    CountSteps::class,
    Subject::class,
    RunningLocation::class,
    RunningWay::class
                     ], version = VERSION_DB, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {


    abstract fun activityDAO(): ActivityDAO
    abstract fun criteriaDAO(): CriteriaDAO
    abstract fun svGroupDAO(): SVGroupDAO
    abstract fun userCriteriaActivityDAO(): UserCriteriaActivityDAO
    abstract fun userCriteriaPointDAO(): UserCriteriaPointDAO
    abstract fun userDAO(): UserDAO
    abstract fun userGroupDAO(): UserGroupDAO
    abstract fun userRoleDAO(): UserRoleDAO
    abstract fun userCheckInActivityDAO(): UserCheckInActivityDAO
    abstract fun scheduleStudnetDAO(): ScheduleStudnetDAO
    abstract fun weekNumberDAO(): WeekNumberDAO
    abstract fun countStepsDAO(): CountStepsDAO
    abstract fun timetableDAO(): TimetableDAO
    abstract fun runningWayDao(): RunningWayDao
    abstract fun runningLocationDao(): RunningLocationDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, InjectorUtils.NAME_DATABASE)
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
//                        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
//                        WorkManager.getInstance().enqueue(request)
                    }
                })
                .build()
        }
    }
}
