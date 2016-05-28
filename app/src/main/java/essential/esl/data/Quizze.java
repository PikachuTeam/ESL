package essential.esl.data;

import java.util.ArrayList;

/**
 * Created by admin on 5/25/2016.
 */
public class Quizze {
    public int id;
    public int conversationID;
    public int orderNo;
    public String question;
    public ArrayList<String> listAnswers;
    private int selectedAnswer = -1;
    private String rightAnswer;


    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public int getRightAnswerPosition() {
        switch (rightAnswer) {
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
        }
        return 0;
    }

    public void clearSelectedAnswer() {
        selectedAnswer = -1;
    }

    public void setSelectedAnswer(int i) {
        selectedAnswer = i;
    }

    public int getSelectedAnswer() {
        return selectedAnswer;

    }
}
