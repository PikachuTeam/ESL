package essential.esl.data;

import java.io.Serializable;

/**
 * Created by admin on 5/24/2016.
 */
public class Conversation implements Serializable {
    public int id;
    public int catID;
    public int level;
    public String topic;
    public String type;
    public String speakers;
    public String length;
    public String title;
    public String preListeningExecises;
    public String helpFulTip;
    public String avatarImageUrl;
    public String audioUrl;
    public String script;
    public String keyVocabulary;
    public int isDownloaded;
    public int totalQuiz;
    public int score;
    public int isFree;
    public int isFavorite;


}
