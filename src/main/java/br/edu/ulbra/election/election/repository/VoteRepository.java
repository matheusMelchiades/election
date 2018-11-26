package br.edu.ulbra.election.election.repository;

import br.edu.ulbra.election.election.model.Vote;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends CrudRepository<Vote, Long> {

    Optional<Vote> findByVoterIdAndElection_Id(Long voteId, Long electionId);

    Long countAllByCandidateId(Long candidateId);

    Long countAllByElection_Id(Long electionId);

    Long countAllByBlankVoteEqualsAndElection_Id(Boolean count, Long electionId);

    Long countAllByNullVoteEqualsAndElection_Id(Boolean count, Long electionId);

    List<Vote> findAllByElection_IdAndCandidateIdIsNotNull(Long electionId);

}
