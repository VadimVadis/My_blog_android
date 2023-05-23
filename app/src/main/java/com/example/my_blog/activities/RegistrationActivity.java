package com.example.my_blog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my_blog.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistrationActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etPassword2;
    private Button btnRegister;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etPassword2 = findViewById(R.id.et_password2);
        btnRegister = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String password2 = etPassword2.getText().toString().trim();

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("username", username);
            jsonRequest.put("password", password);
            jsonRequest.put("password2", password2);
            System.out.println(username);
            System.out.println(password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Отправка данных на сервер
        sendRequest(jsonRequest.toString());
    }

    private void sendRequest(final String json) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://193.168.49.71:5000/api/register");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    OutputStream os = connection.getOutputStream();
                    DataOutputStream writer = new DataOutputStream(os);
                    writer.writeBytes(json);
                    writer.flush();
                    writer.close();
                    os.close();
                    int responseCode = connection.getResponseCode();
                    System.out.println(responseCode);
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Успешно зарегистрировано
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                // Переход к экрану авторизации
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        // Ошибка при регистрации
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}