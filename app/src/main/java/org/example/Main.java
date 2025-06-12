package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    record Agent(int id){}
    public static class RatingRecord implements Comparable<RatingRecord>{
        private Agent agent;
//        private Month month;
        private int ratingSum;
        private int count;

        public RatingRecord(Agent agent, Ratings rate){
            this.agent = agent;
            ratingSum = rate.value;
            count = 1;
        }


        public void addRating(Ratings rate) {
            ratingSum = ratingSum + rate.value;
            count++;
        }

        public double average() {
            return ((double) ratingSum/ count);
        }

        @Override
        public int compareTo(RatingRecord that) {
            int compare = Double.compare(that.average(), this.average());
            if(compare == 0) return that.count - this.count;
            return compare;
        }
    }

    enum Ratings {
        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

        private final int value;

        Ratings(int value) {
            this.value = value;
        }
    }
    record Rating(Ratings rate){}

    public static class AgentRating{
        private final Map<Agent, RatingRecord> agentRatingLog;
//        Map<Agent+Month, RatingRecord(agent, month)>

        public AgentRating(Map<Agent, RatingRecord> agentRatingLog) {
            this.agentRatingLog = agentRatingLog;
        }
        public AgentRating() {
            this(new HashMap<>());
        }

        public void addRating(Agent agent, Ratings rate) {
            if(agentRatingLog.containsKey(agent)) {
                RatingRecord record = agentRatingLog.get(agent);
                record.addRating(rate);
            } else {
                agentRatingLog.put(agent, new RatingRecord(agent,rate));
            }
        }

        public List<Agent> getSortedAgents() {
            return agentRatingLog.values().stream().sorted()
                    .map(r -> r.agent)
                    .collect(Collectors.toList());

        }
    }

}
