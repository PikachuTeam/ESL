package essential.esl.ui.page;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import essential.esl.R;
import essential.esl.app.BasePage;
import essential.esl.app.MyAnimation;
import essential.esl.app.MyBaseActivity;
import essential.esl.app.MyBaseFragment;
import essential.esl.data.Conversation;
import essential.esl.data.DataSource;
import essential.esl.data.Quizze;
import essential.esl.ui.fragment.QuizzeFragment;

/**
 * Created by admin on 5/26/2016.
 */
public class QuizzesPage extends BasePage {
    private ArrayList<Quizze> listQuizze = new ArrayList<>();
    private RecyclerView rvListQuizze;

    @Override
    protected int getContentId() {
        return R.layout.page_quizzes;
    }

    public QuizzesPage(MyBaseFragment fragment, MyBaseActivity activity, int idConversation) {
        super(fragment, activity);
        rvListQuizze = (RecyclerView) getContent().findViewById(R.id.rv_quizzes);
        listQuizze = DataSource.getQuizzes(idConversation);
    }


    public class QuizzeAdapter extends RecyclerView.Adapter<QuizzeAdapter.ViewHolder> {
        public ArrayList<Quizze> list;

        public QuizzeAdapter(ArrayList<Quizze> list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_conversation, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);

            }


        }
    }
}
