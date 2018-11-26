package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.client.CandidateClientService;
import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.CandidateOutput;
import br.edu.ulbra.election.election.output.v1.ElectionCandidateResultOutput;
import br.edu.ulbra.election.election.output.v1.ElectionOutput;
import br.edu.ulbra.election.election.output.v1.ResultOutput;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import br.edu.ulbra.election.election.repository.VoteRepository;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class ResultService {
    private final VoteRepository voteRepository;
    private final ElectionRepository electionRepository;
    private final ModelMapper modelMapper;
    private final CandidateClientService candidateClientService;
    private final String MESSAGE_CANDIDATE_INVALID = "Invalid candidate";
    private final String MESSAGE_ELECTION_INVALID = "Invalid Election";

    @Autowired
    public ResultService(VoteRepository voteRepository, ElectionRepository electionRepository, ModelMapper modelMapper, CandidateClientService candidateClientService) {
        this.voteRepository = voteRepository;
        this.electionRepository = electionRepository;
        this.modelMapper = modelMapper;
        this.candidateClientService = candidateClientService;
    }

    public ElectionCandidateResultOutput getResultByCandidate(Long candidateId) {

        CandidateOutput candidateOutput = validateInput(candidateId);
        ElectionCandidateResultOutput electionCandidateResultOutput = new ElectionCandidateResultOutput();

        electionCandidateResultOutput.setCandidate(candidateOutput);
        electionCandidateResultOutput.setTotalVotes(voteRepository.countAllByCandidateId(candidateId));

        return electionCandidateResultOutput;
    }

    public ResultOutput getResultByElection(Long electionId){
        validateElectionInput(electionId);

        Election election = electionRepository.findById(electionId).orElse(null);
        ElectionOutput electionOutput = modelMapper.map(election, ElectionOutput.class);

        ResultOutput resultOutput = new ResultOutput();

        resultOutput.setElection(electionOutput);
        resultOutput.setCandidates(toElectionCandidateResultOutputList(electionId));
        resultOutput.setBlankVotes(voteRepository.countAllByBlankVoteEqualsAndElection_Id(true, electionId));
        resultOutput.setNullVotes(voteRepository.countAllByNullVoteEqualsAndElection_Id(true, electionId));
        resultOutput.setTotalVotes(voteRepository.countAllByElection_Id(electionId));

        return resultOutput;
    }

    public CandidateOutput validateInput(Long candidateId) {
        if( candidateId == null) {
            throw new GenericOutputException(MESSAGE_CANDIDATE_INVALID);
        }

        CandidateOutput candidateOutput = new CandidateOutput();

        try {
            candidateOutput = candidateClientService.getById(candidateId);
        } catch (FeignException e) {
            if(e.status() == 500) {
                throw new GenericOutputException(MESSAGE_CANDIDATE_INVALID);
            }
        }

        return candidateOutput;
    }

    public void validateElectionInput(Long electionId) {
        if(electionRepository.findById(electionId).orElse(null) == null) {
            throw new GenericOutputException(MESSAGE_ELECTION_INVALID);
        }
    }

    public List<ElectionCandidateResultOutput> toElectionCandidateResultOutputList(Long electionId) {
        List<Vote> voteList = voteRepository.findAllByElection_IdAndCandidateIdIsNotNull(electionId);
        HashSet<Long> ids = new HashSet<>();
        ArrayList<ElectionCandidateResultOutput> arrayCandidates = new ArrayList<>();
        Type electionOutputResultListType = new TypeToken<List<ElectionCandidateResultOutput>>(){}.getType();

        //unique candidates
        for (Vote vote : voteList) {
            ids.add(vote.getCandidateId());
        }

        //get candidatesoutputs
        for (Long number : ids) {
            arrayCandidates.add(toElectionCandidateResultOutput(number));
        }

        return modelMapper.map(arrayCandidates, electionOutputResultListType);
    }

    public ElectionCandidateResultOutput toElectionCandidateResultOutput(Long candidateId) {

        CandidateOutput candidateOutput = new CandidateOutput();

        try {
            candidateOutput = candidateClientService.getById(candidateId);
        } catch (FeignException e) {
            if(e.status() == 500) {
                throw new GenericOutputException(MESSAGE_CANDIDATE_INVALID);
            }
        }

        ElectionCandidateResultOutput electionCandidateResultOutput = new ElectionCandidateResultOutput();

        electionCandidateResultOutput.setCandidate(candidateOutput);
        electionCandidateResultOutput.setTotalVotes(voteRepository.countAllByCandidateId(candidateId));

        return electionCandidateResultOutput;
    }
}
