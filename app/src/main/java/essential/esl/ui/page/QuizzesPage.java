package essential.esl.ui.page;

import android.preference.PreferenceActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import essential.esl.R;
import essential.esl.app.BasePage;
import essential.esl.app.MyAnimation;
import essential.esl.app.MyBaseActivity;
import essential.esl.app.MyBaseFragment;
import essential.esl.app.MyDialog;
import essential.esl.app.MyItemLayout;
import essential.esl.data.DataSource;
import essential.esl.data.Quizze;
import essential.esl.ui.activity.MainActivity;

/**
 * Created by admin on 5/26/2016.
 */
public class QuizzesPage extends BasePage {
    private ArrayList<Quizze> listQuizze = new ArrayList<>();
    private RecyclerView rvListQuizze;
    private LinearLayoutManager lmPhrase;
    private boolean checkMode = false;
    private MyRecycleAdapter adapter2;
    private int idConversation;

    @Override
    protected int getContentId() {
        return R.layout.page_quizzes;
    }

    public QuizzesPage(MyBaseFragment fragment, MyBaseActivity activity, int idConversation) {
        super(fragment, activity);
        this.idConversation = idConversation;
        listQuizze = DataSource.getQuizzes(idConversation);
        setupRecyclerView(listQuizze);
    }

    public void setupRecyclerView(ArrayList<Quizze> listQuizze) {
        rvListQuizze = (RecyclerView) getContent().findViewById(R.id.rv_quizzes);
        rvListQuizze.setHasFixedSize(true);
        lmPhrase = new LinearLayoutManager(activity);
        rvListQuizze.setLayoutManager(lmPhrase);
        adapter2 = new MyRecycleAdapter(listQuizze);
        rvListQuizze.setAdapter(adapter2);
    }


    public class MyRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_FOOTER = 0;
        private static final int TYPE_ITEM = 1;
        private static final int TYPE_HEADER = 2;

        public ArrayList<Quizze> list;

        public MyRecycleAdapter(ArrayList<Quizze> listItems) {
            this.list = listItems;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_HEADER:
                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_list_quizze, parent, false);
                    return new HolderHeader(v);
                case TYPE_ITEM:
                    View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quizze, parent, false);
                    return new HolderItem(v1);
                case TYPE_FOOTER:
                    View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_list_quizze, parent, false);
                    return new HolderFooter(v2);
            }
            return null;
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int k) {
            final int i = k - 1;
            if (holder instanceof HolderItem) {
                final HolderItem viewHolder = (HolderItem) holder;
                viewHolder.tvQuestion.setText(list.get(i).orderNo + ". " + list.get(i).question);
                for (int j = 0; j < 3; j++) {
                    final int finalJ = j;
                    viewHolder.listViewAnswers.get(j).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MyAnimation.animZoomWhenOnClick(v, this, 1, 1.05f, 1, 1.05f);
                            if (!checkMode) {
                                for (int k = 0; k < 3; k++) {
                                    list.get(i).clearSelectedAnswer();
                                }
                                list.get(i).setSelectedAnswer(finalJ);
                            }
                            notifyDataSetChanged();
                        }
                    });
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
                    if (!checkMode) {
                        viewHolder.listViewAnswers.get(j).setDefautStyle();
                        if (list.get(i).getSelectedAnswer() >= 0) {
                            viewHolder.listViewAnswers.get(list.get(i).getSelectedAnswer()).setSelectedStyle();
                        }
                    } else {
                        viewHolder.listViewAnswers.get(j).setDefautStyle();
                        if (list.get(i).getSelectedAnswer() >= 0) {
                            if (list.get(i).getSelectedAnswer() == list.get(i).getRightAnswerPosition()) {
                                viewHolder.listViewAnswers.get(list.get(i).getSelectedAnswer()).setCorrectStyle();
                            } else {
                                viewHolder.listViewAnswers.get(list.get(i).getSelectedAnswer()).setWrongStyle();
                                viewHolder.listViewAnswers.get(list.get(i).getRightAnswerPosition()).setCorrectStyle();
                            }
                        }
                    }

                }
            } else if (holder instanceof HolderHeader) {
            } else if (holder instanceof HolderFooter) {
                final HolderFooter viewHolder = (HolderFooter) holder;
                if (!checkMode) viewHolder.tvScore.setText("0/" + list.size());
                viewHolder.btnReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyAnimation.animZoomWhenOnClick(v, this, 1, 1.05f, 1, 1.05f);
                        list = DataSource.getQuizzes(idConversation);
                        checkMode = false;
                        viewHolder.tvScore.setText("0/" + list.size());
                        notifyDataSetChanged();
                    }
                });
                viewHolder.btnCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyAnimation.animZoomWhenOnClick(v, this, 1, 1.05f, 1, 1.05f);
                        if (MainActivity.isProVersion()) {
                            checkMode = true;
                            notifyDataSetChanged();
                            viewHolder.tvScore.setText(getNumberAnswerCorrect() + "/" + list.size());
                            DataSource.updateScore(idConversation, getNumberAnswerCorrect());
                            MyAnimation.animZoomWhenOnClick(viewHolder.tvScore, this, 1, 1.5f, 1, 1.5f);
                        } else {
                            MyDialog.getInstance(fragment.getContext()).show();
                        }

                    }
                });

            }
        }

        public int getNumberAnswerCorrect() {
            int sum = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getSelectedAnswer() == list.get(i).getRightAnswerPosition()) sum++;
            }
            return sum;
        }

        public boolean isCheckAll() {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getSelectedAnswer() < 0) return false;
            }
            return true;
        }

        //    need to override this method
        @Override
        public int getItemViewType(int position) {
            if (position == 0) return TYPE_HEADER;
            else if (position > 0 && position < list.size() + 1) return TYPE_ITEM;
            else return TYPE_FOOTER;

        }


        @Override
        public int getItemCount() {
            return list.size() + 2;
        }

        class HolderFooter extends RecyclerView.ViewHolder {
            public CardView btnCheck, btnReset;
            public TextView tvScore;

            public HolderFooter(View itemView) {
                super(itemView);
                btnCheck = (CardView) itemView.findViewById(R.id.btn_Check);
                btnReset = (CardView) itemView.findViewById(R.id.btn_Reset);
                tvScore = (TextView) itemView.findViewById(R.id.tv_score);
            }
        }

        class HolderItem extends RecyclerView.ViewHolder {
            public TextView tvQuestion;
            public ArrayList<MyItemLayout> listViewAnswers;

            public HolderItem(View itemView) {
                super(itemView);
                listViewAnswers = new ArrayList<>();
                listViewAnswers.add((MyItemLayout) itemView.findViewById(R.id.answer1));
                listViewAnswers.add((MyItemLayout) itemView.findViewById(R.id.answer2));
                listViewAnswers.add((MyItemLayout) itemView.findViewById(R.id.answer3));
                tvQuestion = (TextView) itemView.findViewById(R.id.tv_question);
            }
        }

        class HolderHeader extends RecyclerView.ViewHolder {
            public HolderHeader(View itemView) {
                super(itemView);
            }
        }
    }
}
