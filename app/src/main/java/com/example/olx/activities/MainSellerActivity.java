package com.example.olx.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.olx.fragment.HomeSellerFragment;
import com.example.olx.R;
import com.example.olx.Utils;
import com.example.olx.databinding.ActivityMainSellerBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainSellerActivity extends AppCompatActivity {

    private ActivityMainSellerBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private static final String TAG ="MainSeller";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainSellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        showHomeFragment(); //Home Fragment
        loadSellerProfile();
        if (firebaseUser == null){
            Utils.toast(MainSellerActivity.this,"Bạn chưa đăng nhập");
            startActivity(new Intent(MainSellerActivity.this,LoginOptionActivity.class));
            finish();
        }
        else {
            //code sau
        }


        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
            }
        });
        binding.bottomNv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId== R.id.menu_home){
                    Log.d(TAG, "onNavigationItemSelected: Home");
                    //Home item click, Fragment Home
                    showHomeFragment();
                    return true;

                }
                else if (itemId == R.id.menu_chat){
                    //Home item click, Fragment Chat
                    if (firebaseUser == null){
                        Utils.toast(MainSellerActivity.this,"Bạn cần đăng nhập tài khoản");
                        startActivity(new Intent(MainSellerActivity.this,LoginOptionActivity.class));
                        return false;
                    }
                    else{
                        showChatsFragment();

                        return true;
                    }

                }
                else if (itemId == R.id.menu_fav){
                    //Home item click, Fragment Navigition
                    if (firebaseUser == null){
                        Utils.toast(MainSellerActivity.this,"Bạn cần đăng nhập tài khoản");
                        startActivity(new Intent(MainSellerActivity.this,LoginOptionActivity.class));
                        return false;
                    }
                    else{
                        showFavFragment();
                        return true;
                    }

                }
                else if (itemId == R.id.menu_person){
                    //Home item click, Fragment Profile
                    if (firebaseUser == null){
                        Utils.toast(MainSellerActivity.this,"Bạn cần đăng nhập tài khoản");
                        startActivity(new Intent(MainSellerActivity.this,LoginOptionActivity.class));
                        return false;
                    }
                    else{
                        showProfileFragment();
                        return true;
                    }

                }
                else {
                    return false;
                }

            }
        });

        binding.sellFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainSellerActivity.this, AdCreateActivity.class);
                intent.putExtra("isEditMode", false);
                startActivity(intent);
            }
        });
        //popup menu
        final PopupMenu popupMenu = new PopupMenu(MainSellerActivity.this, binding.moreBtn);
        //add menu items to our menu
        popupMenu.getMenu().add("Cài đặt");
        popupMenu.getMenu().add("Reviews");
        popupMenu.getMenu().add("Khuyến mãi");
        //handle menu item click
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getTitle() == "Cài đặt"){
                //start settings screen
                Utils.toast(MainSellerActivity.this,"Chức năng đang code, đợi clip sau");
//                startActivity(new Intent(MainSellerActivity.this, SettingsActivity.class));
            }
            else if (menuItem.getTitle() == "Reviews"){
                //open same reviews activity as used in user main page
                Utils.toast(MainSellerActivity.this,"Chức năng đang code, đợi clip sau");
//                Intent intent = new Intent(MainSellerActivity.this, ShopReviewsActivity.class);
//                intent.putExtra("shopUid", ""+firebaseAuth.getUid());
//                startActivity(intent);
            }
            else if (menuItem.getTitle() == "Khuyến mãi"){
                //start promotions list screen
                Utils.toast(MainSellerActivity.this,"Chức năng đang code, đợi clip sau");
//                startActivity(new Intent(MainSellerActivity.this, PromotionCodesActivity.class));
            }

            return true;
        });

        //show more options: Settings, Reviews, Promotion Codes
        binding.moreBtn.setOnClickListener(view -> {
            //show popup menu
            popupMenu.show();
        });
    }

    private void loadSellerProfile() {
        Log.d(TAG, "loadSellerProfile: ");
        String registerUserUid = firebaseAuth.getUid();
        Log.d(TAG, "loadSellerProfile: registerUserUid:"+registerUserUid);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(registerUserUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name =""+snapshot.child("name").getValue();
                        Log.d(TAG, "onDataChange: name"+name);
                        String shopName =""+snapshot.child("shopName").getValue();
                        Log.d(TAG, "onDataChange: shopName"+shopName);
                        String email =""+snapshot.child("email").getValue();
                        Log.d(TAG, "onDataChange: email"+email);
                        String phone =""+snapshot.child("phone").getValue();
                        Log.d(TAG, "onDataChange: phone"+phone);
                        String profileImage =""+snapshot.child("profileImage").getValue();
                        Log.d(TAG, "onDataChange: profileImage"+profileImage);

                        binding.nameTv.setText(name);
                        binding.shopNameTv.setText(shopName);
                        binding.emailTv.setText(email);
                        binding.phoneTv.setText(phone);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void showProfileFragment() {
        Utils.toast(MainSellerActivity.this,"Chức năng đang code, đợi clip sau");
    }

    private void showFavFragment() {
        Utils.toast(MainSellerActivity.this,"Chức năng đang code, đợi clip sau");
    }

    private void showChatsFragment() {
        Utils.toast(MainSellerActivity.this,"Chức năng đang code, đợi clip sau");
    }

    private void showHomeFragment() {
        HomeSellerFragment fragment = new HomeSellerFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentsFl.getId(), fragment, "HomeFragment");
        fragmentTransaction.commit();
    }
}