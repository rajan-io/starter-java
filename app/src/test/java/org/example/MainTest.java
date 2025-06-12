package org.example;

import org.example.Main.Agent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class MainTest {

    @Test
    @DisplayName("should return sorted")
    void shouldReturnSorted() {
        //given
        Main.AgentRating agentRating = new Main.AgentRating();
        //when
        agentRating.addRating(new Agent(1), Main.Ratings.FIVE);
        List<Agent> sortedAgents = agentRating.getSortedAgents();

        //then
        assertThat(sortedAgents).isEqualTo(List.of(new Agent(1)));
    }

    @Test
    @DisplayName("should return sorted for multiple agent")
    void shouldReturnSortedForMultipleAgent() {
        //given
        Main.AgentRating agentRating = new Main.AgentRating();
        //when
        agentRating.addRating(new Agent(1), Main.Ratings.FIVE);
        agentRating.addRating(new Agent(1), Main.Ratings.FIVE);
        agentRating.addRating(new Agent(2), Main.Ratings.FOUR);
        agentRating.addRating(new Agent(3), Main.Ratings.FIVE);
        List<Agent> sortedAgents = agentRating.getSortedAgents();

        //then
        sortedAgents.forEach(System.out::println);
        assertThat(sortedAgents).isEqualTo(List.of(new Agent(1), new Agent(3), new Agent(2)));

    }

}
