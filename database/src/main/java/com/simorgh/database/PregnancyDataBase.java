package com.simorgh.database;

import android.content.Context;

import com.simorgh.database.dao.AlcoholDAO;
import com.simorgh.database.dao.ArticleDAO;
import com.simorgh.database.dao.ArticleWithParagraphDAO;
import com.simorgh.database.dao.BloodPressureDAO;
import com.simorgh.database.dao.CigaretteDAO;
import com.simorgh.database.dao.DateDAO;
import com.simorgh.database.dao.DrugDAO;
import com.simorgh.database.dao.ExerciseTimeDAO;
import com.simorgh.database.dao.FeverDAO;
import com.simorgh.database.dao.ParagraphDAO;
import com.simorgh.database.dao.SleepTimeDAO;
import com.simorgh.database.dao.UserDAO;
import com.simorgh.database.dao.WeekDAO;
import com.simorgh.database.dao.WeightlDAO;
import com.simorgh.database.model.Alcohol;
import com.simorgh.database.model.Article;
import com.simorgh.database.model.BloodPressure;
import com.simorgh.database.model.Cigarette;
import com.simorgh.database.model.Drug;
import com.simorgh.database.model.ExerciseTime;
import com.simorgh.database.model.Fever;
import com.simorgh.database.model.Paragraph;
import com.simorgh.database.model.SleepTime;
import com.simorgh.database.model.User;
import com.simorgh.database.model.Week;
import com.simorgh.database.model.Weight;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Keep
@TypeConverters(com.simorgh.database.TypeConverters.class)
@Database(entities = {Paragraph.class, Article.class,
        User.class, Week.class, Drug.class, Weight.class,
        BloodPressure.class, Fever.class, Alcohol.class,
        Cigarette.class, ExerciseTime.class, SleepTime.class},
        version = 1, exportSchema = false)
public abstract class PregnancyDataBase extends RoomDatabase {
    private static final String DB_NAME = "pregnancy-db";
    private static volatile PregnancyDataBase INSTANCE;


    public abstract UserDAO userDAO();

    public abstract ArticleDAO articleDAO();

    public abstract WeekDAO weekDAO();

    public abstract DrugDAO drugDAO();

    public abstract AlcoholDAO alcoholDAO();

    public abstract CigaretteDAO cigaretteDAO();

    public abstract ExerciseTimeDAO exerciseTimeDAO();

    public abstract BloodPressureDAO bloodPressureDAO();

    public abstract WeightlDAO weightlDAO();

    public abstract FeverDAO feverDAO();

    public abstract SleepTimeDAO sleepTimeDAO();

    public abstract ParagraphDAO paragraphDAO();

    public abstract DateDAO dateDAO();

    public abstract ArticleWithParagraphDAO articleWithParagraphDAO();


    public static PregnancyDataBase getDatabase(@NonNull final Context context) {
        if (INSTANCE == null) {
            synchronized (PregnancyDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(context.getApplicationContext(), PregnancyDataBase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
//                            .addMigrations(new Migration(1, 2) {
//                                @Override
//                                public void migrate(@NonNull SupportSQLiteDatabase database) {
////                                    database.execSQL("");
//                                }
//                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
