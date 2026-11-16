package com.brunojaeger.chatgames.model;
import java.util.*;
public final class Challenge {
 private final String question, answer; private final List<String> options;
 public Challenge(String question,String answer,List<String> options){this.question=question==null?"":question;this.answer=answer==null?"":answer;this.options=options==null?Collections.emptyList():new ArrayList<>(options);}
 public String getQuestion(){return question;} public String getAnswer(){return answer;} public List<String> getOptions(){return Collections.unmodifiableList(options);}
}