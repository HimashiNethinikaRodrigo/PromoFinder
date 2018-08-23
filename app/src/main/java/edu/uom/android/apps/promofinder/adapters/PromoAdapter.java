package edu.uom.android.apps.promofinder.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.uom.android.apps.promofinder.R;
import edu.uom.android.apps.promofinder.data.FirebaseStorageManager;
import edu.uom.android.apps.promofinder.models.Promo;
import edu.uom.android.apps.promofinder.util.DateTimeUtil;
import edu.uom.android.apps.promofinder.util.Utility;
import timber.log.Timber;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.ItemViewHolder> {

    private Context mContext = null;
    private List<Promo> promoList;
    private ItemListItemClickListener mListener;
    private FirebaseStorageManager storageManager;

    public PromoAdapter(Context context, List<Promo> dataList, ItemListItemClickListener mListener) {
        this.mContext = context;
        this.promoList = dataList;
        this.mListener = mListener;
        storageManager = FirebaseStorageManager.getInstance();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_promo, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Promo promo = promoList.get(position);
        holder.setData(promo);
    }

    @Override
    public int getItemCount() {
        return promoList.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtLongDes)
        TextView txtLongDes;
        @BindView(R.id.txtShortDes)
        TextView txtShortDes; @BindView(R.id.txtExpireDate)
        TextView txtExpireDate;
        @BindView(R.id.llMain)
        LinearLayout llMain;
        @BindView(R.id.imgPromo)
        ImageView imgPromo;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(Promo promo) {
            txtShortDes.setText(promo.getShortDescription());
            txtLongDes.setText(promo.getLongDescription());
            txtExpireDate.setText(DateTimeUtil.getISODateToJustdate(new DateTime(promo.getEndDate())));

            Timber.d("Image url %s",promo.getImageUrl());

            if(promo.getImageUrl()!=null) {
                storageManager.getPromoImage(promo.getImageUrl(), new FirebaseStorageManager.DownloadableImageUriCallback() {
                    @Override
                    public void onDownloadableUriSuccess(Uri uri) {
                        Utility.loadImage(mContext, imgPromo, uri);
                    }

                    @Override
                    public void onDownloadableUriError(Exception e) {
                        Timber.d("onDownloadableUriError %s", e.getMessage());
                    }
                });
            }

            llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickItem(promo);
                }
            });
        }
    }

    public interface ItemListItemClickListener {

        void onClickItem(Promo promo);

    }

}
