package essential.esl.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import essential.esl.R;
import essential.esl.app.MyAnimation;
import essential.esl.app.MyBaseFragment;
import essential.esl.data.Conversation;
import essential.esl.data.DataSource;
import essential.esl.ui.activity.MainActivity;
import essential.esl.ui.page.LevelPage;

/**
 * Created by admin on 5/31/2016.
 */
public class FavoriteFragment extends MyBaseFragment implements View.OnClickListener {
    private ArrayList<Conversation> listConversation;
    private LevelAdapter adapter;
    private LinearLayout btnBack, btnShare;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private LinearLayoutManager lmPhrase;

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_favorite;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {

        if (adapter == null) {
            adapter = new LevelAdapter();
        } else adapter.updateData();
        init(rootView);


    }

    private void init(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_conversation);
        btnBack = (LinearLayout) rootView.findViewById(R.id.btn_Back);
        btnShare = (LinearLayout) rootView.findViewById(R.id.btn_Share);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        recyclerView.setHasFixedSize(true);
        lmPhrase = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lmPhrase);
        recyclerView.setAdapter(adapter);
        btnShare.setVisibility(View.GONE);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_Back) {
            ((MainActivity) getBaseActivity()).onBackPressed();
        }
    }

    public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> {
        public ArrayList<Conversation> list;

        public LevelAdapter() {
            list = DataSource.getListFavorite();
//            Toast.makeText(getContext(), list.size() + "", Toast.LENGTH_SHORT).show();
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
            Glide.with(FavoriteFragment.this)
                    .load(list.get(i).avatarImageUrl).centerCrop().crossFade().error(R.drawable.logo)
                    .into(viewHolder.ivAvatar);
            if (list.get(i).score > 0) {
                viewHolder.ivStatus.setImageResource(R.drawable.dot);
                if (list.get(i).score / list.size() == 1)
                    viewHolder.ivStatus.setColorFilter(FavoriteFragment.this.getResources().getColor(R.color.perfect));
                else if (list.get(i).score / list.size() == 8)
                    viewHolder.ivStatus.setColorFilter(FavoriteFragment.this.getResources().getColor(R.color.good));
                else
                    viewHolder.ivStatus.setColorFilter(FavoriteFragment.this.getResources().getColor(R.color.bad));
            } else viewHolder.ivStatus.setImageResource(R.drawable.dot_outline);
            viewHolder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyAnimation.animZoomWhenOnClick(v, this, 1, 1.1f, 1, 1.1f);
                    QuizzeFragment quizzeFragment = new QuizzeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("converstation", list.get(i));
                    quizzeFragment.setArguments(bundle);
                    FavoriteFragment.this.replaceFragment(quizzeFragment, quizzeFragment.getTag());
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void updateData() {
            listConversation = DataSource.getListFavorite();
            list = listConversation;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvTitle;
            public TextView tvLength;
            public TextView tvStatus;
            public ImageView ivAvatar;
            public ImageView ivStatus;
            public CardView item;


            public ViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_title_conversation);
                tvLength = (TextView) itemView.findViewById(R.id.tv_length);
                tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
                ivAvatar = (ImageView) itemView.findViewById(R.id.avatar);
                ivStatus = (ImageView) itemView.findViewById(R.id.iv_status);
                item = (CardView) itemView.findViewById(R.id.item);

            }


        }
    }
}
