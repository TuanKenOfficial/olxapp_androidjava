package com.example.olx.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.olx.CurrencyFormatter;
import com.example.olx.R;
import com.example.olx.Utils;
import com.example.olx.databinding.RowOrderBinding;
import com.example.olx.model.ModelCart;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.HolderOrder> {

    private RowOrderBinding binding;
    private  Context context;
    private  ArrayList<ModelCart> cartArrayList;
    private final String TAG = "AD_Order";

    private FirebaseAuth firebaseAuth;

    public AdapterOrder(Context context, ArrayList<ModelCart> cartArrayList) {
        this.context = context;
        this.cartArrayList = cartArrayList;
    }

    @NonNull
    @Override
    public HolderOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        binding = RowOrderBinding.inflate(LayoutInflater.from(context), parent, false);
        return new AdapterOrder.HolderOrder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrder holder, int position) {
        ModelCart modelCart = cartArrayList.get(position);
        int idGH = modelCart.getId();
        String pId = modelCart.getProductAdsId();
        String title = modelCart.getTen();
        int Soluongdadat = modelCart.getSoluongdadat();
        int price = modelCart.getPrice();
        int tongtiensp = modelCart.getTongtien();
        String uidNguoiMua = modelCart.getUidNguoiMua();

        //check tài khoản đăng nhập
        firebaseAuth = FirebaseAuth.getInstance();

        Log.d(TAG, "onBindViewHolder: idGH: "+idGH);
        Log.d(TAG, "onBindViewHolder: title: "+title);
        Log.d(TAG, "onBindViewHolder: Soluongdadat: "+Soluongdadat);
        Log.d(TAG, "onBindViewHolder: giá: "+price);
        Log.d(TAG, "onBindViewHolder: tongtiensp: "+tongtiensp);
        Log.d(TAG, "onBindViewHolder: id ảnh: "+pId);
        Log.d(TAG, "onBindViewHolder: uidNguoiMua: "+uidNguoiMua);
        holder.titleTv.setText(title);
        holder.sQuantityTv.setText("Số lượng: "+Soluongdadat);
        holder.priceTv.setText("Giá: "+CurrencyFormatter.getFormatter().format(Double.valueOf(String.valueOf(price))));
        holder.finalPriceTv.setText("Tổng tiền: "+CurrencyFormatter.getFormatter().format(Double.valueOf(tongtiensp)));

       loadImage(modelCart, holder);
    }



    @Override
    public int getItemCount() {
        return cartArrayList.size(); //return list size
    }

    //view holder class
    public class HolderOrder extends RecyclerView.ViewHolder{

        //views of row_ordereditem.xml
        public TextView titleTv, sQuantityTv, priceTv, finalPriceTv;
        public ShapeableImageView productIv;
        public HolderOrder(@NonNull View itemView) {
            super(itemView);

            //init views
            productIv = binding.productIv;
            titleTv = binding.titleTv;
            sQuantityTv = binding.sQuantityTv;
            priceTv = binding.priceTv;
            finalPriceTv = binding.finalPriceTv;

        }
    }

    private void loadImage(ModelCart modelCart, HolderOrder holder) {
        // lấy hình ảnh đầu tiên của sản phẩm
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GioHang");
        reference.orderByChild("uidNguoiMua").equalTo(modelCart.getUidNguoiMua())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // lấy hình ảnh đầu tiên của sản phẩm
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ProductAds");
                        ref.child(modelCart.getProductAdsId()).child("Images").limitToFirst(1)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            String imageUrl = "" + ds.child("imageUrl").getValue();
                                            Log.d(TAG, "onDataChange: imageUrl:"+imageUrl);
                                            try {
                                                Glide.with(context)
                                                        .load(imageUrl)
                                                        .placeholder(R.drawable.image)
                                                        .into(holder.productIv);
                                            }catch (Exception e){
                                                Log.e(TAG, "onBindViewHolder: ",e);
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
