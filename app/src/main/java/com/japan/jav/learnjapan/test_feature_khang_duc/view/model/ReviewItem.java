package com.japan.jav.learnjapan.test_feature_khang_duc.view.model;

import java.io.Serializable;

public class ReviewItem implements Serializable {
    private String question;
    private String answer;
    private String userAnswer;

    public ReviewItem(String question, String answer, String userAnswer) {
        this.question = question;
        this.answer = answer;
        this.userAnswer = userAnswer;
    }

    public ReviewItem() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
