package essential.esl.ui.page;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import essential.esl.R;
import essential.esl.app.BasePage;
import essential.esl.app.MyBaseActivity;
import essential.esl.app.MyBaseFragment;
import essential.esl.app.MyItemLayout;
import essential.esl.data.DataSource;
import essential.esl.data.Quizze;

/**
 * Created by admin on 5/26/2016.
 */
public class QuizzesPage extends BasePage {
    private ArrayList<Quizze> listQuizze = new ArrayList<>();
    private RecyclerView rvListQuizze;
    private QuizzeAdapter adapter;
    private LinearLayoutManager lmPhrase;

    @Override
    protected int getContentId() {
        return R.layout.page_quizzes;
    }

    public QuizzesPage(MyBaseFragment fragment, MyBaseActivity activity, int idConversation) {
        super(fragment, activity);
        listQuizze = DataSource.getQuizzes(idConversation);
        setupRecyclerView(listQuizze);
    }

    public void setupRecyclerView(ArrayList<Quizze> listQuizze) {
        rvListQuizze = (RecyclerView) getContent().findViewById(R.id.rv_quizzes);
        rvListQuizze.setHasFixedSize(true);
        lmPhrase = new LinearLayoutManager(activity);
        rvListQuizze.setLayoutManager(lmPhrase);
        adapter = new QuizzeAdapter(listQuizze);
        rvListQuizze.setAdapter(adapter);
    }


    public class QuizzeAdapter extends RecyclerView.Adapter<QuizzeAdapter.ViewHolder> {
        public ArrayList<Quizze> list;

        public QuizzeAdapter(ArrayList<Quizze> list) {
            this.list = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_quizze, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            viewHolder.tvQuestion.setText(list.get(i).orderNo + ". " + list.get(i).question);
            for (int j = 0; j < 3; j++) {
                switch (j) {
                    case 0:
                        viewHolder.listViewAnswers.get(j).setText("A. " + list.get(i).listAnswers.get(j));
                        break;
                    case 1:
                        viewHolder.listViewAnswers.get(j).setText("B. " + list.get(i).listAnswers.get(j));
                        break;
                    case 2:
                        viewHolder.listViewAnswers.get(j).setText("C. " + list.get(i).listAnswers.get(j));
                        break;
                }

                viewHolder.listViewAnswers.get(j).setTextColor(fragment.getResources().getColor(R.color.white));
                viewHolder.listViewAnswers.get(j).setImage(R.drawable.dot_outline);
                viewHolder.listViewAnswers.get(j).setImageColorFilter(fragment.getResources().getColor(R.color.colorPrimaryDark));
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvQuestion;
            public ArrayList<MyItemLayout> listViewAnswers;

            public ViewHolder(View itemView) {
                super(itemView);
                listViewAnswers = new ArrayList<>();
                listViewAnswers.add((MyItemLayout) itemView.findViewById(R.id.answer1));
                listViewAnswers.add((MyItemLayout) itemView.findViewById(R.id.answer2));
                listViewAnswers.add((MyItemLayout) itemView.findViewById(R.id.answer3));
                tvQuestion = (TextView) itemView.findViewById(R.id.tv_question);

            }


        }
    }
}
