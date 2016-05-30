package essential.esl.ui.page;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import essential.esl.R;
import essential.esl.app.BasePage;
import essential.esl.app.MyAnimation;
import essential.esl.app.MyBaseActivity;
import essential.esl.app.MyBaseFragment;
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
        adapter2 = new MyRecycleAdapter(getHeader(), listQuizze);
        rvListQuizze.setAdapter(adapter2);
    }

    public PreferenceActivity.Header getHeader() {
        PreferenceActivity.Header header = new PreferenceActivity.Header();
        return header;
    }



    public class MyRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        PreferenceActivity.Header header;
        public ArrayList<Quizze> list;

        public MyRecycleAdapter(PreferenceActivity.Header header, ArrayList<Quizze> listItems) {
            this.header = header;
            this.list = listItems;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_list_quizze, parent, false);
                return new VHHeader(v);
            } else if (viewType == TYPE_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quizze, parent, false);
                return new VHItem(v);
            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
            if (holder instanceof VHHeader) {
                final VHHeader viewHolder = (VHHeader) holder;
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
                        if (isCheckAll()) {
                            checkMode = true;
                            notifyDataSetChanged();
                            viewHolder.tvScore.setText(getNumberAnswerCorrect() + "/" + list.size());
                            DataSource.updateScore(idConversation, getNumberAnswerCorrect());
                            MyAnimation.animZoomWhenOnClick(viewHolder.tvScore, this, 1, 1.5f, 1, 1.5f);
                        } else
                            fragment.makeMessage(rvListQuizze, R.string.choose_answer_for_all_question);


                    }
                });

            } else if (holder instanceof VHItem) {
                final VHItem viewHolder = (VHItem) holder;
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
                        if (isCheckAll()) {
                            viewHolder.listViewAnswers.get(j).setDefautStyle();
                            if (list.get(i).getSelectedAnswer() == list.get(i).getRightAnswerPosition()) {
                                viewHolder.listViewAnswers.get(list.get(i).getSelectedAnswer()).setCorrectStyle();
                            } else {
                                viewHolder.listViewAnswers.get(list.get(i).getSelectedAnswer()).setWrongStyle();
                                viewHolder.listViewAnswers.get(list.get(i).getRightAnswerPosition()).setCorrectStyle();
                            }
                        } else {
                            //Toast
                        }
                    }

                }
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
            if (isPositionHeader(position))
                return TYPE_HEADER;
            return TYPE_ITEM;
        }

        private boolean isPositionHeader(int position) {
            return position == list.size();
        }

        @Override
        public int getItemCount() {
            return list.size() + 1;
        }

        class VHHeader extends RecyclerView.ViewHolder {
            public CardView btnCheck, btnReset;
            public TextView tvScore;

            public VHHeader(View itemView) {
                super(itemView);
                btnCheck = (CardView) itemView.findViewById(R.id.btn_Check);
                btnReset = (CardView) itemView.findViewById(R.id.btn_Reset);
                tvScore = (TextView) itemView.findViewById(R.id.tv_score);
            }
        }

        class VHItem extends RecyclerView.ViewHolder {
            public TextView tvQuestion;
            public ArrayList<MyItemLayout> listViewAnswers;

            public VHItem(View itemView) {
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
