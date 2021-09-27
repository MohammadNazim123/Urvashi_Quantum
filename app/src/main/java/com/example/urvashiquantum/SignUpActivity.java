package com.example.urvashiquantum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class SignUpActivity extends AppCompatActivity {
    private EditText name,email,phone,password;
    private CountryCodePicker ccp;
    private Button signup_button;
    private CheckBox check;
    private TextView gotosignin;
    FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = findViewById(R.id.Name);
        email = findViewById(R.id.Email);
        phone = findViewById(R.id.MobileNumber);
        ccp = findViewById(R.id.CountryCodePicker);
        signup_button = findViewById(R.id.SignUpButton);
        password = findViewById(R.id.Password);
        check = findViewById(R.id.CheckBox);
        gotosignin = findViewById(R.id.GotoSignIn);
        Auth = FirebaseAuth.getInstance();

        String text="Log in With Terms & Conditions";
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
//                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
//                startActivity(intent);
//                finish();
            }
        };
        spannableString.setSpan(clickableSpan,12,30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.argb(250,214,42,11)), 12, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        check.setText(spannableString);
        check.setClickable(true);
        check.setMovementMethod(LinkMovementMethod.getInstance());

        String SignUpText="Already have an account? Sign in!";
        SpannableString spannableString2 = new SpannableString(SignUpText);
        ClickableSpan clickableSpan2=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        spannableString2.setSpan(clickableSpan2,25,33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString2.setSpan(new ForegroundColorSpan(Color.argb(250,214,42,11)), 25, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        gotosignin.setText(spannableString2);
        gotosignin.setClickable(true);
        gotosignin.setMovementMethod(LinkMovementMethod.getInstance());

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();
                String Email = email.getText().toString();
                String PhoneNumber = phone.getText().toString();
                String Password = password.getText().toString();
                String CCP = ccp.getDefaultCountryCodeWithPlus();
                String NumberWithCountryCode = CCP + PhoneNumber;

                if (TextUtils.isEmpty(Name)) {
                    name.setError("Field is Empty");
                }
                if (TextUtils.isEmpty(Email)) {
                    email.setError("Field is Empty");
                }
                if (TextUtils.isEmpty(PhoneNumber)) {
                    phone.setError("Field is Empty");

                }
                if (TextUtils.isEmpty(Password)) {
                    password.setError("Field is Empty");

                }
                if (Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    email.setError("Invalid Email Address");
                }
                if (Password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password should not be less than 6 digits", Toast.LENGTH_SHORT).show();
                } else {
                    if (check.isChecked()) {
                        Auth.createUserWithEmailAndPassword(Email, Password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            User user = new User(Email, Password, NumberWithCountryCode, Name);
                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
                                            ref.child(Password).setValue(user);
                                            Toast.makeText(getApplicationContext(), "SignUp SuccessFull", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));

                                        } else {
                                            Toast.makeText(getApplicationContext(), "SignUp Failed", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "Please accept terms & conditions", Toast.LENGTH_SHORT).show();
                    }

                }



            }
        });



    }
}

class User {
    String name;
    String email;
    String phone;
    String password;

    public User() {
    }

    public User(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}