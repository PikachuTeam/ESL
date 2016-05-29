package essential.esl.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

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
            quizze.question = cursor.getString(3);
            ArrayList<String> listAnswers = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                listAnswers.add(cursor.getString(3 + i));
            }
            quizze.listAnswers = listAnswers;
            quizze.setRightAnswer(cursor.getString(7));
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
            conversation.topic = cursor.getString(3);
            conversation.type = cursor.getString(4);
            conversation.speakers = cursor.getString(5);
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
            listConversation.add(conversation);
            cursor.moveToNext();
        }
        cursor.close();
        closeConnection();
        return listConversation;
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
