package com.example.myapplication;

import java.util.Random;

public class MathQuestion {
    private int numberA;
    private int numberB;
    private int correctAnswer;
    private String questionText;

    public MathQuestion() {
        generateNewQuestion();
    }

    // Hàm tạo phép tính cộng ngẫu nhiên (từ 10 đến 50)
    public void generateNewQuestion() {
        Random random = new Random();
        numberA = random.nextInt(41) + 10; // Random từ 10 đến 50
        numberB = random.nextInt(41) + 10;

        correctAnswer = numberA + numberB;
        questionText = numberA + " + " + numberB + " = ?";
    }

    // Lấy chuỗi câu hỏi để hiển thị lên màn hình
    public String getQuestionText() {
        return questionText;
    }

    // Kiểm tra đáp án người dùng nhập vào có đúng không
    public boolean isCorrect(int userAnswer) {
        return userAnswer == correctAnswer;
    }
}