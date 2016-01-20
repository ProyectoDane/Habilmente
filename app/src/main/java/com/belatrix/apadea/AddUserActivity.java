package com.belatrix.apadea;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.belatrix.apadea.datamanager.UserManager;
import com.belatrix.apadea.datamodel.User;
import com.belatrix.apadea.util.InputFilterMinMax;
import com.belatrix.apadea.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AddUserActivity extends Activity {

    public static final String USER_TYPE = "user_type";

    static final String SAVE = "GUARDAR";
    static final String USER_ID = "user_id";
    static final String SELECT_IMAGE_FROM = "Seleccionar imagen desde";

    private EditText mNameEditText;
    private EditText mLastNameEditText;
    private EditText mAgeEditText;
    private TextView mTitleTextView;

    private Button mCreateUserButton;
    private Button mDeleteUserButton;

    private RadioButton mFemaleRadioButton;
    private RadioButton mMaleRadioButton;

    private RadioGroup mRadioGroup;

    private ImageView mProfileImageView;

    private static final int SELECT_PICTURE = 1;
    private static final int TAKE_PICTURE = 2;

    private boolean mEditModeEnabled = false;

    //UserDao attributes
    private String mName;
    private String mLastName;
    private int mAge;
    private String mUserType;

    private Long mCurrentSelectedUserId;

    private byte[] mProfileImageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        mUserType = getIntent().getExtras().get(USER_TYPE).toString();
        initializeUI();

        Intent intent = getIntent();

        mCurrentSelectedUserId = intent.getLongExtra(USER_ID, 0L);

        // if the comparison returns 0 there is no extra data from intent.
        int retValue = mCurrentSelectedUserId.compareTo(0L);

        if (retValue != 0) {
            initializeEditModeUI();
        } else {
            System.out.println("obj1 is equal to obj2");
        }

        mDeleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddUserActivity.this);
                builder.setTitle(getString(R.string.delete_user));
                builder.setMessage(getString(R.string.delete_user_message));
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserManager.getsInstance(AddUserActivity.this).getDaoSession().getUserDao().deleteByKey(mCurrentSelectedUserId);
                        Toast.makeText(AddUserActivity.this,getString(R.string.user_deleted), Toast.LENGTH_SHORT);
                        finish();
                    }
                });

                builder.create().show();
            }
        });

        mCreateUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName = mNameEditText.getText().toString();
                mLastName = mLastNameEditText.getText().toString();
                try {
                    mAge = Integer.parseInt(mAgeEditText.getText().toString());
                } catch (NumberFormatException e) {
                    mAge = 0;
                }

                if (mName.length() < 1 || mLastName.length() < 1 || mAge == 0) {
                    Toast.makeText(AddUserActivity.this,"Datos incompletos",Toast.LENGTH_LONG).show();
                    return;
                }

                User user = new User();
                user.setFirstName(mName);
                user.setLastName(mLastName);
                user.setAge(mAge);

                if(mRadioGroup.getCheckedRadioButtonId() == R.id.femaleRadioButton) {
                    user.setGender("F");
                } else {
                    user.setGender("M");
                }

                user.setType(mUserType);

                if (mProfileImageByteArray == null) {

                    ArrayList<User> mUsersList = UserManager.getsInstance(AddUserActivity.this).fetchUsersByType(mUserType);
                    int index = mUsersList.size() + 1;

                    TextDrawable drawable = TextDrawable.builder()
                            .beginConfig()
                            .withBorder(4) /* thickness in px */
                            .width(200)
                            .height(200)
                            .endConfig()
                            .buildRoundRect(mName.substring(0, 1).toUpperCase(), Utils.getImageColor(index), 10);

                    user.setProfileImage(convertBitmap(drawableToBitmap(drawable)));
                } else {
                    user.setProfileImage(mProfileImageByteArray);
                }

                if (mEditModeEnabled) {
                    user.setId(mCurrentSelectedUserId);
                    UserManager.getsInstance(AddUserActivity.this).getDaoSession().getUserDao().update(user);
                } else {
                    UserManager.getsInstance(AddUserActivity.this).getDaoSession().getUserDao().insert(user);
                }
                finish();
            }
        });
    }

    private void initializeUI() {
        mNameEditText = (EditText) findViewById(R.id.nameEditText);
        mLastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        mAgeEditText = (EditText) findViewById(R.id.ageEditText);
        mAgeEditText.setFilters(new InputFilter[]{new InputFilterMinMax("1", "100")});
        mCreateUserButton = (Button) findViewById(R.id.createUserButton);
        mTitleTextView = (TextView) findViewById(R.id.title);

        if(mUserType.equals(User.USER_TYPE_SUBJECT)) {
            mTitleTextView.setText(getResources().getString(R.string.add_subject));
        } else {
            mTitleTextView.setText(getResources().getString(R.string.add_therapist));
        }

        mRadioGroup = (RadioGroup) findViewById(R.id.radioSexButtons);
        mFemaleRadioButton = (RadioButton) findViewById(R.id.femaleRadioButton);
        mMaleRadioButton = (RadioButton) findViewById(R.id.maleRadioButton);
        mMaleRadioButton.setChecked(true);

        mProfileImageView = (ImageView) findViewById(R.id.profileImageView);

        mDeleteUserButton = (Button) findViewById(R.id.deleteUserButton);

        Button mSelectProfileImageButton = (Button) findViewById(R.id.selectProfileImageButton);
        mSelectProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });
    }

    private void initializeEditModeUI() {
        mEditModeEnabled = true;
        mDeleteUserButton.setVisibility(View.VISIBLE);

        if(mUserType.equals(User.USER_TYPE_SUBJECT)) {
            mTitleTextView.setText(getResources().getString(R.string.edit_subject));
        } else {
            mTitleTextView.setText(getResources().getString(R.string.edit_therapist));
        }

        mCreateUserButton.setText(SAVE);

        loadUserProfile();
    }

    private void loadUserProfile() {
        User editUser = UserManager.getsInstance(this).getDaoSession().getUserDao().load(mCurrentSelectedUserId);

        mNameEditText.setText(editUser.getFirstName());
        mLastNameEditText.setText(editUser.getLastName());
        mAgeEditText.setText(editUser.getAge().toString());

        if(editUser.getGender().equals("F")) {
            mFemaleRadioButton.setChecked(true);
        } else {
            mMaleRadioButton.setChecked(true);
        }

        mProfileImageView.setImageBitmap(convertBytesToBitmap(editUser.getProfileImage()));
        mUserType = editUser.getType();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                try {
                    Uri selectedImage = data.getData();
                    Bitmap yourSelectedImage = decodeUri(selectedImage);

                    try {
                        mProfileImageView.setImageBitmap(yourSelectedImage);
                        mProfileImageByteArray = convertBitmap(yourSelectedImage);
                        String imagepath_new = getRealPathFromURI(selectedImage);

                        System.out.println("gakk" + imagepath_new);
                        String[] s = imagepath_new.split("/");
                        System.out.println(s[s.length - 1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == TAKE_PICTURE) {
                //create instance of File with same name we created before to get image from storage
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                Bitmap resizedImage = Bitmap.createScaledBitmap(decodeSampledBitmapFromFile(file.getAbsolutePath(), 600, 450), 800, 800, true);

                mProfileImageView.setImageBitmap(resizedImage);
                mProfileImageByteArray = convertBitmap(resizedImage);
            }
        }
    }

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setMessage(SELECT_IMAGE_FROM);
        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
                    }
                });
        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                       /* create an instance of intent
     * pass action android.media.action.IMAGE_CAPTURE
     * as argument to launch camera
     */
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    /*create instance of File with name img.jpg*/
                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
    /*put uri as extra in intent object*/
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
    /*start activity for result pass intent as argument and request code */
                        startActivityForResult(intent, TAKE_PICTURE);
                    }
                });
        myAlertDialog.show();
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //Query bitmap without allocating memory
        options.inJustDecodeBounds = true;
        //decode file from path
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        //decode according to configuration or according best match
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;
        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;
        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }
        //if value is greater than 1,sub sample the original image
        options.inSampleSize = inSampleSize;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private byte[] convertBitmap(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 3;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }

    public String getRealPathFromURI(Uri contentUri) {
        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, // Which columns to
                // return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap convertBytesToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}
