package essential.esl.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tatteam.com.app_common.sqlite.BaseDataSource;

/**
 * Created by admin on 5/24/2016.
 */
public class DataSource extends BaseDataSource {
    public static ArrayList<Quizze> getQuizzes(int idConversation) {
        ArrayList<Quizze> listQuizze = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = openConnection();
        String query = "select * from Quizzes where ConversationID = ? order by OrderNo";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{"" + idConversation});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Quizze quizze = new Quizze();
            quizze.id = cursor.getInt(0);
            quizze.conversationID = cursor.getInt(1);
            quizze.orderNo = cursor.getInt(2);
            quizze.question = cursor.getString(3).trim();
            ArrayList<String> listAnswers = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                listAnswers.add(cursor.getString(3 + i));
            }
            quizze.listAnswers = listAnswers;
            quizze.setRightAnswer(cursor.getString(7).trim());
            listQuizze.add(quizze);
            cursor.moveToNext();

        }
        cursor.close();
        closeConnection();
        return listQuizze;
    }

    public static ArrayList<Conversation> getConversation(int catID, int level) {
        ArrayList<Conversation> listConversation = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = openConnection();
        String query = "select* from Conversations where CatID =? and  Level =?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{"" + catID, "" + level});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Conversation conversation = new Conversation();
            conversation.id = cursor.getInt(0);
            conversation.catID = cursor.getInt(1);
            conversation.level = cursor.getInt(2);
            if (cursor.getString(4) != null)
                conversation.type = cursor.getString(4).trim();
            else conversation.type = "";
            if (cursor.getString(3) != null)
                conversation.topic = cursor.getString(3).trim();
            else conversation.topic = "";
            if (cursor.getString(5) != null)
                conversation.speakers = cursor.getString(5).trim();
            else conversation.speakers = "";
            conversation.length = cursor.getString(6);
            conversation.title = cursor.getString(7);
            conversation.avatarImageUrl = cursor.getString(10);
            conversation.audioUrl = cursor.getString(13);
            if (cursor.getString(8) == null)
                conversation.helpFulTip = "";
            else
                conversation.helpFulTip = cursor.getString(8);
            if (cursor.getString(14) == null)
                conversation.script = "";
            else
                conversation.script = cursor.getString(14);
            if (cursor.getString(15) == null)
                conversation.keyVocabulary = "";
            else
                conversation.keyVocabulary = cursor.getString(15);
            conversation.isDownloaded = cursor.getInt(18);
            conversation.totalQuiz = cursor.getInt(19);
            conversation.score = cursor.getInt(20);
            conversation.isFree = cursor.getInt(22);
            conversation.isFavorite = cursor.getInt(21);
            listConversation.add(conversation);
            cursor.moveToNext();
        }
        cursor.close();
        closeConnection();
        return listConversation;
    }

    public static void setFavorite(int idConversation) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        int newRecent = getMaxFavorite() + 1;
        String value = newRecent + "";
        Cursor cursor = sqLiteDatabase.rawQuery("UPDATE Conversations SET isFavorite= ? WHERE ID =?", new String[]{value, idConversation + ""});
        cursor.moveToFirst();
        cursor.close();
        closeConnection();
    }

    public static void removeFavorite(int idConversation) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        Cursor cursor = sqLiteDatabase.rawQuery("UPDATE Conversations SET isFavorite= 0 WHERE ID =?", new String[]{idConversation + ""});
        cursor.moveToFirst();
        cursor.close();
        closeConnection();
    }

    public static ArrayList<Conversation> getListFavorite() {
        ArrayList<Conversation> listConversation = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = openConnection();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Conversations where isFavorite > 0 order by isFavorite desc", null);
        if (cursor.getCount() == 0) return listConversation;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Conversation conversation = new Conversation();
            conversation.id = cursor.getInt(0);
            conversation.catID = cursor.getInt(1);
            conversation.level = cursor.getInt(2);
            if (cursor.getString(4) != null)
                conversation.type = cursor.getString(4).trim();
            else conversation.type = "";
            if (cursor.getString(3) != null)
                conversation.topic = cursor.getString(3).trim();
            else conversation.topic = "";
            if (cursor.getString(5) != null)
                conversation.speakers = cursor.getString(5).trim();
            else conversation.speakers = "";
            conversation.length = cursor.getString(6);
            conversation.title = cursor.getString(7);
            conversation.avatarImageUrl = cursor.getString(10);
            conversation.audioUrl = cursor.getString(13);
            if (cursor.getString(8) == null)
                conversation.helpFulTip = "";
            else
                conversation.helpFulTip = cursor.getString(8);
            if (cursor.getString(14) == null)
                conversation.script = "";
            else
                conversation.script = cursor.getString(14);
            if (cursor.getString(15) == null)
                conversation.keyVocabulary = "";
            else
                conversation.keyVocabulary = cursor.getString(15);
            conversation.isDownloaded = cursor.getInt(18);
            conversation.totalQuiz = cursor.getInt(19);
            conversation.score = cursor.getInt(20);
            conversation.isFree = cursor.getInt(22);
            conversation.isFavorite = cursor.getInt(21);
            listConversation.add(conversation);
            cursor.moveToNext();
        }
        cursor.close();
        closeConnection();
        return listConversation;
    }

    public static int getMaxFavorite() {
        int max;
        SQLiteDatabase sqLiteDatabase = openConnection();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Conversations where isFavorite > 0 order by isFavorite desc limit 1", null);
        if (cursor.getCount() == 0) return 0;
        cursor.moveToFirst();
        max = cursor.getInt(21);
        cursor.close();
        closeConnection();
        return max;

    }

    public static int getFavorite(int id) {
        int max;
        SQLiteDatabase sqLiteDatabase = openConnection();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Conversations WHERE ID = ?", new String[]{id + ""});
        if (cursor.getCount() == 0) return 0;
        cursor.moveToFirst();
        max = cursor.getInt(21);
        cursor.close();
        closeConnection();
        return max;

    }

    public static void updateScore(int idConversations, int score) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        Cursor cursor = sqLiteDatabase.rawQuery("UPDATE Conversations SET Score = ? WHERE ID = ?", new String[]{score + "", idConversations + ""});
        cursor.moveToFirst();
        cursor.close();
        openConnection();
    }

    public static void updateDownloaded(int idConversations, int score) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        Cursor cursor = sqLiteDatabase.rawQuery("UPDATE Conversations SET IsDownloaded = ? WHERE ID = ?", new String[]{score + "", idConversations + ""});
        cursor.moveToFirst();
        cursor.close();
        openConnection();
    }

    public static int getDownloaded(int idConversations) {
        SQLiteDatabase sqLiteDatabase = openConnection();
        Cursor cursor = sqLiteDatabase.rawQuery("select IsDownloaded from Conversations WHERE ID = ?", new String[]{idConversations + ""});
        cursor.moveToFirst();
        int isDownloaded = cursor.getInt(0);
        cursor.close();
        openConnection();
        return isDownloaded;
    }

    public static boolean isFileExists(String name) {
        File extStore = Environment.getExternalStorageDirectory();
        File myFile = new File(extStore.getAbsolutePath() + "/Essential/ESLAudios/" + name + ".mp3");

        if (myFile.exists())
            return true;

        return false;
    }
}
