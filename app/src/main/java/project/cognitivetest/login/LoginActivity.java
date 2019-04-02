package project.cognitivetest.login;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import java.util.ArrayList;

import project.cognitivetest.R;
import project.cognitivetest.modules.Security;
import project.cognitivetest.modules.User;

public class LoginActivity extends AppCompatActivity implements OnClickListener,
        OnItemClickListener, OnDismissListener {

    protected static final String TAG = "LoginActivity";
    private LinearLayout mLoginLinearLayout; // the container for the login content
    private LinearLayout mUserIdLinearLayout; // the drop-down pop-up window will be displayed below the container
    private Animation mTranslate; // displacement animation
    private Dialog mLoginingDlg; // displays the Dialog being logged in
    private EditText mIdEditText; // login ID edit box
    private EditText mPwdEditText; // login password edit box
    private ImageView mMoreUsers; // drop down icon
    private Button mLoginButton; // login button
    private ImageView mLoginMoreUserView; // pull down the popup button
    private String mIdString;
    private String mPwdString;
    private ArrayList<Security> mUser; // user list
    private ListView mUserIdListView; // the ListView object displayed in the drop-down pop-up window
    private LoginAapter mAdapter; //ListView listener
    private PopupWindow mPop; // the pulled down pop-up window
    private CheckBox checkRemPWD; //check box for selecting whether to save the password

    private CheckBox isRemPWD =  (CheckBox) findViewById(R.id.login_remPWD);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setListener();
        mLoginLinearLayout.startAnimation(mTranslate);//Y axis horizontal movement

        /* get saved user password*/
        mUser = LocalUserList.getUserList(LoginActivity.this);

        /* displays the first user in the list in the edit box */
        if (mUser.size() > 0) {
            //            put the first user into edittext field
            mIdEditText.setText(mUser.get(0).getUsername());
            mPwdEditText.setText(mUser.get(0).getPassword());
        }

        LinearLayout parent = (LinearLayout) getLayoutInflater().inflate(
                R.layout.userifo_listview, null);
        mUserIdListView = (ListView) parent.findViewById(android.R.id.list);
        parent.removeView(mUserIdListView); // must disengage from the parent-child relationship, or you will report an error
        mUserIdListView.setOnItemClickListener(this); // set the click event
        LoginAapter mAdapter = new LoginAapter(mUser);
        mUserIdListView.setAdapter(mAdapter);
    }

    private void initView() {
        mIdEditText = (EditText) findViewById(R.id.login_edtId);
        mPwdEditText = (EditText) findViewById(R.id.login_edtPwd);
        mMoreUsers = (ImageView) findViewById(R.id.login_more_user);
        mLoginButton = (Button) findViewById(R.id.login_btnLogin);
        mLoginMoreUserView = (ImageView) findViewById(R.id.login_more_user);
        mLoginLinearLayout = (LinearLayout) findViewById(R.id.login_linearLayout);
        mUserIdLinearLayout = (LinearLayout) findViewById(R.id.userId_LinearLayout);
        mTranslate = AnimationUtils.loadAnimation(this, R.anim.my_translate); // initial the animation
        initLoginingDlg();
    }

    public void initPop() {
        int width = mUserIdLinearLayout.getWidth() - 4;
        int height = LayoutParams.WRAP_CONTENT;
        mPop = new PopupWindow(mUserIdListView, width, height, true);
        mPop.setOnDismissListener(this);// Set the listener when the pop-up window disappears

        // Click the other area of the popup window to make the window disappear
        mPop.setBackgroundDrawable(new ColorDrawable(0xffffffff));
    }

    /* initial the login Dialog */
    private void initLoginingDlg() {

        mLoginingDlg = new Dialog(this, R.style.logining_dlg);
        mLoginingDlg.setContentView(R.layout.logining_dlg);

        Window window = mLoginingDlg.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // acquire the parameters about current window to set the parameters of the mLoginingDlg

        // acquire the height and width of the screen
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int cxScreen = dm.widthPixels;
        int cyScreen = dm.heightPixels;

        int height = (int) getResources().getDimension(
                R.dimen.loginingdlg_lr_margin);// height 42dp
        int lrMargin = (int) getResources().getDimension(
                R.dimen.loginingdlg_lr_margin); // margin to double sides 10dp
        int topMargin = (int) getResources().getDimension(
                R.dimen.loginingdlg_top_margin); // top margin 20dp

        params.y = (-(cyScreen - height) / 2) + topMargin; // -199
        /* put the window in the center of the screen, so x,y will show the The offset from this control to the center of the screen*/

        params.width = cxScreen;
        params.height = height;
        // width,height means actual size of mLoginingDlg

        mLoginingDlg.setCanceledOnTouchOutside(true); // Click any external Dialog area to close Dialog
    }

    /* show the logging dialog box */
    private void showLoginingDlg() {
        if (mLoginingDlg != null)
            mLoginingDlg.show();
    }

    /* close the logging dialog box */
    private void closeLoginingDlg() {
        if (mLoginingDlg != null && mLoginingDlg.isShowing())
            mLoginingDlg.dismiss();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btnLogin:
                // start login

                showLoginingDlg(); //show "log in" dialog box, as the activity is not connected with the server, the actual result cannot be shown now
                Log.i(TAG, mIdString + "  " + mPwdString);
                if (mIdString == null || mIdString.equals("")) { // when the username filed is empty, it will show "Please enter username"
                    Toast.makeText(LoginActivity.this, "Please enter username", Toast.LENGTH_SHORT)
                            .show();
                } else if (mPwdString == null || mPwdString.equals("")) {// when the pwd filed is empty, it will show "Please enter password"

                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT)
                            .show();
                } else {//when the username and pwd are both not null

                    try {

                        checkRemPWD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton,
                                                         boolean isChecked) {
                                boolean mIsSave = true;
                                if(isChecked){
                                    Log.i(TAG, "Save user list");
                                    for (Security user : mUser) { // Determines whether the local document has this ID user
                                        if (user.getUsername().equals(mIdString)) {
                                            mIsSave = false;
                                            break;
                                        }
                                    }
                                    if (mIsSave) { // put the current user in to userlist
                                        Security user = new Security(mIdString, mPwdString);
                                        mUser.add(user);
                                        //unfinished waiting for the verify process
                                    }
                                }else{
//unfinished waiting for the verify process
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    closeLoginingDlg();// close dialog box
                    Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case R.id.login_more_user: // when click drop down list
                if (mPop == null) {
                    initPop();
                }
                if (!mPop.isShowing() && mUser.size() > 0) {
                    Log.i(TAG, "the dropdown icon will change to pull up icon");
                    mMoreUsers.setImageResource(R.drawable.login_more_down); // change icon
                    mPop.showAsDropDown(mUserIdLinearLayout, 2, 1); // show pop box
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mIdEditText.setText(mUser.get(i).getUsername());
        mPwdEditText.setText(mUser.get(i).getPassword());
        mPop.dismiss();
    }

    @Override
    //When the PopupWindow object is dismiss
    public void onDismiss() {
        // Log.i(TAG, "change to pull up icon");
        mMoreUsers.setImageResource(R.drawable.login_more_up);
    }

    private void setListener() {
        mIdEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mIdString = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        mPwdEditText.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                mPwdString = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        mLoginButton.setOnClickListener(this);
        mLoginMoreUserView.setOnClickListener(this);
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            checkRemPWD.setChecked(true);
        } else {
            checkRemPWD.setChecked(false);
        }
    }

    /*Adaptor of the Viewlist*/
    class LoginAapter extends ArrayAdapter<Security> {

        public LoginAapter(ArrayList<Security> users) {
            super(LoginActivity.this,0,users);
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(
                        R.layout.listview_item, null);
            }

            TextView userIdText = (TextView) convertView
                    .findViewById(R.id.listview_userid);
            userIdText.setText(getItem(position).getUsername());

            ImageView deleteUser = (ImageView) convertView
                    .findViewById(R.id.login_delete_user);
            deleteUser.setOnClickListener(new OnClickListener() {
                //  when click the deleteUser, it will delete the selected element in the mUser
                @Override
                public void onClick(View v) {

                    if (getItem(position).getUsername().equals(mIdString)) {
                        // if the username of detected user is same with the current text in the text field, clear them
                        mIdString = "";
                        mPwdString = "";
                        mIdEditText.setText(mIdString);
                        mPwdEditText.setText(mPwdString);
                    }
                    mUser.remove(getItem(position));
                    mAdapter.notifyDataSetChanged(); // renew ListView
                }
            });
            return convertView;
        }

    }

    /* save user list when exit the activity */
    @Override
    public void onPause() {
        super.onPause();
        try {
            LocalUserList.saveUserList(LoginActivity.this, mUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
