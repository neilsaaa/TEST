package com.example.quiz.sqlite;
public class Question {
    public int id;
    public String content;
    public String options;
    public String answer;
    public Question(int id, String content, String options, String answer) {
        this.id = id;
        this.content = content;
        this.options = options;
        this.answer = answer;
    }
}
