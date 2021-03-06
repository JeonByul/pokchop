package com.shine_star_11.abc;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.shine_star_11.abc.SignInActivity;
import com.shine_star_11.abc.model.BackPressCloseHandler;
import com.shine_star_11.abc.model.pokeStopGym;
import com.shine_star_11.abc.model.postitem;
import com.shine_star_11.abc.pokemonPost;
import com.shine_star_11.abc.viewHolder.PostAdapter;

import static android.view.View.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, OnMapLongClickListener, GoogleMap.OnMapClickListener, GoogleApiClient.OnConnectionFailedListener {

    SupportMapFragment sMapFragment;
    private GoogleMap mMap;
    private LinearLayout llBottomSheet;
    private Marker marker;
    List<ClipData.Item> items;

    // back press handler
    private BackPressCloseHandler backPressCloseHandler;

    // Admob instance variables
    public AdRequest adRequest = new AdRequest.Builder().build();
    public AdView mAdView;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseUser CurrentUser;
    private String mUsername;
    private String mUseremail;
    private String mPhotoUrl;
    public static final String ANONYMOUS = "anonymous";
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient client;

    // Firebase db instaces
    private DatabaseReference mDatabase;
    private Button enroll;
    private byte[] img_up;

    // Firebase analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    // pokemon list
    String[] textArr = {" 포켓몬 선택하기 ", " 1 이상해씨 Bulbasaur ", " 2 이상해풀 Ivysaur ", " 3 이상해꽃 Venusaur ", " 4 파이리 Charmander ", " 5 리자드 Charmeleon ", " 6 리자몽 Charizard ", " 7 꼬부기 Squirtle ", " 8 어니부기 Wartortle ", " 9 거북왕 Blastoise ", " 10 캐터피 Caterpie ", " 11 단데기 Metapod ", " 12 버터플 Butterfree ", " 13 뿔충이 Weedle ", " 14 딱충이 Kakuna ", " 15 독침붕 Beedrill ", " 16 구구 Pidgey ", " 17 피죤 Pidgeotto ", " 18 피죤투 Pidgeot ", " 19 꼬렛 Rattata ", " 20 레트라 Raticate ", " 21 깨비참 Spearow ", " 22 깨비드릴조 Fearow ", " 23 아보 Ekans ", " 24 아보크 Arbok ", " 25 피카츄 Pikachu ", " 26 라이츄 Raichu ", " 27 모래두지 Sandshrew ", " 28 고지 Sandslash ", " 29 니드런♀ Nidoran♀ ", " 30 니드리나 Nidorina ", " 31 니드퀸 Nidoqueen ", " 32 니드런♂ Nidoran♂ ", " 33 니드리노 Nidorino ", " 34 니드킹 Nidoking ", " 35 삐삐 Clefairy ", " 36 픽시 Clefable ", " 37 식스테일 Vulpix ", " 38 나인테일 Ninetales ", " 39 푸린 Jigglypuff ", " 40 푸크린 Wigglytuff ", " 41 주뱃 Zubat ", " 42 골뱃 Golbat ", " 43 뚜벅쵸 Oddish ", " 44 냄새꼬 Gloom ", " 45 라플레시아 Vileplume ", " 46 파라스 Paras ", " 47 파라섹트 Parasect ", " 48 콘팡 Venonat ", " 49 도나리 Venomoth ", " 50 디그다 Diglett ", " 51 닥트리오 Dugtrio ", " 52 나옹 Meowth ", " 53 페르시온 Persian ", " 54 고라파덕 Psyduck ", " 55 골덕 Golduck ", " 56 망키 Mankey ", " 57 성원숭 Primeape ", " 58 가디 Growlithe ", " 59 윈디 Arcanine ", " 60 발챙이 Poliwag ", " 61 슈륙챙이 Poliwhirl ", " 62 강챙이 Poliwrath ", " 63 캐이시 Abra ", " 64 윤겔라 Kadabra ", " 65 후딘 Alakazam ", " 66 알통몬 Machop ", " 67 근육몬 Machoke ", " 68 괴력몬 Machamp ", " 69 모다피 Bellsprout ", " 70 우츠동 Weepinbell ", " 71 우츠보트 Victreebel ", " 72 왕눈해 Tentacool ", " 73 독파리 Tentacruel ", " 74 꼬마돌 Geodude ", " 75 데구리 Graveler ", " 76 딱구리 Golem ", " 77 포니타 Ponyta ", " 78 날쌩마 Rapidash ", " 79 야돈 Slowpoke ", " 80 야도란 Slowbro ", " 81 코일 Magnemite ", " 82 레어코일 Magneton ", " 83 파오리 Farfetch’d ", " 84 두두 Doduo ", " 85 두트리오 Dodrio ", " 86 쥬쥬 Seel ", " 87 쥬레곤 Dewgong ", " 88 질퍽이 Grimer ", " 89 질뻐기 Muk ", " 90 셀러 Shellder ", " 91 파르셀 Cloyster ", " 92 고오스 Gastly ", " 93 고우스트 Haunter ", " 94 팬텀 Gengar ", " 95 롱스톤 Onix ", " 96 슬리프 Drowzee ", " 97 슬리퍼 Hypno ", " 98 크랩 Krabby ", " 99 킹크랩 Kingler ", " 100 찌리리공 Voltorb ", " 101 붐볼 Electrode ", " 102 아라리 Exeggcute ", " 103 나시 Exeggutor ", " 104 탕구리 Cubone ", " 105 텅구리 Marowak ", " 106 시라소몬 Hitmonlee ", " 107 홍수몬 Hitmonchan ", " 108 내루미 Lickitung ", " 109 또가스 Koffing ", " 110 또도가스 Weezing ", " 111 뿔카노 Rhyhorn ", " 112 코뿌리 Rhydon ", " 113 럭키 Chansey ", " 114 덩쿠리 Tangela ", " 115 캥카 Kangaskhan ", " 116 쏘드라 Horsea ", " 117 시드라 Seadra ", " 118 콘치 Goldeen ", " 119 왕콘치 Seaking ", " 120 별가사리 Staryu ", " 121 아쿠스타 Starmie ", " 122 마임맨 Mr. Mime ", " 123 스라크 Scyther ", " 124 루주라 Jynx ", " 125 에레브 Electabuzz ", " 126 마그마 Magmar ", " 127 쁘사이저 Pinsir ", " 128 켄타로스 Tauros ", " 129 잉어킹 Magikarp ", " 130 갸라도스 Gyarados ", " 131 라프라스 Lapras ", " 132 메타몽 Ditto ", " 133 이브이 Eevee ", " 134 샤미드 Vaporeon ", " 135 쥬피썬더 Jolteon ", " 136 부스터 Flareon ", " 137 폴리곤 Porygon ", " 138 암나이트 Omanyte ", " 139 암스타 Omastar ", " 140 투구 Kabuto ", " 141 투구푸스 Kabutops ", " 142 프테라 Aerodactyl ", " 143 잠만보 Snorlax ", " 144 프리져 Articuno ", " 145 썬더 Zapdos ", " 146 파이어 Moltres ", " 147 미뇽 Dratini ", " 148 신뇽 Dragonair ", " 149 망나뇽 Dragonite ", " 150 뮤츠 Mewtwo ", " 151 뮤 Mew "};
    int[] hexArr;

    //img upload var
    private static int RESULT_LOAD_IMG = 1;
    private String imgDecodableString;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9100766434021280~4396242656");
        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        backPressCloseHandler = new BackPressCloseHandler(this);

        sMapFragment = SupportMapFragment.newInstance();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .addApi(AppInvite.API)
                .build();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);

        BottomSheetBehavior behavior = BottomSheetBehavior.from(llBottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // google adMob adview implementing

        //mAdView = new AdView(this);
        mAdView = (AdView) findViewById(R.id.adView);
        //mAdView.setAdSize(AdSize.SMART_BANNER);
        //mAdView.setAdUnitId("ca-app-pub-9488666633576327/5185503697");
        final AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //RelativeLayout layout = (RelativeLayout)findViewById(R.id.ad_test);
        //layout.addView(mAdView);
        //AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        //mAdView.loadAd(adRequestBuilder.build());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                TextView userinfo_name = (TextView) findViewById(R.id.drawer_user_name);
                TextView userinfo_email = (TextView) findViewById(R.id.drawer_user_email);
                String name = CurrentUser.getDisplayName().toString();
                String email = CurrentUser.getEmail().toString();
                Uri photoUrl = CurrentUser.getPhotoUrl();
                userinfo_name.setText(name);
                userinfo_email.setText(email);
                mAdView.loadAd(adRequest);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));

        android.app.FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();

        sMapFragment.getMapAsync(this);

        // Initialize Firebase Auth, Sign in method for unauthenticated User
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backPressCloseHandler.onBackPressed();
        }
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                finish();
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            /*case R.id.invite_menu:
                sendInvitation();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }

        //return super.onOptionsItemSelected(item);
    }

    // Navigation drawer fragment selecting function
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        android.app.FragmentManager fm = getFragmentManager();

        FragmentManager sFm = getSupportFragmentManager();

        int id = item.getItemId();

        if (!sMapFragment.isAdded())
            sFm.beginTransaction().add(R.id.map, sMapFragment).commit();


        if (id == R.id.nav_gallery) {
            sFm.beginTransaction().show(sMapFragment).commit();

        } else if (id == R.id.nav_camera) {
            closing();
            sFm.beginTransaction().hide(sMapFragment).commit();
            fm.beginTransaction().replace(R.id.content_frame, new ImportFragment()).commit();

            // Handle the camera action
        } else if (id == R.id.nav_slideshow) {
            closing();
            sFm.beginTransaction().hide(sMapFragment).commit();
            fm.beginTransaction().replace(R.id.content_frame, new FreeBoard()).commit();

        } /*else if (id == R.id.nav_manage) {
            closing();
        }*/
        else if (id == R.id.nav_share) {
            Toast.makeText(getApplication(), "플레이 스토어에서 자세히 보기를 눌러주세요.", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(getApplication(), "플레이 스토어에서 댓글로 남겨주세요.", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    // Map listener functions
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getApplication(), "PokeData v1.0. J&B Mobile", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplication(), "화면을 길게 눌러서 포켓몬을 추가해주세요~!", Toast.LENGTH_LONG).show();

        mMap = googleMap;
        mAdView.loadAd(adRequest);

        // Map UI setting
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);

        // Add a marker in Sydney and move the camera
        LatLng sokcho = new LatLng(38.206983, 128.591848);
        mMap.addMarker(new MarkerOptions().position(sokcho).title("여기는 속초마을~").snippet("놀러오세요~~"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sokcho));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            Toast.makeText(getApplication(), "현재 위치를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

        final String[] stopgyminfo = {"포켓몬센터","체육관"};

        final Query pokemonlist = mDatabase.child("pokemonPosts");
        final Query stopgymlist = mDatabase.child("pokemon_StopGym");
        pokemonlist.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                pokemonPost mapinfo = dataSnapshot.getValue(pokemonPost.class);
                if (mapinfo.pokenumber != 0) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(mapinfo.enlat, mapinfo.enlng))
                            .title(textArr[(int) mapinfo.pokenumber])
                            .snippet(mapinfo.comment + "  by." + mapinfo.username)
                            .icon(BitmapDescriptorFactory.fromResource(0x7f030008 + Integer.valueOf(Integer.toHexString((int) mapinfo.pokenumber - 1), 16))));
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        stopgymlist.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                pokeStopGym admininfo = dataSnapshot.getValue(pokeStopGym.class);
                mMap.addMarker(new MarkerOptions().position(new LatLng(admininfo.enlat, admininfo.enlng))
                        .title(stopgyminfo[admininfo.type - 1])
                        .icon(BitmapDescriptorFactory.fromResource(0x7f03009f + Integer.valueOf(Integer.toHexString(admininfo.type - 1), 16))));
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onMapLongClick(final LatLng point) {
        Toast.makeText(getApplication(), "허위 입력시 계정이 차단될 수 있습니다.", Toast.LENGTH_SHORT).show();
        mAdView.loadAd(adRequest);
        if (marker != null)
            marker.remove();

        marker = mMap.addMarker(new MarkerOptions().position(point).title("요기는~~").snippet("*** 출몰지역"));
        LatLng fix = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        CameraPosition currentPlace = new CameraPosition.Builder().target(fix).zoom(mMap.getCameraPosition().zoom).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));

        llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(llBottomSheet);
        behavior.setPeekHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 72.f, getResources().getDisplayMetrics()));
        behavior.setHideable(true);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        hexArr = new int[152];
        hexArr[0]=R.mipmap.app_icon;
        for(int i=1; i<152; i++){
            hexArr[i]=0x7f030008 + Integer.valueOf(Integer.toHexString(i-1), 16);
        }

        final Spinner mySpinner = (Spinner) findViewById(R.id.pokemon_name);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.pokemon_dbrow, R.id.pokemon_dbname, textArr);
        //ArrayAdapter<Integer> adapter_pic = new ArrayAdapter<Integer>(this, R.layout.pokemon_dbrow, R.id.pokemon_icon, hexArr);
        Adapter adapter = new Adapter(this,textArr,hexArr);
        mySpinner.setAdapter(adapter);

        //mySpinner.setAdapter(adapter);
        //mySpinner.setAdapter(adapter_pic);

        // pokemon enrolling

        final String enname = CurrentUser.getDisplayName().toString();
        final String enemail = CurrentUser.getEmail().toString();
        final Uri enphotoUrl = CurrentUser.getPhotoUrl();
        enroll = (Button) findViewById(R.id.pokemon_upload);
        enroll.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(llBottomSheet);
                EditText comment = (EditText) findViewById(R.id.pokemon_content);
                String comment_str = comment.getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                pokemonPost newpokemon;
                if(mySpinner.getSelectedItemPosition() == 0)
                    Toast.makeText(getApplication(), "포켓몬을 고르세요.",Toast.LENGTH_SHORT).show();
                else {
                    if (enphotoUrl == null)
                        newpokemon = new pokemonPost(comment_str, enemail, point.latitude, point.longitude, "", mySpinner.getSelectedItemPosition(), "", 3, enname, Calendar.getInstance().getTime().toString());
                    else
                        newpokemon = new pokemonPost(comment_str, enemail, point.latitude, point.longitude, "", mySpinner.getSelectedItemPosition(), enphotoUrl.toString(), 3, enname, Calendar.getInstance().getTime().toString());

                    mDatabase.child("pokemonPosts").push().setValue(newpokemon);
                }

                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                mySpinner.setSelection(0);
                comment.setText("");
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if (marker != null)
                    marker.remove();
            }
        });
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mAdView.loadAd(adRequest);
        closing();
    }

    public void closing() {
        llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(llBottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        Spinner mySpinner = (Spinner) findViewById(R.id.pokemon_name);
        mySpinner.setSelection(0);

        EditText comment = (EditText) findViewById(R.id.pokemon_content);
        comment.setText("");

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(llBottomSheet.getWindowToken(), 0);

        if (marker != null)
            marker.remove();
    }

    // Image upload funciton
    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.pokemon_picture);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                imgView.setDrawingCacheEnabled(true);
                imgView.buildDrawingCache();
                Bitmap bitmap = imgView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                img_up = baos.toByteArray();

            } else {
                Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        Log.d("analytics", "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "sent");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE,payload);
                // Check how many invitations were sent and log.
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode,data);
                Log.d("analytics", "Invitations sent: " + ids.length);
            } else {
                Bundle payload = new Bundle();
                payload.putString(FirebaseAnalytics.Param.VALUE, "not sent");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE,payload);
                // Sending failed or it was canceled, show failure message to
                // the user
                Log.d("analytics", "Failed to send invitation.");
            }
        }
    }

    /*private void sendInvitation() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }*/

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.shine_star_11.abc/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.shine_star_11.abc/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("ConnectionFailed", "onConnectionFailed:" + connectionResult);
    }
}
