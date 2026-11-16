package com.brunojaeger.chatgames.model;
import java.util.*;
public final class GameDefinition {
 private final String name,type,displayName; private final int timeoutSeconds; private final double reward; private final List<Challenge> challenges;
 public GameDefinition(String name,String type,String displayName,int timeoutSeconds,double reward,List<Challenge> challenges){this.name=name;this.type=type;this.displayName=displayName;this.timeoutSeconds=Math.max(5,timeoutSeconds);this.reward=Math.max(0,reward);this.challenges=new ArrayList<>(challenges);}
 public String getName(){return name;} public String getType(){return type;} public String getDisplayName(){return displayName;} public int getTimeoutSeconds(){return timeoutSeconds;} public double getReward(){return reward;} public List<Challenge> getChallenges(){return Collections.unmodifiableList(challenges);}
}