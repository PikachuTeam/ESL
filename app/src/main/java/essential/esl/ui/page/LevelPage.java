package essential.esl.ui.page;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import essential.esl.ui.activity.MainActivity;
import essential.esl.ui.fragment.QuizzeFragment;

/**
 * Created by admin on 5/22/2016.
 */
public class LevelPage extends BasePage {
    private int LEVEL = 0;
    private int CATID = 0;
    private ArrayList<Conversation> listConversation;
    private RecyclerView recyclerView;
    private MyBaseFragment fragment;
    private LevelAdapter adapterPhrase;
    private LinearLayoutManager lmPhrase;

    @Override
    protected int getContentId() {
        return R.layout.page_level;
    }

    public LevelPage(MyBaseFragment fragment, MainActivity activity, int catID, int level) {
        super(fragment, activity);
        LEVEL = level;
        CATID = catID;
        this.fragment = fragment;
        listConversation = DataSource.getConversation(CATID, LEVEL);
        setupRecyclerView(listConversation);
    }

    public void setupRecyclerView(ArrayList<Conversation> listConversation) {
        recyclerView = (RecyclerView) getContent().findViewById(R.id.rv_conversation);
        recyclerView.setHasFixedSize(true);
        lmPhrase = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(lmPhrase);
        adapterPhrase = new LevelAdapter(listConversation);
        recyclerView.setAdapter(adapterPhrase);
    }

    public String getTitle() {
        String title = "";
        switch (LEVEL) {
            case 1:
                title = "EASY";
                break;
            case 2:
                title = "MEDIUM";
                break;
            case 3:
                title = "DIFFICULT";
                break;
            case 4:
                title = "EXTREME DIFFICULT";
                break;
        }
        return title;
    }


    public void updateData() {
        adapterPhrase.updateData();
    }


    public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> {
        public ArrayList<Conversation> list;

        public LevelAdapter(ArrayList<Conversation> list) {
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
            viewHolder.tvTitle.setText(list.get(i).title);
            viewHolder.tvStatus.setText(list.get(i).score + "/" + list.get(i).totalQuiz);
            viewHolder.tvLength.setText(list.get(i).length);
            Glide.with(fragment)
                    .load(list.get(i).avatarImageUrl).centerCrop().crossFade().error(R.drawable.err)
                    .into(viewHolder.ivAvatar);
            if (list.get(i).isFavorite > 0) {
                viewHolder.ivFavorite.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivFavorite.setVisibility(View.INVISIBLE);
            }
            if (list.get(i).isDownloaded > 0) {
                viewHolder.cvDownloaded.setVisibility(View.VISIBLE);
            } else {
                viewHolder.cvDownloaded.setVisibility(View.INVISIBLE);
            }
            if (activity.isProVersion()) {
                viewHolder.ivPro.setVisibility(View.INVISIBLE);
            } else if (list.get(i).isFree > 0) {
                viewHolder.ivPro.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.ivPro.setVisibility(View.VISIBLE);
            }
            if (list.get(i).score > 0) {
                viewHolder.ivStatus.setImageResource(R.drawable.dot);
                if (list.get(i).score / list.size() == 1)
                    viewHolder.ivStatus.setColorFilter(fragment.getResources().getColor(R.color.perfect));
                else if (list.get(i).score / list.size() == 8)
                    viewHolder.ivStatus.setColorFilter(fragment.getResources().getColor(R.color.good));
                else
                    viewHolder.ivStatus.setColorFilter(fragment.getResources().getColor(R.color.bad));
            } else viewHolder.ivStatus.setImageResource(R.drawable.dot_outline);
            viewHolder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyAnimation.animZoomWhenOnClick(v, this, 1, 1.1f, 1, 1.1f);
                    QuizzeFragment quizzeFragment = new QuizzeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("converstation", list.get(i));
                    quizzeFragment.setArguments(bundle);
                    fragment.replaceFragment(quizzeFragment, quizzeFragment.getTag());
                    activity.showBigAdsIfNeeded();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void updateData() {
            listConversation = DataSource.getConversation(CATID, LEVEL);
            list = listConversation;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvTitle;
            public TextView tvLength;
            public TextView tvStatus;
            public ImageView ivAvatar, ivPro;
            public ImageView ivStatus;
            public CardView item, cvDownloaded;
            public RelativeLayout ivFavorite;


            public ViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title_conversation);
                tvLength = (TextView) itemView.findViewById(R.id.tv_length);
                tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
                ivAvatar = (ImageView) itemView.findViewById(R.id.avatar);
                ivStatus = (ImageView) itemView.findViewById(R.id.iv_status);
                ivFavorite = (RelativeLayout) itemView.findViewById(R.id.item_favorite);
                ivPro = (ImageView) itemView.findViewById(R.id.id_iv_pro);
                item = (CardView) itemView.findViewById(R.id.item);
                cvDownloaded = (CardView) itemView.findViewById(R.id.id_iv_downloaded);

            }


        }
    }
}
